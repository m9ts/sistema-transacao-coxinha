package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.ExtratoResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/clientes") // URL base

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Cadastrar cliente")
    @PostMapping
    public Cliente salvarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvarCliente(cliente);
    }

    @Operation(summary = "Listar todos os clientes")
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @Operation(summary = "Consultar extrato de um cliente")
    @GetMapping("/{id}/extrato")
    public ExtratoResponseDTO extrato(@PathVariable Long id) {
        return clienteService.obterExtrato(id);
    }
}
