package com.sistemabankcoxinha.patterns.observer;

import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.stereotype.Component;

@Component
public class LogObserver implements CompraObserver {
    @Override
    public void onCompraaRealizada(Movimentacao movimentacao) {
        System.out.println("[LOG] Evento de compra: cliente_id="
        + movimentacao.getCliente().getId()
        + " sabor=" + movimentacao.getSabor()
        + " valor=" + movimentacao.getValor());
    }
}
