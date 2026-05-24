package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.CompraRequestDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.Movimentacao;
import com.sistemabankcoxinha.model.SlotNota;
import com.sistemabankcoxinha.patterns.factory.Coxinha;
import com.sistemabankcoxinha.patterns.factory.CoxinhaFactory;
import com.sistemabankcoxinha.patterns.strategy.TrocoPadraoStrategy;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private SlotNotaRepository slotNotaRepository;

    public String comprar(CompraRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                        new RuntimeException("Cliente não encontrado!"));

        Coxinha coxinha =
                CoxinhaFactory.criarCoxinha(dto.getSabor());

        double valorCoxinha = coxinha.getPreco();

        int notaInserida = dto.getNotaInserida();

        if (notaInserida < valorCoxinha) {

            throw new RuntimeException("Valor insuficiente!");
        }

        double troco = notaInserida - valorCoxinha;

        List<SlotNota> slots =
                slotNotaRepository.findAll();

        TrocoPadraoStrategy strategy =
                new TrocoPadraoStrategy();

        strategy.calcularTroco(troco, slots);

        Movimentacao movimentacao =
                Movimentacao.builder()
                        .cliente(cliente)
                        .sabor(coxinha.getSabor())
                        .valor(valorCoxinha)
                        .tipoOperacao("COMPRA")
                        .dataHora(LocalDateTime.now())
                        .build();

        movimentacaoRepository.save(movimentacao);

        return "Compra realizada com sucesso.";
    }
}