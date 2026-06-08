package com.sistemabankcoxinha.patterns.observer;

import com.sistemabankcoxinha.model.Movimentacao;

public interface CompraObserver {
    void onCompraaRealizada(Movimentacao movimentacao);
}
