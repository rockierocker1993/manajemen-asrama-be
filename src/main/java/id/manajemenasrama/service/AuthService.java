package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.BaseResponse;
import id.manajemenasrama.dto.auth.LoginRequest;
import id.manajemenasrama.dto.auth.RegisterRequest;
import id.manajemenasrama.dto.auth.TokenResponse;
import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.exception.UnauthorizedException;
import id.manajemenasrama.repository.RoleRepository;
import id.manajemenasrama.repository.UserRepository;
import id.manajemenasrama.util.CommonUtil;
import id.manajemenasrama.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;

    public ResponseEntity<BaseResponse<TokenResponse>> login(LoginRequest loginRequest) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            Instant now = Instant.now();
            Instant expiresAt = now.plusMillis(jwtExpirationMs);
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("user-management-api")
                    .issuedAt(now)
                    .expiresAt(expiresAt)
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();

            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
            String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

            return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS,
                    TokenResponse.builder()
                            .accessToken(token)
                            .tokenType("Bearer")
                            .expiresIn(expiresAt.getEpochSecond() - now.getEpochSecond())
                            .scope(scope)
                            .build()
            ));

        } catch (AuthenticationException e) {
            throw new UnauthorizedException(ResponseCode.INVALID_USERNAME_PASSWORD);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<BaseResponse> registerUser(RegisterRequest request) {
        log.info("Registering new user with username: {}", request.getUsername());
        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new BadRequestException(ResponseCode.USERNAME_ALREADY_REGISTERED);
        }

        Role role = roleRepository.findByName(request.getRole()).orElseThrow(() -> new BadRequestException(ResponseCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .fullName(request.getFullName())
                .build();
        user.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        userRepository.save(user);
        log.info("User registered successfully with username: {}", request.getUsername());
        return ResponseEntity.ok(ResponseUtil.buildSuccessResponse(ResponseCode.SUCCESS, null));
    }

}
