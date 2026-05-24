package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaCostela implements Coxinha {
    @Override
    public String getSabor() {
        return "Costela";
    }

    @Override
    public Double getPreco() {
        return 10.0;
    }
}
