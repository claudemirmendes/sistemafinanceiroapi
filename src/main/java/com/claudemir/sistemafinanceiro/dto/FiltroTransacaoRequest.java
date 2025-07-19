package com.claudemir.sistemafinanceiro.dto;

import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FiltroTransacaoRequest {
    private TipoTransacao tipo;

    // Filtros comuns
    private BigDecimal valorMin;
    private BigDecimal valorMax;

    // Para DESPESA
    private Boolean paga;
    private LocalDate dataVencimentoInicio;
    private LocalDate dataVencimentoFim;
    private LocalDate dataPagamentoInicio;
    private LocalDate dataPagamentoFim;

    // Para RECEITA
    private Boolean confirmada;
    private LocalDate dataPrevistaInicio;
    private LocalDate dataPrevistaFim;
    private LocalDate dataRecebimentoInicio;
    private LocalDate dataRecebimentoFim;
}
