package com.sistemabankcoxinha.patterns.decorator;

import com.sistemabankcoxinha.patterns.factory.Coxinha;

public abstract class CoxinhaDecorator implements Coxinha {
    protected final Coxinha coxinhaBase;

    public CoxinhaDecorator(Coxinha coxinhaBase) {
        this.coxinhaBase = coxinhaBase;
    }

    @Override
    public String getSabor() {
        return coxinhaBase.getSabor();
    }

    @Override
    public Double getPreco() {
        return coxinhaBase.getPreco();
    }
}
