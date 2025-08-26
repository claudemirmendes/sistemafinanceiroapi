package com.claudemir.sistemafinanceiro.controller;

import com.claudemir.sistemafinanceiro.dto.ConfirmarTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.FiltroTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.LoginRequest;
import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import com.claudemir.sistemafinanceiro.service.TransacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
public class


TransacaoController {

    private final TransacaoService transacaoService;
    private  final UserRepository userRepository;

    public TransacaoController(TransacaoService transacaoService,UserRepository userRepository) {
        this.transacaoService = transacaoService;
        this.userRepository = userRepository;
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

    @PutMapping("/{id}/confirmar-pagamento")
    public ResponseEntity<?> confirmarPagamento(@PathVariable("id") Long id, @RequestBody ConfirmarTransacaoRequest confirmarTransacaoRequest, Authentication authentication) {
        try {
            transacaoService.confirmarPagamento(id, confirmarTransacaoRequest, authentication);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Pagamento confirmado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}/delete_transacao")
     public ResponseEntity<?> deletarPagamento(@PathVariable("id") Long id, Authentication authentication) {
        try {
            transacaoService.deletarPagamento(id, authentication);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Transacao deletada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}/confirmar-recebimento")
    public ResponseEntity<?> confirmarRecebimento(@PathVariable("id") Long id , @RequestBody ConfirmarTransacaoRequest confirmarTransacaoRequest,Authentication authentication) {
        try {
            transacaoService.confirmarRecebimento(id, confirmarTransacaoRequest,authentication);
            Map<String, String> resposta = new HashMap<>();


            resposta.put("mensagem", "recebimento confirmado");
            return ResponseEntity.ok(resposta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestBody FiltroTransacaoRequest filtro, Authentication authentication) {
        List<Transacao> resultado = transacaoService.filtrar(filtro, authentication);
        return ResponseEntity.ok(resultado);
    }

@PutMapping("/{id}/atualizar")
public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
                                   @RequestBody TransacaoRequest request,
                                   Authentication authentication) {
    try {
        Transacao transacaoAtualizada = transacaoService.atualizarTransacao(id, request, authentication);
        return ResponseEntity.ok(Map.of(
            "mensagem", "Transação atualizada com sucesso",
            "transacao", transacaoAtualizada
        ));
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("erro", "Transação não encontrada"));
    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest()
                             .body(Map.of("erro", e.getMessage()));
    }
}



}
