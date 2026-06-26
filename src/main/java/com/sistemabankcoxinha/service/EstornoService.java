package com.sistemabankcoxinha.service;

import com.sistemabankcoxinha.dto.EstornoResponseDTO;
import com.sistemabankcoxinha.model.Movimentacao;
import com.sistemabankcoxinha.patterns.command.EstornarCompraCommand;
import com.sistemabankcoxinha.repository.ClienteRepository;
import com.sistemabankcoxinha.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstornoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public EstornoResponseDTO estornar(Long movimentacaoId) {
        Movimentacao mov = movimentacaoRepository.findById(movimentacaoId)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));

        if (!mov.getTipoOperacao().equals("COMPRA")) {
            throw new RuntimeException("Apenas compras podem ser estornadas.");
        }

        EstornarCompraCommand command = new EstornarCompraCommand(
                mov, clienteRepository, movimentacaoRepository
        );
        command.executar();

        return new EstornoResponseDTO(
                "Compra estornada com sucesso!",
                mov.getSabor(),
                mov.getValor()
        );
    }
}