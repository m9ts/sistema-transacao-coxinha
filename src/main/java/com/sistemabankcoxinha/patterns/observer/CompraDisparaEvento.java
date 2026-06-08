package com.sistemabankcoxinha.patterns.observer;

import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompraDisparaEvento {
    private final List<CompraObserver> observers;

    // injeta os todos os @Component que implementam CompraObserrver
    public CompraDisparaEvento(List<CompraObserver> observers) {
        this.observers = observers;
    }

    public void publicar(Movimentacao movimentacao) {
        for (CompraObserver observer: observers) {
            observer.onCompraaRealizada(movimentacao);
        }
    }
}
