package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.CompraRequestDTO;
import com.sistemabankcoxinha.dto.CompraResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.patterns.command.ComprarCoxinhaCommand;
import com.sistemabankcoxinha.patterns.factory.Coxinha;
import com.sistemabankcoxinha.patterns.factory.CoxinhaFactory;
import com.sistemabankcoxinha.patterns.observer.CompraDisparaEvento;
import com.sistemabankcoxinha.patterns.strategy.TrocoPadraoStrategy;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import com.sistemabankcoxinha.repository.SlotNotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private SlotNotaRepository slotNotaRepository;

    @Autowired
    private CompraDisparaEvento compraDisparaEvento;

    public CompraResponseDTO comprar(CompraRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Coxinha coxinha = CoxinhaFactory.criarCoxinha(
                dto.getSabor(),
                dto.isAdicionarRecheio(),
                dto.isAplicarDesconto()
        );

        ComprarCoxinhaCommand command = new ComprarCoxinhaCommand(
                cliente,
                coxinha,
                new TrocoPadraoStrategy(),
                clienteRepository,
                movimentacaoRepository,
                slotNotaRepository
        );
        command.executar();

        // dispara os observers (log, extrato, estoque)
        compraDisparaEvento.publicar(command.getMovimentacao());

        CompraResponseDTO response = new CompraResponseDTO();
        response.setMensagem("Compra realizada com sucesso! Troco mantido como crédito.");
        response.setSabor(coxinha.getSabor());
        response.setValorCompra(coxinha.getPreco());
        response.setValorPago((int) (coxinha.getPreco() + command.getTrocoValor()));
        response.setTroco((int) command.getTrocoValor());
        response.setNotasTroco(command.getTrocoNotas());

        return response;
    }
}