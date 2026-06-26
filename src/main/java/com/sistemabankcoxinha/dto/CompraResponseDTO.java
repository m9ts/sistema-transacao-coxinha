package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CompraResponseDTO {
    private String mensagem;
    private String sabor;
    private Double valorCompra;
    private Integer valorPago;
    private Integer troco;
    private Map<Integer, Integer> notasTroco;
}
