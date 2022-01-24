package ru.aivar.webpdfapp.repositories;

public interface BlacklistRepository {
    void save(String token);

    boolean exists(String token);

    void delete(String token);
}
