package com.claudemir.sistemafinanceiro.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // Ex: ROLE_USER

    // Implementação da interface UserDetails:

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // pode usar lógica aqui se quiser desabilitar usuário por expiração
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // pode usar lógica para usuários bloqueados
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // pode usar lógica para senhas expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // pode usar lógica para ativação de conta
    }
}
