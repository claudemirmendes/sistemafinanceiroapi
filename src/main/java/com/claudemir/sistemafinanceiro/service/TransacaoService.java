package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.dto.FiltroTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.model.User;
import com.claudemir.sistemafinanceiro.repository.TransacaoRepository;
import com.claudemir.sistemafinanceiro.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
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


    public TransacaoService(TransacaoRepository transacaoRepository,
                            UserRepository userRepository,
                            EntityManager entityManager) {
        this.transacaoRepository = transacaoRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
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
    public List<Transacao> filtrar(FiltroTransacaoRequest filtro, Authentication authentication) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transacao> query = cb.createQuery(Transacao.class);
        Root<Transacao> root = query.from(Transacao.class);

        List<Predicate> predicates = new ArrayList<>();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        // Sempre filtrar por usuário
        predicates.add(cb.equal(root.get("usuario").get("id"), user.getId()));

        if (filtro.getTipo() != null) {
            predicates.add(cb.equal(root.get("tipo"), filtro.getTipo()));
        }

        if (filtro.getValorMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("valor"), filtro.getValorMin()));
        }

        if (filtro.getValorMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("valor"), filtro.getValorMax()));
        }

        // Filtros de DESPESA
        if (filtro.getPaga() != null) {
            predicates.add(cb.equal(root.get("paga"), filtro.getPaga()));
        }

        if (filtro.getDataVencimentoInicio() != null && filtro.getDataVencimentoFim() != null) {
            predicates.add(cb.between(root.get("dataVencimento"),
                    filtro.getDataVencimentoInicio(), filtro.getDataVencimentoFim()));
        }

        if (filtro.getDataPagamentoInicio() != null && filtro.getDataPagamentoFim() != null) {
            predicates.add(cb.between(root.get("dataPagamento"),
                    filtro.getDataPagamentoInicio(), filtro.getDataPagamentoFim()));
        }

        // Filtros de RECEITA
        if (filtro.getConfirmada() != null) {
            predicates.add(cb.equal(root.get("confirmada"), filtro.getConfirmada()));
        }

        if (filtro.getDataPrevistaInicio() != null && filtro.getDataPrevistaFim() != null) {
            predicates.add(cb.between(root.get("dataPrevistaRecebimento"),
                    filtro.getDataPrevistaInicio(), filtro.getDataPrevistaFim()));
        }

        if (filtro.getDataRecebimentoInicio() != null && filtro.getDataRecebimentoFim() != null) {
            predicates.add(cb.between(root.get("dataRecebimento"),
                    filtro.getDataRecebimentoInicio(), filtro.getDataRecebimentoFim()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

}
