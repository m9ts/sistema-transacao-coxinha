package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.CompraRequestDTO;
import com.sistemabankcoxinha.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")

public class CompraController {
    @Autowired
    private CompraService compraService;

    @PostMapping
    public String comprar(@RequestBody CompraRequestDTO dto) {
        return compraService.comprar(dto);
    }
}
