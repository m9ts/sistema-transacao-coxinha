package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.CreditoRequestDTO;
import com.sistemabankcoxinha.dto.CreditoResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.patterns.command.InserirCreditoCommand;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InserirCreditoService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private SlotNotaRepository slotNotaRepository;

    public CreditoResponseDTO inserirCredito(CreditoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        InserirCreditoCommand command = new InserirCreditoCommand(
                cliente, dto.getValorNota(), clienteRepository, slotNotaRepository);
        command.executar();

        return new CreditoResponseDTO(
                "Crédito de R$" + dto.getValorNota() + " inserido com sucesso!",
                cliente.getSaldo()
        );
    }
}