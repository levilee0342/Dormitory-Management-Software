package com.ht.qlktx.filter;

import com.ht.qlktx.entities.Account;
import com.ht.qlktx.modules.account.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.security.jwt.access-secret-key}")
    private String accessSecretKey;
    @Value("${application.security.jwt.password-secret-key}")
    private String passwordSecretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.password.expiration}")
    private long resetPasswordExpiration;

    private final AccountRepository accountRepository;

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(accessSecretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSub(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }
    public String extractRole(String jwt) {
        return extractClaim(jwt, claims -> claims.get("role")).toString();
    }

    public String generateAccessToken(Account account) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole().getRole());
        return buildToken(claims, account.getUsername(), jwtExpiration, accessSecretKey);
    }

    public String generateResetPasswordToken(String email) {
        return buildToken(new HashMap<>(), email, resetPasswordExpiration, passwordSecretKey);
    }

    public VerificationTokenResult verifyResetPasswordToken(String requestToken) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(passwordSecretKey))
                    .build()
                    .parseClaimsJws(requestToken)
                    .getBody();
        } catch (Exception e) {
            return new VerificationTokenResult(false, null);
        }
        var email = claims.getSubject();
        var expiration = claims.getExpiration();
        var isTokenExpired = expiration.before(new Date());

        if (isTokenExpired)
            return new VerificationTokenResult(false, email);

        var userToken = accountRepository.findByEmail(email)
                .map(Account::getResetPasswordToken)
                .orElse(null);

        if (userToken == null || !userToken.equals(requestToken))
            return new VerificationTokenResult(false, email);

        return new VerificationTokenResult(true, email);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String subject,
            long expiration,
            String secret
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }
}
