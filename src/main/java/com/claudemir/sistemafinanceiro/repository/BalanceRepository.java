package com.claudemir.sistemafinanceiro.repository;

import com.claudemir.sistemafinanceiro.model.Balance;
import com.claudemir.sistemafinanceiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long>, JpaSpecificationExecutor<Balance> {
    Optional<Balance> findByAnoAndMes(int ano, int mes);
}
