package ru.aivar.webpdfapp.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTUserDetailsDto {

    private String email;
    private List<String> roles;

}
