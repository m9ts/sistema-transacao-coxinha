package com.sistemabankcoxinha.dto;

import com.sistemabankcoxinha.model.Movimentacao;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ExtratoResponseDTO {

    private String cliente;
    private Double saldo;
    private List<Movimentacao> movimentacoes;
}