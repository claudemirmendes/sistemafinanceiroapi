package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.dto.FiltroTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.mapper.TransacaoMapper;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.TransacaoRepository;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import com.claudemir.sistemafinanceiro.specification.TransacaoSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.claudemir.sistemafinanceiro.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UserRepository userRepository;
    private EntityManager entityManager;
    private User usuario;
    private final TransacaoMapper mapper;


    public TransacaoService(TransacaoRepository transacaoRepository,
                            UserRepository userRepository,
                            EntityManager entityManager,
                            TransacaoMapper mapper) {
        this.transacaoRepository = transacaoRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
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
    public List<Transacao> filtrar(FiltroTransacaoRequest filtro, Authentication authentication) {;

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        Specification<Transacao> spec = Specification.where(TransacaoSpecification.doUsuario(user.getId()));

        if (filtro.getTipo() != null) {
            spec = spec.and(TransacaoSpecification.doTipo(filtro.getTipo()));
        }

        if (filtro.getValorMin() != null) {
           spec = spec.and(TransacaoSpecification.valorMenorQue(filtro.getValorMin()));
        }

        if (filtro.getValorMax() != null) {
           spec = spec.and(TransacaoSpecification.valorMaiorQue(filtro.getValorMax()));
        }
        if (filtro.getDescricao() != null) {
            spec = spec.and(TransacaoSpecification.descricao(filtro.getDescricao()));
        }

        // Filtros de DESPESA
        if (filtro.getPaga() != null) {
            spec = spec.and(TransacaoSpecification.ePaga(filtro.getPaga()));
        }

        if (filtro.getDataVencimentoInicio() != null && filtro.getDataVencimentoFim() != null) {
            spec = spec.and(TransacaoSpecification.dataVencimentoEntre(filtro.getDataVencimentoInicio(), filtro.getDataVencimentoFim()));
        }

        if (filtro.getDataPagamentoInicio() != null && filtro.getDataPagamentoFim() != null) {
            spec = spec.and(TransacaoSpecification.dataPagamentoEntre(filtro.getDataPagamentoInicio(), filtro.getDataPagamentoFim()));
        }

        // Filtros de RECEITA criar o spe
        if (filtro.getConfirmada() != null) {
            spec = spec.and(TransacaoSpecification.eConfirmada(filtro.getConfirmada()));
        }

        if (filtro.getDataPrevistaInicio() != null && filtro.getDataPrevistaFim() != null) {
            spec = spec.and(TransacaoSpecification.dataPrevistaEntre(filtro.getDataPrevistaInicio(), filtro.getDataPrevistaFim()));
        }

        if (filtro.getDataRecebimentoInicio() != null && filtro.getDataRecebimentoFim() != null) {
            spec = spec.and(TransacaoSpecification.dataRecebimentoEntre(filtro.getDataRecebimentoInicio(), filtro.getDataRecebimentoFim()));
        }

        return transacaoRepository.findAll(spec);
    }

    @Transactional
    public void atualizarTransacao(Long id, TransacaoRequest request) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));


        if (request.getTipo() != null){
            throw new IllegalStateException("Não é possivel alterar o tipo da transação");
        };
        mapper.updateTransacaoFromRequest(request, transacao);

        transacaoRepository.save(transacao);
    }

}
