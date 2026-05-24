package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaFactory {

    public static Coxinha criarCoxinha(String sabor) {

        switch (sabor.toLowerCase()) {
            case "frango":
                return new CoxinhaFrango();
            case "costela":
                return new CoxinhaCostela();
            case "carne":
                return new CoxinhaCarne();
            default:
                throw new IllegalArgumentException("Sabor inválido!");
        }
    }
}
