package com.sistemabankcoxinha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class TrocarSaborResponseDTO {
    private String mensagem;
    private String novoSabor;
    private Double valorPago;
}