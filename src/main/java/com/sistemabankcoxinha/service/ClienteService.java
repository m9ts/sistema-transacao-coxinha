package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // salvando no banco
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // listando todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}
