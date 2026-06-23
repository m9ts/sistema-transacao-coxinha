package com.sistemabankcoxinha.CommandLineRunner;

import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SlotNotaRepository slotNotaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (slotNotaRepository.count() == 0) {
            int[] valores = {2, 5, 10, 20, 50, 100, 200};
            for (int v : valores) {
                SlotNota slot = SlotNota.builder()
                        .valorNota(v)
                        .qtd(5) // quantidade inicial
                        .build();
                slotNotaRepository.save(slot);
            }
            System.out.println("Slots de notas criados com 5 unidades cada.");
        }
    }
}