package com.claudemir.sistemafinanceiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ano;
    private Integer mes;

    private BigDecimal totalReceitas = BigDecimal.ZERO;
    private BigDecimal totalDespesas = BigDecimal.ZERO;


    public BigDecimal getSaldoLiquido() {
        return totalReceitas.subtract(totalDespesas);
    }
}
