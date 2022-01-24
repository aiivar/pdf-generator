package ru.aivar.webpdfapp.redis.service;

import ru.aivar.webpdfapp.models.User;

public interface RedisUserService {
    void addTokenToUser(User user, String token);

    void addAllTokensToBlacklist(User user);
}
