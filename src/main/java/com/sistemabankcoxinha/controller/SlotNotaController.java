package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.service.SlotNotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")

public class SlotNotaController {

    @Autowired
    private SlotNotaService slotNotaService;

    @PostMapping
    public SlotNota adicionarSlot(
            @RequestBody SlotNota slotNota) {

        return slotNotaService.salvarSlot(slotNota);
    }

    @GetMapping
    public List<SlotNota> listarSlots() {

        return slotNotaService.listarSlots();
    }
}