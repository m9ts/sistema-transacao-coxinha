package com.sistemabankcoxinha.patterns.strategy;

import com.sistemabankcoxinha.model.SlotNota;

import java.util.List;
import java.util.Map;

public interface TrocoStrategy {

    Map<Integer, Integer> calcularTroco(Double valorTroco, List<SlotNota> slots);
}
