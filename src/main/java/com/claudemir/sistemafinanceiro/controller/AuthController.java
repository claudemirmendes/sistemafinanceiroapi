package com.claudemir.sistemafinanceiro.controller;

import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        boolean exists = userRepository.findByUsername(user.getUsername()).isPresent();
        if (exists) {
            return "Nome de usuário já está em uso.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // criptografa a senha
        user.setRole("ROLE_USER"); // define a role padrão
        userRepository.save(user); // salva no banco

        return "Usuário registrado com sucesso!";
    }
}
