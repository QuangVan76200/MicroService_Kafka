package com.service.acountservice.accountservice.service;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.service.acountservice.accountservice.authException.AuthException;
import com.service.acountservice.accountservice.dao.IAccountDao;
import com.service.acountservice.accountservice.data.Account;
import com.service.acountservice.accountservice.model.auth.TokenInfo;
import com.service.acountservice.accountservice.utils.PBKDF2Encoder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

/**
 * SecurityService class
 *
 * @author VanLQ
 */
@Component
public class SecurityService implements Serializable {
    private final IAccountDao userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String defaultExpirationTimeInSecondsConf;

    public SecurityService(IAccountDao userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenInfo generateAccessToken(Account user) {
        var claims = new HashMap<String, Object>() {{
            put("role", user.getRole());
        }};

        return doGenerateToken(claims, user.getUsername(), user.getId().toString());
    }

    private TokenInfo doGenerateToken(Map<String, Object> claims, String issuer, String subject) {
        var expirationTimeInMilliseconds = Long.parseLong(defaultExpirationTimeInSecondsConf) * 1000;
        var expirationDate = new Date(new Date().getTime() + expirationTimeInMilliseconds);

        return doGenerateToken(expirationDate, claims, issuer, subject);
    }

    private TokenInfo doGenerateToken(Date expirationDate, Map<String, Object> claims, String issuer, String subject) {
        var createdDate = new Date();
        var token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenInfo.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    public Mono<TokenInfo> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled())
                        return Mono.error(new AuthException("Account disabled.", "USER_ACCOUNT_DISABLED"));
                    String passwordRequest = passwordEncoder.encode(password);
                    String passwordInDB = user.getPassword();
                    
                    System.out.println("passwordRequest "+passwordRequest);
                    System.out.println("passwordInDB "+passwordInDB);
                    
                    
                    if (!passwordEncoder.encode(password).equals(user.getPassword()))
                        return Mono.error(new AuthException("Invalid user password!", "INVALID_USER_PASSWORD"));

                    return Mono.just(generateAccessToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                }).switchIfEmpty(Mono.error(new AuthException("Invalid user, " + username + " is not registered.", "INVALID_USERNAME")));
    }
                
}

