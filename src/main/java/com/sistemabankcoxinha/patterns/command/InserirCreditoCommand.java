package com.sistemabankcoxinha.patterns.command;

import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.SlotNotaRepository;

public class InserirCreditoCommand implements Command {
    private final Cliente cliente;
    private final Integer valorNota;
    private final ClienteRepository clienteRepository;
    private final SlotNotaRepository slotNotaRepository;

    public InserirCreditoCommand(Cliente cliente, Integer valorNota,
                                 ClienteRepository clienteRepository,
                                 SlotNotaRepository slotNotaRepository) {
        this.cliente = cliente;
        this.valorNota = valorNota;
        this.clienteRepository = clienteRepository;
        this.slotNotaRepository = slotNotaRepository;
    }

    @Override
    public void executar() {
        cliente.setSaldo(cliente.getSaldo() + valorNota);
        clienteRepository.save(cliente);

        SlotNota slot = slotNotaRepository.findByValorNota(valorNota)
                .orElseThrow(() -> new RuntimeException("Slot não encontrado para nota R$" + valorNota));
        slot.setQtd(slot.getQtd() + 1);
        slotNotaRepository.save(slot);
    }
}