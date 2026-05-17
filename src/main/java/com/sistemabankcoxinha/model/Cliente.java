package com.sistemabankcoxinha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id automático
    private Long id;
    private String nome;
    private Double saldo;
}
