package com.sistemabankcoxinha.patterns.command;

import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.Movimentacao;
import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.patterns.factory.Coxinha;
import com.sistemabankcoxinha.patterns.strategy.TrocoStrategy;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ComprarCoxinhaCommand implements Command {

    private final Cliente cliente;
    private final Coxinha coxinha;
    private final TrocoStrategy trocoStrategy;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final SlotNotaRepository slotNotaRepository;

    @Getter
    private double trocoValor;
    @Getter
    private Movimentacao movimentacao;
    @Getter
    private Map<Integer, Integer> trocoNotas;

    public ComprarCoxinhaCommand(Cliente cliente, Coxinha coxinha,
                                 TrocoStrategy trocoStrategy,
                                 ClienteRepository clienteRepository,
                                 MovimentacaoRepository movimentacaoRepository,
                                 SlotNotaRepository slotNotaRepository) {
        this.cliente = cliente;
        this.coxinha = coxinha;
        this.trocoStrategy = trocoStrategy;
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.slotNotaRepository = slotNotaRepository;
    }

    @Override
    public void executar() {
        double valorCoxinha = coxinha.getPreco();
        double saldoAtual = cliente.getSaldo();

        if (saldoAtual < valorCoxinha) {
            throw new RuntimeException("Saldo insuficiente! Seu saldo: R$" + saldoAtual);
        }

        double trocoCalculado = saldoAtual - valorCoxinha;
        List<SlotNota> slots = slotNotaRepository.findAll();

        this.trocoNotas = trocoStrategy.calcularTroco(trocoCalculado, slots);

        cliente.setSaldo(saldoAtual - valorCoxinha);
        clienteRepository.save(cliente);

        Movimentacao mov = Movimentacao.builder()
                .cliente(cliente)
                .sabor(coxinha.getSabor())
                .valor(valorCoxinha)
                .tipoOperacao("COMPRA")
                .dataHora(LocalDateTime.now())
                .build();
        movimentacao = movimentacaoRepository.save(mov);

        this.trocoValor = trocoCalculado;
    }
}