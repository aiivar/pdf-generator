package ru.aivar.webpdfapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.models.User;
import ru.aivar.webpdfapp.redis.service.RedisUserService;
import ru.aivar.webpdfapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUserService redisUserService;

    @Override
    public void block(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        redisUserService.addAllTokensToBlacklist(user);
    }

}
