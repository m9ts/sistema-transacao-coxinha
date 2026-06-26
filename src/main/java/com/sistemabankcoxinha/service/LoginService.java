package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.LoginRequestDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente login(LoginRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!cliente.getSenha().equals(dto.getSenha())) {
            throw new RuntimeException("Senha incorreta!");
        }

        return cliente;
    }
}