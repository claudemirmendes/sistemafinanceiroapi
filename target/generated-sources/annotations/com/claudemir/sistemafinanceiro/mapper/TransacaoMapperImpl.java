package com.claudemir.sistemafinanceiro.mapper;

import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.Transacao;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-11T22:11:14-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class TransacaoMapperImpl implements TransacaoMapper {

    @Override
    public Transacao toEntity(TransacaoRequest request) {
        if ( request == null ) {
            return null;
        }

        Transacao transacao = new Transacao();

        transacao.setTipo( request.getTipo() );
        transacao.setValor( request.getValor() );
        transacao.setDescricao( request.getDescricao() );
        transacao.setDataPrevistaRecebimento( request.getDataPrevistaRecebimento() );
        transacao.setDataRecebida( request.getDataRecebida() );
        transacao.setConfirmada( request.getConfirmada() );
        transacao.setDataVencimento( request.getDataVencimento() );
        transacao.setDataPagamento( request.getDataPagamento() );
        transacao.setPaga( request.getPaga() );

        return transacao;
    }

    @Override
    public void updateTransacaoFromRequest(TransacaoRequest request, Transacao entity) {
        if ( request == null ) {
            return;
        }

        if ( request.getTipo() != null ) {
            entity.setTipo( request.getTipo() );
        }
        if ( request.getValor() != null ) {
            entity.setValor( request.getValor() );
        }
        if ( request.getDescricao() != null ) {
            entity.setDescricao( request.getDescricao() );
        }
        if ( request.getDataPrevistaRecebimento() != null ) {
            entity.setDataPrevistaRecebimento( request.getDataPrevistaRecebimento() );
        }
        if ( request.getDataRecebida() != null ) {
            entity.setDataRecebida( request.getDataRecebida() );
        }
        if ( request.getConfirmada() != null ) {
            entity.setConfirmada( request.getConfirmada() );
        }
        if ( request.getDataVencimento() != null ) {
            entity.setDataVencimento( request.getDataVencimento() );
        }
        if ( request.getDataPagamento() != null ) {
            entity.setDataPagamento( request.getDataPagamento() );
        }
        if ( request.getPaga() != null ) {
            entity.setPaga( request.getPaga() );
        }
    }
}
