package id.manajemenasrama.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.manajemenasrama.constant.ResponseCode;
import id.manajemenasrama.util.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security Configuration - Simple JWT Authentication
 * <p>
 * - /api/auth/login - Login dengan username/password → return JWT token
 * - /api/auth/register - Register user baru
 * - /api/** - Protected endpoints, butuh Bearer token
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF untuk REST API
                .csrf(csrf -> csrf.disable())

                // Enable CORS for all origins
                .cors(Customizer.withDefaults())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/login",
                                "/error"
                        ).permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // JWT Resource Server - validate Bearer tokens
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint((request, response, authException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN)
                        )
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN)
                        )
                )

                // Stateless session - no cookies
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("scope");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    private void writeErrorResponse(HttpServletResponse response, int statusCode) throws java.io.IOException {
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(
                response.getWriter(),
                ResponseUtil.<Void>buildSuccessResponse(ResponseCode.UNAUTHORIZED, null)
        );
    }
}

