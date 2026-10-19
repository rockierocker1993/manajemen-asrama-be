package id.manajemenasrama.service;

import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.dto.auth.LoginRequest;
import id.manajemenasrama.dto.auth.RegisterRequest;
import id.manajemenasrama.dto.auth.TokenResponse;
import id.manajemenasrama.entity.Role;
import id.manajemenasrama.entity.User;
import id.manajemenasrama.exception.BadRequestException;
import id.manajemenasrama.exception.UnauthorizedException;
import id.manajemenasrama.repository.RoleRepository;
import id.manajemenasrama.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtExpirationMs", 3_600_000L);
    }

    @AfterEach
    void tearDown() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @Test
    void loginToken_should_return_jwt_response_when_credentials_are_valid() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                "secret",
                List.of(new SimpleGrantedAuthority("SUPER_ADMIN"))
        );
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(
                Jwt.withTokenValue("token-123")
                        .header("alg", "HS256")
                        .claim("sub", "admin")
                        .claim("scope", "SUPER_ADMIN")
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(3600))
                        .build()
        );

        TokenResponse tokenResponse = authService.loginToken(LoginRequest.builder()
                .username("admin")
                .password("secret")
                .build());

        assertThat(tokenResponse.getAccessToken()).isEqualTo("token-123");
        assertThat(tokenResponse.getTokenType()).isEqualTo("Bearer");
        assertThat(tokenResponse.getScope()).isEqualTo("SUPER_ADMIN");
        assertThat(tokenResponse.getExpiresIn()).isGreaterThan(0);
    }

    @Test
    void loginToken_should_throw_unauthorized_when_credentials_are_invalid() {
        when(authenticationManager.authenticate(any())).thenThrow(new org.springframework.security.authentication.BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.loginToken(LoginRequest.builder()
                .username("admin")
                .password("wrong")
                .build()))
                .isInstanceOf(UnauthorizedException.class)
                .extracting(ex -> ((UnauthorizedException) ex).getResponseCode())
                .isEqualTo(ResponseCode.INVALID_USERNAME_PASSWORD);
    }

    @Test
    void registerUser_should_save_user_with_encoded_password() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("creator", "secret")
        );

        Role role = Role.builder().name("PENGURUS").build();
        when(userRepository.findByUsername("new_user")).thenReturn(Optional.empty());
        when(roleRepository.findByName("PENGURUS")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = authService.registerUser(RegisterRequest.builder()
                .username("new_user")
                .password("password123")
                .fullName("New User")
                .role("PENGURUS")
                .build());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertThat(saved.getUsername()).isEqualTo("new_user");
        assertThat(saved.getPassword()).isEqualTo("hashed-password");
        assertThat(saved.getFullName()).isEqualTo("New User");
        assertThat(saved.getRole()).isSameAs(role);
        assertThat(saved.getCreatedBy()).isEqualTo("creator");
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void registerUser_should_reject_duplicate_username() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("creator", "secret")
        );
        when(userRepository.findByUsername("new_user")).thenReturn(Optional.of(User.builder().username("new_user").build()));

        assertThatThrownBy(() -> authService.registerUser(RegisterRequest.builder()
                .username("new_user")
                .password("password123")
                .fullName("New User")
                .role("PENGURUS")
                .build()))
                .isInstanceOf(BadRequestException.class)
                .extracting(ex -> ((BadRequestException) ex).getResponseCode())
                .isEqualTo(ResponseCode.USERNAME_ALREADY_REGISTERED);
    }
}

