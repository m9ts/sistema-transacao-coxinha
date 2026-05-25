package com.sistemabankcoxinha.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sabor;
    private Double valor;
    private String tipoOperacao;
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;
}
