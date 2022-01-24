package ru.aivar.webpdfapp.redis.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import ru.aivar.webpdfapp.redis.model.RedisUser;

public interface RedisUserRepository extends KeyValueRepository<RedisUser, String> {



}
