package com.claudemir.sistemafinanceiro.dto;

import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoRequest {
    private TipoTransacao tipo;
    private BigDecimal valor;
    private String descricao;

    // Receita
    private LocalDate dataPrevistaRecebimento;
    private LocalDate dataRecebida;
    private Boolean confirmada;

    // Despesa
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Boolean paga;
}
