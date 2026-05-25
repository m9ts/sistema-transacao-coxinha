package com.sistemabankcoxinha.controller;

import com.sistemabankcoxinha.dto.ExtratoResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes") // URL base

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public Cliente salvarCliente(@RequestBody Cliente cliente) {
        return clienteService.salvarCliente(cliente);
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}/extrato")
    public ExtratoResponseDTO extrato(@PathVariable Long id) {
        return clienteService.obterExtrato(id);
    }
}
