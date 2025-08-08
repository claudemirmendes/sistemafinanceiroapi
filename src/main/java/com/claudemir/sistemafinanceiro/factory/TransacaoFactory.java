package com.claudemir.sistemafinanceiro.factory;
import com.claudemir.sistemafinanceiro.model.Transacao;
import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.User;


public class TransacaoFactory {

    public static Transacao criarTransacao(TransacaoRequest request, User user) {
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
        return transacao;
    }  

}
