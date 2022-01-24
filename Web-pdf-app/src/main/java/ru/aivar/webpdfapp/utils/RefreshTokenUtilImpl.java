package ru.aivar.webpdfapp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aivar.webpdfapp.models.RefreshToken;
import ru.aivar.webpdfapp.models.User;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;


@Component
public class RefreshTokenUtilImpl implements RefreshTokenUtil {

    @Value("${app.refresh.token.expiration.days}")
    private Integer expiresInDays;

    @Override
    public RefreshToken generateRefreshToken(User user) {
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expires(LocalDateTime.now().plus(Period.ofDays(expiresInDays)))
                .user(user)
                .build();
    }
}
