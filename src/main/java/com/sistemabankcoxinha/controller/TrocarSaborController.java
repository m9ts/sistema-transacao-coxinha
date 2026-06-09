package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.TrocarSaborRequestDTO;
import com.sistemabankcoxinha.dto.TrocarSaborResponseDTO;
import com.sistemabankcoxinha.service.TrocarSaborService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trocar-sabor")
public class TrocarSaborController {

    @Autowired
    private TrocarSaborService service;

    @Operation(summary = "Trocar sabor de uma coxinha reservada")
    @PutMapping
    public TrocarSaborResponseDTO trocarSabor(@RequestBody TrocarSaborRequestDTO dto) {
        System.out.println("Controller recebeu: clienteId=" + dto.getClienteId()
                + ", origem=" + dto.getSaborOrigem()
                + ", destino=" + dto.getSaborDestino());
        return service.trocarSabor(dto);
    }
}