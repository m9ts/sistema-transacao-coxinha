package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaPalmito implements Coxinha {
    @Override
    public String getSabor() {
        return "Palmito";
    }

    @Override
    public Double getPreco() {
        return 15.0;
    }
}
