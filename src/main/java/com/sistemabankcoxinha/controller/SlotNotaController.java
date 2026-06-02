package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.service.SlotNotaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")

public class SlotNotaController {

    @Autowired
    private SlotNotaService slotNotaService;

    @Operation(summary = "Adicionar slot de nota")
    @PostMapping
    public SlotNota adicionarSlot(
            @RequestBody SlotNota slotNota) {

        return slotNotaService.salvarSlot(slotNota);
    }

    @Operation(summary = "Listar todos os slots de notas")
    @GetMapping
    public List<SlotNota> listarSlots() {

        return slotNotaService.listarSlots();
    }
}