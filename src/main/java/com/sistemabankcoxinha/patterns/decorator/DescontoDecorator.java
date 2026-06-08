package com.sistemabankcoxinha.patterns.decorator;

import com.sistemabankcoxinha.patterns.factory.Coxinha;

public class DescontoDecorator extends CoxinhaDecorator {

    private final double percentualDesconto;

    public DescontoDecorator(Coxinha coxinhaBase, double percentualDesconto) {
        super(coxinhaBase);
        this.percentualDesconto = percentualDesconto;
    }
    public Double getPreco() {
        return coxinhaBase.getPreco() * (1 - percentualDesconto / 100);
    }

    public String getSabor() {
        return coxinhaBase.getSabor() + " (com " + (int) percentualDesconto + "% de desconto)";
    }
}
