package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.auth.LoginRequest;
import id.manajemenasrama.dto.auth.TokenResponse;
import id.manajemenasrama.dto.user.ProfileResponseDto;
import id.manajemenasrama.dto.user.UpdateProfileRequestDto;
import id.manajemenasrama.dto.user.UserResponseDto;
import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.exception.UnauthorizedException;
import id.manajemenasrama.repository.RoleRepository;
import id.manajemenasrama.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void me_should_return_current_user_profile() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "AdminUser",
                        "secret",
                        List.of(new SimpleGrantedAuthority("SUPER_ADMIN"))
                )
        );
        Role role = Role.builder().name("SUPER_ADMIN").build();
        User user = User.builder()
                .id(1L)
                .username("adminuser")
                .fullName("Admin User")
                .role(role)
                .build();
        when(userRepository.findByUsername("adminuser")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.me();

        assertThat(response.getBody()).isNotNull();
        Object data = ((id.manajemenasrama.dto.BaseResponse<?>) response.getBody()).getData();
        assertThat(data).isInstanceOf(ProfileResponseDto.class);
        ProfileResponseDto profile = (ProfileResponseDto) data;
        assertThat(profile.getUsername()).isEqualTo("adminuser");
        assertThat(profile.getFullName()).isEqualTo("Admin User");
        assertThat(profile.getRole()).isEqualTo("SUPER_ADMIN");
    }

    @Test
    void me_should_reject_anonymous_user() {
        assertThatThrownBy(() -> userService.me())
                .isInstanceOf(UnauthorizedException.class)
                .extracting(ex -> ((UnauthorizedException) ex).getResponseCode())
                .isEqualTo(ResponseCode.UNAUTHORIZED);
    }

    @Test
    void delete_should_soft_delete_user() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("operator", "secret")
        );
        User user = User.builder().id(9L).username("target").build();
        when(userRepository.findByUsername("target")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = userService.delete("target");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getDeleted()).isNotNull();
        assertThat(saved.getUpdatedBy()).isEqualTo("operator");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void meUpdate_should_change_username_and_return_new_token_when_password_is_updated() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("current_user", "secret")
        );

        User user = User.builder()
                .id(1L)
                .username("current_user")
                .fullName("Current User")
                .build();
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIdNot("new_user", 1L)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("new_password")).thenReturn("hashed-new-password");
        when(authService.loginToken(any(LoginRequest.class))).thenReturn(
                TokenResponse.builder()
                        .accessToken("new-token")
                        .tokenType("Bearer")
                        .expiresIn(3600)
                        .scope("SUPER_ADMIN")
                        .build()
        );
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateProfileRequestDto request = new UpdateProfileRequestDto();
        request.setUsername("new_user");
        request.setFullName("New User");
        request.setPassword("new_password");

        ResponseEntity<?> response = userService.meUpdate(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("new_user");
        assertThat(saved.getFullName()).isEqualTo("New User");
        assertThat(saved.getPassword()).isEqualTo("hashed-new-password");
        assertThat(saved.getUpdatedBy()).isEqualTo("current_user");
        assertThat(response.getBody()).isNotNull();
        Object tokenData = ((id.manajemenasrama.dto.BaseResponse<?>) response.getBody()).getData();
        assertThat(tokenData).isInstanceOf(TokenResponse.class);
        assertThat(((TokenResponse) tokenData).getAccessToken()).isEqualTo("new-token");
    }
}

