package com.sistemabankcoxinha.patterns.factory;

import com.sistemabankcoxinha.patterns.decorator.CoxinhaRecheioDecorator;
import com.sistemabankcoxinha.patterns.decorator.DescontoDecorator;

public class CoxinhaFactory {

    public static Coxinha criarCoxinha(String sabor) {

        switch (sabor.toLowerCase()) {
            case "frango":
                return new CoxinhaFrango();
            case "costela":
                return new CoxinhaCostela();
            case "carne":
                return new CoxinhaCarne();
            case "frango especial":
                return new CoxinhaRecheioDecorator(new CoxinhaFrango());
            case "carne especial":
                return new DescontoDecorator((new CoxinhaCarne()), 20);
            default:
                throw new IllegalArgumentException("Sabor inválido!");
        }
    }
}
