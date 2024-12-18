package edu.martynov.rental_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDto {
    private String token;
    private String role;

    public static AuthResponseDto create(String token, String role) {
        return new AuthResponseDto(token, role);
    }
}
