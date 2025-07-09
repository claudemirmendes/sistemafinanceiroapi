package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.TransacaoRepository;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UserRepository userRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, UserRepository userRepository) {
        this.transacaoRepository = transacaoRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> salvarComUsuario(TransacaoRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Transacao transacao = new Transacao();
        transacao.setTipo(request.getTipo());
        transacao.setValor(request.getValor());
        transacao.setDescricao(request.getDescricao());
        transacao.setUsuario(user);

        if (request.getTipo().isReceita()) {
            transacao.setDataPrevistaRecebimento(request.getDataPrevistaRecebimento());
            transacao.setConfirmada(request.getConfirmada());
        } else {
            transacao.setDataVencimento(request.getDataVencimento());
            transacao.setPaga(request.getPaga());
        }

        transacaoRepository.save(transacao);
        return ResponseEntity.ok(transacao);
    }

    @Transactional
    public void confirmarPagamento(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        if (transacao.getTipo() != TipoTransacao.DESPESA) {
            throw new IllegalStateException("Apenas despesas podem ser confirmadas como pagas.");
        }

        if (Boolean.TRUE.equals(transacao.getPaga())) {
            throw new IllegalStateException("Essa despesa já está marcada como paga.");
        }

        transacao.setPaga(true);
        transacao.setDataPagamento(LocalDate.now());

        transacaoRepository.save(transacao);
    }

    @Transactional
    public void confirmarRecebimento(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Transacao Não encontrada"));
        if (transacao.getTipo() != TipoTransacao.RECEITA){
            throw new IllegalStateException("Apenas Receitas podem confirmar Pagamento");
        }
        if (Boolean.TRUE.equals(transacao.getConfirmada())){
            throw new IllegalStateException("Esta Receita já foi confirmada");
        }
        transacao.setConfirmada(true);
        transacao.setDataRecebida(LocalDate.now());

        transacaoRepository.save(transacao);
    }
}
