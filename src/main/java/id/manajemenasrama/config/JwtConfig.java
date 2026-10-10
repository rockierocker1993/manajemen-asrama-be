package id.manajemenasrama.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * JWT Configuration - stable HMAC secret for signing and validating JWT tokens.
 */
@Configuration
public class JwtConfig {

    private final SecretKey secretKey;

    public JwtConfig(@Value("${jwt.secret}") String secret) {
        this.secretKey = resolveSecretKey(secret);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(secretKey));
    }

    private SecretKey resolveSecretKey(String secret) {
        String normalizedSecret = secret == null ? "" : secret.trim();
        if (normalizedSecret.isEmpty()) {
            throw new IllegalStateException("jwt.secret must not be empty");
        }

        byte[] keyBytes = isHex(normalizedSecret)
                ? HexFormat.of().parseHex(normalizedSecret)
                : normalizedSecret.getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    private boolean isHex(String value) {
        return value.length() % 2 == 0 && value.matches("^[0-9a-fA-F]+$");
    }
}

