package com.sistemabankcoxinha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CreditoResponseDTO {
    private String mensagem;
    private Double novoSaldo;
}