package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequestDTO {
    private Long clienteId;
    private String senha;
}