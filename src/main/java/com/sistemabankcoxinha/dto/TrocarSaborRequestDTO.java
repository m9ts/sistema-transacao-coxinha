package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TrocarSaborRequestDTO {
    private Long clienteId;
    private String saborOrigem;
    private String saborDestino;
}