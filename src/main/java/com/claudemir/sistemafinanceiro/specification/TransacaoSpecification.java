package com.claudemir.sistemafinanceiro.specification;
import com.claudemir.sistemafinanceiro.model.TipoTransacao;
import com.claudemir.sistemafinanceiro.model.Transacao;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
public class TransacaoSpecification {
    public static Specification<Transacao> doUsuario(Long id) {
        // Corrigido: acessa o campo "usuario" (objeto) e seu atributo "id"
        return (root, query, cb) -> cb.equal(root.get("usuario").get("id"), id);
    }

    public static Specification<Transacao> doTipo(TipoTransacao tipo) {
        // Correto: campo "tipo" é direto na entidade Transacao
        return (root, query, cb) -> cb.equal(root.get("tipo"), tipo);
    }


    public static Specification<Transacao> valorMaiorQue(BigDecimal valorMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("valor"), valorMin);
    }

    public static Specification<Transacao> valorMenorQue(BigDecimal valorMax) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("valor"), valorMax);
    }

    public static Specification<Transacao> dataVencimentoEntre(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> cb.between(root.get("data_vencimento"), inicio, fim);
    }
    public static Specification<Transacao> dataPagamentoEntre(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> cb.between(root.get("data_pagamento"), inicio, fim);
    }

    public static Specification<Transacao> dataPrevistaEntre(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> cb.between(root.get("data_prevista_recebimento"), inicio, fim);
    }

    public static Specification<Transacao> dataRecebimentoEntre(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> cb.between(root.get("data_prevista_recebimento"), inicio, fim);
    }

    public static Specification<Transacao> ePaga(Boolean paga){
        return   (root, query, cb) -> cb.equal(root.get("paga"), paga);
    }
    public static Specification<Transacao> eConfirmada(Boolean confirmada){
        return   (root, query, cb) -> cb.equal(root.get("confirmada"), confirmada);
    }

}
