package com.sistemabankcoxinha.patterns.decorator;

import com.sistemabankcoxinha.patterns.factory.Coxinha;

public class CoxinhaRecheioDecorator extends CoxinhaDecorator {

    public CoxinhaRecheioDecorator(Coxinha coxinhaBase) {
        super(coxinhaBase);
    }

    public Double getPreco() {
        return coxinhaBase.getPreco() + 2.0;
    }

    public String getSabor() {
        return coxinhaBase.getSabor() + " Especial";
    }
}
