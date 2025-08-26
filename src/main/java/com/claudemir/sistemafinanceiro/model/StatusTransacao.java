package com.claudemir.sistemafinanceiro.model;

public enum StatusTransacao {
    CANCELADA,
    ATIVA;

    public boolean isCancelada() {
        return this == CANCELADA;
    }

    public boolean isAtiva() {
        return this == ATIVA;
    }
}
