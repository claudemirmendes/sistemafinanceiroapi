package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.TransacaoRepository;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
}
