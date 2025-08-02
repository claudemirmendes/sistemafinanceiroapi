package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.model.Balance;
import com.claudemir.sistemafinanceiro.repository.BalanceRepository;
import com.claudemir.sistemafinanceiro.repository.UserRepository;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }


    public Balance obterOuCriarBalance(LocalDate data) {
        int mes = data.getMonthValue();
        int ano = data.getYear();
        return balanceRepository
                .findByAnoAndMes(ano, mes)
                .orElseGet(() -> {
                    Balance novo = new Balance();
                    novo.setAno(ano);
                    novo.setMes(mes);
                    return novo;
                });
    }

}
