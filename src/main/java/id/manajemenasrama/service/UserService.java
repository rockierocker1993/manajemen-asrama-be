package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.auth.LoginRequest;
import id.manajemenasrama.dto.auth.TokenResponse;
import id.manajemenasrama.dto.user.*;
import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.exception.UnauthorizedException;
import id.manajemenasrama.repository.RoleRepository;
import id.manajemenasrama.repository.UserRepository;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public ResponseEntity<BaseResponse<ProfileResponseDto>> me() {
        log.info("get profile current usser");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException(ResponseCode.UNAUTHORIZED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException(ResponseCode.UNAUTHORIZED));

        String role = user.getRole() != null
                ? user.getRole().getName()
                : null;

        ProfileResponseDto profile = ProfileResponseDto.builder()
                .fullName(user.getFullName())
                .id(user.getId())
                .username(user.getUsername())
                .role(role)
                .build();

        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, profile)
        );
    }

    public ResponseEntity<BaseResponse<List<UserResponseDto>>> getAll() {
        log.info("get all user");
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDto = new ArrayList<>();
        for (User user : users) {
            String role = user.getRole() != null
                    ? user.getRole().getName()
                    : null;
            userResponseDto.add(UserResponseDto.builder()
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .registerDate(user.getCreated())
                    .role(role)
                    .build());
        }
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, userResponseDto)
        );
    }

    public ResponseEntity<BaseResponse<Void>> delete(String username) {
        log.info("delete user");
        User user = userRepository.findByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException(ResponseCode.USER_NOT_FOUND));
        user.setDeleted(LocalDateTime.now());
        user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        userRepository.save(user);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.DELETE_SUCCESS, null)
        );
    }

    public ResponseEntity<BaseResponse<UserResponseDto>> update(UpdateUserRequestDto updateUserRequestDto) {
        log.info("update user");

        User user = userRepository.findByUsername(updateUserRequestDto.getUsername())
                .orElseThrow(() -> new BadRequestException(ResponseCode.USER_NOT_FOUND));
        Role role = roleRepository.findByName(updateUserRequestDto.getRole())
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROLE_NOT_FOUND));

        user.setFullName(updateUserRequestDto.getFullName());
        user.setRole(role);
        user.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        if (StringUtils.hasText(updateUserRequestDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateUserRequestDto.getPassword()));
        }
        userRepository.save(user);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.UPDATE_SUCCESS, UserResponseDto.builder()
                        .fullName(user.getFullName())
                        .username(user.getUsername())
                        .registerDate(user.getCreated())
                        .role(role.getName())
                        .build()
                )
        );
    }

    public ResponseEntity<BaseResponse<TokenResponse>> meUpdate(UpdateProfileRequestDto updateProfileRequestDto) {
        log.info("update user");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ResponseCode.USER_NOT_FOUND));
        User userExist = userRepository.findByUsernameAndIdNot(updateProfileRequestDto.getUsername(), user.getId())
                .orElse(null);
        if (userExist != null) {
            throw new BadRequestException(ResponseCode.USERNAME_ALREADY_REGISTERED);
        }

        user.setUsername(updateProfileRequestDto.getUsername());
        user.setFullName(updateProfileRequestDto.getFullName());
        user.setUpdatedBy(username);
        if (StringUtils.hasText(updateProfileRequestDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateProfileRequestDto.getPassword()));
        }
        userRepository.save(user);
        return ResponseEntity.ok(
                ResponseUtil.buildSuccessResponse(ResponseCode.UPDATE_SUCCESS, authService.loginToken(LoginRequest.builder()
                                .password(updateProfileRequestDto.getPassword())
                                .username(updateProfileRequestDto.getUsername())
                        .build()))
        );
    }
}