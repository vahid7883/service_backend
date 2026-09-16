package com.numjew.service_backend.auth;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {

//    public static final  String SECRET ="5df18640b29d8a3d7e99b16bb50f9e692e65b4c56e63b7d8fbfe4a557ceff772bda6697264ad81fe414a027c5f17a79c03e7897232ff9f7c35bad6d49ad7351fa7536473b771c125e7a2de02d78e40c000b250eb937840d8354fe339a69346b6fe1cc242a499dcfa4f0424f2373498e4ecc8f8b5ef133d8177c214dabd2c35373b95d9a820b6b6e5bd09a628cf6e6a2579111ee69c97fff81160e7da2cbf79c5177c533c4db902ae4b05373a37ad3cff91fe4deaa023941992d02b9441a7bc574c56c7d01fe65bc8d9066eb47ea6f3f97e34d009fb4a5e3dc7685801972f8034b4edb342ebd91664ea8fce2835d8206a49b9b673237432e140d3529aa05b64cb";

//   @Value("${jwt.secretKey}")
    private String secretKey;


    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
