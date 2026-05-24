package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotNotaService {

    @Autowired
    private SlotNotaRepository slotNotaRepository;

    public SlotNota salvarSlot(SlotNota slotNota) {

        return slotNotaRepository.save(slotNota);
    }

    public List<SlotNota> listarSlots() {

        return slotNotaRepository.findAll();
    }
}