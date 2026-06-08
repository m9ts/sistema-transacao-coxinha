package com.sistemabankcoxinha.patterns.observer;

import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.stereotype.Component;

@Component
public class EstoqueObserver implements CompraObserver {

    @Override
    public void onCompraaRealizada(Movimentacao movimentacao) {
        System.out.println("[ESTOQUE] Compra de "
        + movimentacao.getSabor()
        + " processada. Verifique slots de troco.");
    }
}
