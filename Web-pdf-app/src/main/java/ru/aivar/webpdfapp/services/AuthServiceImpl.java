package ru.aivar.webpdfapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.dto.request.SignInRequest;
import ru.aivar.webpdfapp.dto.request.SignUpRequest;
import ru.aivar.webpdfapp.dto.response.JwtResponse;
import ru.aivar.webpdfapp.dto.response.MessageResponse;
import ru.aivar.webpdfapp.models.ERole;
import ru.aivar.webpdfapp.models.RefreshToken;
import ru.aivar.webpdfapp.models.Role;
import ru.aivar.webpdfapp.models.User;
import ru.aivar.webpdfapp.redis.service.RedisUserService;
import ru.aivar.webpdfapp.repositories.RefreshTokenRepository;
import ru.aivar.webpdfapp.repositories.RoleRepository;
import ru.aivar.webpdfapp.repositories.UserRepository;
import ru.aivar.webpdfapp.utils.JwtUtils;
import ru.aivar.webpdfapp.utils.RefreshTokenUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenUtil refreshTokenUtil;

    @Autowired
    private RedisUserService redisUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User was not found")
        );
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getHashPassword())) {
            throw new UsernameNotFoundException("Authentication was failed");
        }
        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
        RefreshToken newRefreshToken = refreshTokenUtil.generateRefreshToken(user);
        refreshTokenRepository.save(newRefreshToken);
        String jwt = jwtUtils.generateJwt(user);
        redisUserService.addTokenToUser(user, newRefreshToken.getToken());
        redisUserService.addTokenToUser(user, jwt);
        return JwtResponse.builder()
                .refreshToken(newRefreshToken.getToken())
                .email(signInRequest.getEmail())
                .token(jwt)
                .build();
    }

    @Override
    public MessageResponse signUp(SignUpRequest signUpRequest) {
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(IllegalArgumentException::new);
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .hashPassword(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Collections.singleton(role))
                .build();

        User registeredUser = userRepository.save(user);
        log.info("IN AuthServiceImpl::signUp - user {} was successfully registered", registeredUser);
        return MessageResponse.builder()
                .message("user was successfully registered")
                .build();
    }

    @Override
    public JwtResponse refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr).orElseThrow(
                () -> new UsernameNotFoundException("Token was not found")
        );
        User user = userRepository.findByRefreshTokenId(refreshToken.getId()).orElseThrow(
                () -> new UsernameNotFoundException("User was not found")
        );
        refreshTokenRepository.delete(refreshToken);
        RefreshToken newRefreshToken = refreshTokenUtil.generateRefreshToken(user);
        refreshTokenRepository.save(newRefreshToken);
        String jwt = jwtUtils.generateJwt(user);
        redisUserService.addTokenToUser(user, newRefreshToken.getToken());
        redisUserService.addTokenToUser(user, jwt);
        return JwtResponse.builder()
                .refreshToken(newRefreshToken.getToken())
                .email(user.getEmail())
                .token(jwt)
                .build();
    }
}
