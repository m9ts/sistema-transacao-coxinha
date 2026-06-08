package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraResponseDTO {
    private String mensagem;
    private String sabor;
    private Double valorCompra;
    private Integer valorPago;
    private Integer troco;
}
