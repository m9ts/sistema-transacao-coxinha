package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreditoRequestDTO {
    private Long clienteId;
    private Integer valorNota; // 2,5,10,20,50,100,200
}