package com.sistemabankcoxinha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class EstornoResponseDTO {
    private String mensagem;
    private String sabor;
    private Double valor;
}