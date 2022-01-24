package ru.aivar.webpdfapp.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.aivar.webpdfapp.security.jwt.dto.JWTUserDetailsDto;
import ru.aivar.webpdfapp.utils.JwtUtils;

@Service("jwtDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {
        JWTUserDetailsDto detailsDto = jwtUtils.parseUserDetails(jwt);
        return new JWTUserDetailsImpl(
                detailsDto.getEmail(),
                detailsDto.getRoles()
        );
    }
}
