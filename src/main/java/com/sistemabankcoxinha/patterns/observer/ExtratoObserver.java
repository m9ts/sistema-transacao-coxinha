package com.sistemabankcoxinha.patterns.observer;

import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.stereotype.Component;

@Component
public class ExtratoObserver implements CompraObserver {

    @Override
    public void onCompraaRealizada(Movimentacao movimentacao) {
        System.out.println("[EXTRATO] Nova movimentação: "
                + movimentacao.getSabor() + " | R$ "
                + movimentacao.getValor() + " | "
                + movimentacao.getDataHora());
    }
}
