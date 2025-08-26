package com.claudemir.sistemafinanceiro.service;

import com.claudemir.sistemafinanceiro.dto.ConfirmarTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.FiltroTransacaoRequest;
import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.mapper.TransacaoMapper;
import com.claudemir.sistemafinanceiro.model.*;
import com.claudemir.sistemafinanceiro.repository.BalanceRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.claudemir.sistemafinanceiro.factory.TransacaoFactory;



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
    private final BalanceRepository balanceRepository;

    @Autowired
    private BalanceService balanceService;


    public TransacaoService(TransacaoRepository transacaoRepository,
                            UserRepository userRepository,
                            EntityManager entityManager,
                            TransacaoMapper mapper, BalanceRepository balanceRepository, BalanceService balanceService) {
        this.transacaoRepository = transacaoRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
        this.balanceRepository = balanceRepository;
        this.balanceService = balanceService;
    }

    public ResponseEntity<?> salvarComUsuario(TransacaoRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();




        Transacao  transacao = TransacaoFactory.criarTransacao(request, user);
        transacaoRepository.save(transacao);
        return ResponseEntity.ok(transacao);
    }

    @Transactional
    public void confirmarPagamento(Long id, ConfirmarTransacaoRequest confirmarTransacaoRequest, Authentication authentication) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        String username = authentication.getName();
                User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));

        if (transacao.getTipo() != TipoTransacao.DESPESA) {
            throw new IllegalStateException("Apenas despesas podem ser confirmadas como pagas.");
        }

        if (Boolean.TRUE.equals(transacao.getPaga())) {
            throw new IllegalStateException("Essa despesa já está marcada como paga.");
        }
        if (StatusTransacao.CANCELADA.equals(transacao.getStatus())) {
            throw new IllegalStateException("Essa transação está cancelada e não pode ser paga.");
        }
        transacao.setPaga(true);
        if (user.getId() != transacao.getUsuario().getId()) {
            throw new IllegalStateException("Usuário não autorizado a confirmar pagamento desta transação.");
        }
        if(confirmarTransacaoRequest.getDataPagamento() != null) {
            transacao.setDataPagamento(confirmarTransacaoRequest.getDataPagamento());
        }
       


       
        Balance balance = balanceService.obterOuCriarBalance(confirmarTransacaoRequest.getDataPagamento());
        balance.setTotalDespesas(balance.getTotalDespesas().add(transacao.getValor()));
        balanceRepository.save(balance);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void confirmarRecebimento(Long id, ConfirmarTransacaoRequest confirmarTransacaoRequest, Authentication authentication) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Transacao Não encontrada"));

            String username = authentication.getName();
                User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        if (user.getId() != transacao.getUsuario().getId()) {
            throw new IllegalStateException("Usuário não autorizado a confirmar recebimento desta transação.");
        }
        if (transacao.getTipo() != TipoTransacao.RECEITA){
            throw new IllegalStateException("Apenas Receitas podem confirmar Pagamento");
        }
        if (Boolean.TRUE.equals(transacao.getConfirmada())){
            throw new IllegalStateException("Esta Receita já foi confirmada");
        }
        if (StatusTransacao.CANCELADA.equals(transacao.getStatus())) {
            throw new IllegalStateException("Essa transação está cancelada e não pode ser recebida.");
        }
        if(confirmarTransacaoRequest.getDataRecebimento() != null) {
            transacao.setDataRecebida(confirmarTransacaoRequest.getDataRecebimento());
        }
        transacao.setConfirmada(true);


        Balance balance = balanceService.obterOuCriarBalance(confirmarTransacaoRequest.getDataRecebimento());
        balance.setTotalReceitas(balance.getTotalReceitas().add(transacao.getValor()));
        balanceRepository.save(balance);
        transacaoRepository.save(transacao);
    }
    public List<Transacao> filtrar(FiltroTransacaoRequest filtro, Authentication authentication) {;

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        Specification<Transacao> spec = Specification.where(TransacaoSpecification.doUsuario(user.getId()));

        spec = spec.and(TransacaoSpecification.naoCanceladas(StatusTransacao.CANCELADA));
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
    public Transacao  atualizarTransacao(Long id, TransacaoRequest request, Authentication authentication) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));


        if (request.getTipo() != null){
            throw new IllegalStateException("Não é possivel alterar o tipo da transação");
        };

            String username = authentication.getName();
                User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        if (user.getId() != transacao.getUsuario().getId()) {
            throw new IllegalStateException("Usuário não autorizado a alterar esta transação.");
        }
        mapper.updateTransacaoFromRequest(request, transacao);

        transacaoRepository.save(transacao);
        return transacao;
    }

    public void deletarPagamento(Long id, Authentication authentication) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
            String username = authentication.getName();
                User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new EntityNotFoundException("Usuário Não encontrado"));
        if (user.getId() != transacao.getUsuario().getId()) {
            throw new IllegalStateException("Usuário não autorizado a deletar esta transação.");
        }
        
        // Atualiza o balance se necessário
        if (transacao.getTipo() == TipoTransacao.DESPESA && transacao.getDataPagamento() != null) {
            Balance balance = balanceService.obterOuCriarBalance(transacao.getDataPagamento());
            balance.setTotalDespesas(balance.getTotalDespesas().subtract(transacao.getValor()));
            balanceRepository.save(balance);
        }
        if (transacao.getTipo() == TipoTransacao.RECEITA && transacao.getDataRecebida() != null) {
            Balance balance = balanceService.obterOuCriarBalance(transacao.getDataRecebida());
           balance.setTotalReceitas(balance.getTotalReceitas().subtract(transacao.getValor()));

            balanceRepository.save(balance);
        }
       
    transacao.setStatus(StatusTransacao.CANCELADA);
    transacaoRepository.save(transacao);
    }

}
