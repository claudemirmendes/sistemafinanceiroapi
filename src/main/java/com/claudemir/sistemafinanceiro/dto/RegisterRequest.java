package com.claudemir.sistemafinanceiro.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String accessKey;

    // Getters e setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
}
