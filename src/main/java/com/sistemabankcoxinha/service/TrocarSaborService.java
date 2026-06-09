package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.TrocarSaborRequestDTO;
import com.sistemabankcoxinha.dto.TrocarSaborResponseDTO;
import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.Movimentacao;
import com.sistemabankcoxinha.patterns.factory.Coxinha;
import com.sistemabankcoxinha.patterns.factory.CoxinhaFactory;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TrocarSaborService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public TrocarSaborResponseDTO trocarSabor(TrocarSaborRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Movimentacao ultimaCompra = cliente.getMovimentacoes().stream()
                .filter(m -> m.getTipoOperacao().equals("COMPRA") && m.getSabor().equalsIgnoreCase(dto.getSaborOrigem()))
                .reduce((first, second) -> second) // pega a mais recente
                .orElseThrow(() -> new RuntimeException("Nenhuma compra do sabor " + dto.getSaborOrigem() + " encontrada"));

        Coxinha novaCoxinha = CoxinhaFactory.criarCoxinha(dto.getSaborDestino());
        double valorDestino = novaCoxinha.getPreco();
        double valorOriginal = ultimaCompra.getValor();

        if (valorDestino > valorOriginal) {
            double diferenca = valorDestino - valorOriginal;
            if (cliente.getSaldo() < diferenca) {
                throw new RuntimeException("Saldo insuficiente para pagar a diferença de R$" + diferenca);
            }
            cliente.setSaldo(cliente.getSaldo() - diferenca);
        } else if (valorDestino < valorOriginal) {
            double restante = valorOriginal - valorDestino;
            cliente.setSaldo(cliente.getSaldo() + restante);
        }

        clienteRepository.save(cliente);

        Movimentacao trocaMov = Movimentacao.builder()
                .cliente(cliente)
                .sabor(dto.getSaborOrigem() + " -> " + dto.getSaborDestino())
                .valor(valorDestino)
                .tipoOperacao("TROCA_SABOR")
                .dataHora(LocalDateTime.now())
                .build();
        movimentacaoRepository.save(trocaMov);

        return new TrocarSaborResponseDTO(
                "Sabor trocado com sucesso!",
                novaCoxinha.getSabor(),
                valorDestino
        );
    }
}