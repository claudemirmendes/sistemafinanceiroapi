package com.claudemir.sistemafinanceiro.controller;

import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid TransacaoRequest request, Authentication authentication) {
        if (request.getTipo() == TipoTransacao.RECEITA) {
            if (request.getDataPrevistaRecebimento() == null) {
                return ResponseEntity.badRequest().body("Campos de receita são obrigatórios.");
            }
        } else if (request.getTipo() == TipoTransacao.DESPESA) {
            if (request.getDataVencimento() == null || request.getPaga() == null) {
                return ResponseEntity.badRequest().body("Campos de despesa são obrigatórios.");
            }
        }

        return transacaoService.salvarComUsuario(request, authentication);
    }
}
