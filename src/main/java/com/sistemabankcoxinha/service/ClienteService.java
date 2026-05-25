package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.ExtratoResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // salvar cliente
    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // obter extrato do cliente
    public ExtratoResponseDTO obterExtrato(Long clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado");
        }

        ExtratoResponseDTO extrato = new ExtratoResponseDTO();

        extrato.setCliente(cliente.getNome());
        extrato.setSaldo(cliente.getSaldo());
        extrato.setMovimentacoes(cliente.getMovimentacoes());

        return extrato;
    }
}