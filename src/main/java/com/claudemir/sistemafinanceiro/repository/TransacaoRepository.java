package com.claudemir.sistemafinanceiro.repository;

import com.claudemir.sistemafinanceiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
