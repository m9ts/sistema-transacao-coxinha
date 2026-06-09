package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.CreditoRequestDTO;
import com.sistemabankcoxinha.dto.CreditoResponseDTO;
import com.sistemabankcoxinha.service.InserirCreditoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credito")
public class InserirCreditoController {

    @Autowired
    private InserirCreditoService service;

    @Operation(summary = "Inserir crédito (nota física)")
    @PostMapping
    public CreditoResponseDTO inserirCredito(@RequestBody CreditoRequestDTO dto) {
        return service.inserirCredito(dto);
    }
}