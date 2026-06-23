package com.sistemabankcoxinha.patterns.strategy;

import com.sistemabankcoxinha.model.SlotNota;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrocoPadraoStrategy implements TrocoStrategy {
    @Override
    public Map<Integer, Integer> calcularTroco(Double valorTroco, List<SlotNota> slots) {
        Map<Integer, Integer> troco = new HashMap<>();

        int restante = valorTroco.intValue();

        // ordenação de notas da maior para a menor
        slots.sort((a, b) -> b.getValorNota() - a.getValorNota());

        for (SlotNota slot : slots) {
            int nota = slot.getValorNota();
            int quantidadeDisponivel = slot.getQtd();

            int quantidadeUsada = 0;

            while (restante >= nota && quantidadeDisponivel > 0) {
                restante -= nota;
                quantidadeDisponivel--;
                quantidadeUsada++;
            }
            if (quantidadeUsada > 0) {
                troco.put(nota, quantidadeUsada);
            }
        }
        if (restante != 0) {
            throw new RuntimeException("Transação impossível: falta de cédulas específicas!");
        }
        return troco;
    }
}
