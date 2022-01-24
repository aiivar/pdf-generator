package ru.aivar.webpdfapp.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aivar.webpdfapp.models.User;
import ru.aivar.webpdfapp.security.jwt.JwtAuthenticationException;
import ru.aivar.webpdfapp.security.jwt.dto.JWTUserDetailsDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtilsImpl implements JwtUtils {

    @Value("${app.jwt.expiration.min}")
    private Integer expiresInMinutes;
    @Value("${app.jwt.secret}")
    private String secret;

    @Override
    public String generateJwt(User user) {
        return Jwts.builder()
                .setIssuer("Aivar")
                .setSubject("rest-api")
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(LocalDateTime.now().plusMinutes(expiresInMinutes).atZone(ZoneId.systemDefault()).toInstant())
                )
                .signWith(
                        SignatureAlgorithm.HS256,
                        TextCodec.BASE64.decode(secret)
                )
                .compact();
    }

    @Override
    public JWTUserDetailsDto parseUserDetails(String jwt) {
        if (verifyToken(jwt)){
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
            String email = claims.getBody().get("email", String.class);
            List<String> roles = (List<String>) claims.getBody().get("roles");
            return JWTUserDetailsDto.builder()
                    .email(email)
                    .roles(roles)
                    .build();
        }
        throw new JwtAuthenticationException("JWT is expired or invalid");
    }

    @Override
    public boolean verifyToken(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT is expired or invalid");
        }
    }
}
