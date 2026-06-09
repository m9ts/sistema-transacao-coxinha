package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaFrango implements Coxinha{
    @Override
    public String getSabor() {
        return "Frango";
    }

    @Override
    public Double getPreco() {
        return 8.0;
    }
}
