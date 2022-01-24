package ru.aivar.webpdfapp.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.repositories.BlacklistRepository;

@Service
public class TokensBlacklistServiceImpl implements TokensBlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Override
    public void add(String token) {
        blacklistRepository.save(token);
    }

    @Override
    public boolean exists(String token) {
        return blacklistRepository.exists(token);
    }

    @Override
    public void delete(String token) {
        blacklistRepository.delete(token);
    }
}
