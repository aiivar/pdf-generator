package ru.aivar.webpdfapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aivar.webpdfapp.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long id);

    Optional<RefreshToken> findByToken(String token);

}
