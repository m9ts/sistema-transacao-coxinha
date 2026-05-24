package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaCarne implements Coxinha {
    @Override
    public String getSabor() {
        return "Carne";
    }

    @Override
    public Double getPreco() {
        return 9.0;
    }
}
