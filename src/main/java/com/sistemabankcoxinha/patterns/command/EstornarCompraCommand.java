package com.sistemabankcoxinha.patterns.command;

import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.Movimentacao;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import lombok.Getter;

import java.time.LocalDateTime;

public class EstornarCompraCommand implements Command {

    private final Movimentacao movimentacao;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    @Getter private Movimentacao estornoMovimentacao;

    public EstornarCompraCommand(Movimentacao movimentacao,
                                 ClienteRepository clienteRepository,
                                 MovimentacaoRepository movimentacaoRepository) {
        this.movimentacao = movimentacao;
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public void executar() {
        if (movimentacao.isEstornado()) {
            throw new RuntimeException("Esta compra já foi estornada.");
        }

        Cliente cliente = movimentacao.getCliente();

        cliente.setSaldo(cliente.getSaldo() + movimentacao.getValor());
        clienteRepository.save(cliente);

        movimentacao.setEstornado(true);
        movimentacaoRepository.save(movimentacao);

        Movimentacao estorno = Movimentacao.builder()
                .cliente(cliente)
                .sabor("ESTORNO: " + movimentacao.getSabor())
                .valor(movimentacao.getValor())
                .tipoOperacao("ESTORNO")
                .dataHora(LocalDateTime.now())
                .estornado(false)
                .build();
        estornoMovimentacao = movimentacaoRepository.save(estorno);
    }
}