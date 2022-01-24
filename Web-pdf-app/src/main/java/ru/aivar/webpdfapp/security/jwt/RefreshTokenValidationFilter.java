package ru.aivar.webpdfapp.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.aivar.webpdfapp.models.RefreshToken;
import ru.aivar.webpdfapp.redis.service.TokensBlacklistService;
import ru.aivar.webpdfapp.repositories.RefreshTokenRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RefreshTokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private TokensBlacklistService tokensBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String refreshTokenParam = httpServletRequest.getParameter("refreshToken");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenParam).orElse(null);
        if (refreshToken != null){
            if (tokensBlacklistService.exists(refreshToken.getToken())){
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (refreshToken.getExpires().isBefore(LocalDateTime.now())) {
                refreshTokenRepository.delete(refreshToken);
                tokensBlacklistService.delete(refreshToken.getToken());
                throw new UsernameNotFoundException("Token was expired");
            } else {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
