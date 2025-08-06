
package com.claudemir.sistemafinanceiro.dto;

import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConfirmarTransacaoRequest {
    private LocalDate dataRecebimento;
    private LocalDate dataPagamento;
}
