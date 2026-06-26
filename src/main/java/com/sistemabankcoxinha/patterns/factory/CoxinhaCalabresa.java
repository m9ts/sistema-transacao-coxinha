package com.sistemabankcoxinha.patterns.factory;

public class CoxinhaCalabresa implements Coxinha {
    @Override
    public String getSabor() {
        return "Calabresa";
    }

    @Override
    public Double getPreco() {
        return 12.0;
    }
}
