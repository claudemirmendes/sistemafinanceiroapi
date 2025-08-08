package com.claudemir.sistemafinanceiro.mapper;

import com.claudemir.sistemafinanceiro.dto.TransacaoRequest;
import com.claudemir.sistemafinanceiro.model.Transacao;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    // Cria uma nova transação a partir do request
    Transacao toEntity(TransacaoRequest request);

    // Atualiza campos de uma transação existente com valores do request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTransacaoFromRequest(TransacaoRequest request, @MappingTarget Transacao entity);
}
