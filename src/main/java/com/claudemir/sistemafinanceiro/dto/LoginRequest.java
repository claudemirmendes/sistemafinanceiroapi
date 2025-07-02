package com.claudemir.sistemafinanceiro.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false) // rejeita campos extras no JSON
public class LoginRequest {
    private String username;
    private String password;
}
