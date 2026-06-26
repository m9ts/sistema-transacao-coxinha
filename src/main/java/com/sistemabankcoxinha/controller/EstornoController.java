package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.EstornoResponseDTO;
import com.sistemabankcoxinha.service.EstornoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estorno")
public class EstornoController {

    @Autowired
    private EstornoService service;

    @Operation(summary = "Estornar uma compra")
    @PostMapping("/{movimentacaoId}")
    public EstornoResponseDTO estornar(@PathVariable Long movimentacaoId) {
        return service.estornar(movimentacaoId);
    }
}