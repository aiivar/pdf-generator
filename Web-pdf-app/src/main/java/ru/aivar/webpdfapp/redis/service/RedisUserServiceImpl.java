package ru.aivar.webpdfapp.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.models.User;
import ru.aivar.webpdfapp.redis.model.RedisUser;
import ru.aivar.webpdfapp.redis.repository.RedisUserRepository;
import ru.aivar.webpdfapp.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RedisUserServiceImpl implements RedisUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokensBlacklistService blacklistService;
    @Autowired
    private RedisUserRepository redisUserRepository;

    @Override
    public void addTokenToUser(User user, String token) {
        String redisId = user.getRedisId();
        RedisUser redisUser;
        if (redisId != null){
            redisUser = redisUserRepository.findById(redisId).orElseThrow(IllegalArgumentException::new);
            if (redisUser.getTokens() == null){
                redisUser.setTokens(new ArrayList<>());
            }
            redisUser.getTokens().add(token);
        }else {
            redisUser = RedisUser.builder()
                    .userId(user.getId())
                    .tokens(Collections.singletonList(token))
                    .build();
        }
        redisUserRepository.save(redisUser);
        user.setRedisId(redisUser.getId());
        userRepository.save(user);
    }

    @Override
    public void addAllTokensToBlacklist(User user) {
        if (user.getRedisId() != null){
            RedisUser redisUser = redisUserRepository.findById(user.getRedisId()).orElseThrow(IllegalArgumentException::new);
            List<String> tokens = redisUser.getTokens();
            tokens.forEach(token -> blacklistService.add(token));
            redisUser.getTokens().clear();
            redisUserRepository.save(redisUser);
        }
    }
}
