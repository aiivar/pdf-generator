package ru.aivar.webpdfapp.utils;

import ru.aivar.webpdfapp.models.RefreshToken;
import ru.aivar.webpdfapp.models.User;

public interface RefreshTokenUtil {

    RefreshToken generateRefreshToken(User user);

}
