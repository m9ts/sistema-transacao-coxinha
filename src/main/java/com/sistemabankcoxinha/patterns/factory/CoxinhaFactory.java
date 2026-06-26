package com.sistemabankcoxinha.patterns.factory;

import com.sistemabankcoxinha.patterns.decorator.CoxinhaRecheioDecorator;
import com.sistemabankcoxinha.patterns.decorator.DescontoDecorator;

public class CoxinhaFactory {
    public static Coxinha criarCoxinha(String sabor) {
        return criarCoxinha(sabor, false, false);
    }

    public static Coxinha criarCoxinha(String sabor, boolean adicionarRecheio, boolean aplicarDesconto) {
        Coxinha base;

        switch (sabor.toLowerCase()) {
            case "frango":
                base = new CoxinhaFrango();
                break;
            case "carne":
                base = new CoxinhaCarne();
                break;
            case "costela":
                base = new CoxinhaCostela();
                break;
            case "calabresa":
                base = new CoxinhaCalabresa();
                break;
            case "palmito":
                base = new CoxinhaPalmito();
                break;
            case "frango especial":
                base = new CoxinhaFrango();
                adicionarRecheio = true;
                break;
            case "carne especial":
                base = new CoxinhaCarne();
                aplicarDesconto = true;
                break;
            default:
                throw new IllegalArgumentException("Sabor inválido: " + sabor);
        }

        if (adicionarRecheio && "frango".equalsIgnoreCase(sabor)) {
            base = new CoxinhaRecheioDecorator(base);
        }

        if (aplicarDesconto && "carne".equalsIgnoreCase(sabor)) {
            base = new DescontoDecorator(base, 20);
        }

        return base;
    }
}