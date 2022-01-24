package ru.aivar.webpdfapp.services;

import ru.aivar.webpdfapp.dto.request.SignInRequest;
import ru.aivar.webpdfapp.dto.request.SignUpRequest;
import ru.aivar.webpdfapp.dto.response.JwtResponse;
import ru.aivar.webpdfapp.dto.response.MessageResponse;

public interface AuthService {
    JwtResponse signIn(SignInRequest signInRequest);

    MessageResponse signUp(SignUpRequest signUpRequest);

    JwtResponse refresh(String refreshToken);
}

