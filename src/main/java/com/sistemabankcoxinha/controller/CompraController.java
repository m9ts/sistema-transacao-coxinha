package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.CompraRequestDTO;
import com.sistemabankcoxinha.dto.CompraResponseDTO;
import com.sistemabankcoxinha.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Operation(summary = "Realizar compra")
    @PostMapping
    public CompraResponseDTO comprar(@RequestBody CompraRequestDTO dto) {
        return compraService.comprar(dto);
    }
}