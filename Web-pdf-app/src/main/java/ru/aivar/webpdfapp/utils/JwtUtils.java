package ru.aivar.webpdfapp.utils;

import ru.aivar.webpdfapp.models.User;
import ru.aivar.webpdfapp.security.jwt.dto.JWTUserDetailsDto;

public interface JwtUtils {

    String generateJwt(User user);

    JWTUserDetailsDto parseUserDetails(String jwt);

    boolean verifyToken(String jwt);

}
