package com.claudemir.sistemafinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(nullable = false, precision = 10, scale = 2)
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

    @ManyToOne
    @JsonIgnore
    private User usuario;
}
