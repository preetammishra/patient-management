package com.pm.authservice.dto;

/**
 * @author User
 * @since 10-11-2026
 */


public class LoginResponseDto {

    private final String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}