package ru.aivar.generatorpdfapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aivar.generatorpdfapp.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
