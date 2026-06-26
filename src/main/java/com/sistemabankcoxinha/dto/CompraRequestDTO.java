package com.sistemabankcoxinha.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CompraRequestDTO {
    private Long clienteId;
    private String sabor;
    private Integer notaInserida;
    private boolean adicionarRecheio;
    private boolean aplicarDesconto;
}
