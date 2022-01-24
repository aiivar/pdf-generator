package ru.aivar.webpdfapp.redis.service;

public interface TokensBlacklistService {

    void add(String token);

    boolean exists(String token);

    void delete(String token);
}
