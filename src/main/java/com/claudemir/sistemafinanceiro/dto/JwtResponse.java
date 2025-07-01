package com.claudemir.sistemafinanceiro.dto;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private String message;

    public JwtResponse(String token, String refreshToken, String message) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    // Getters e setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
