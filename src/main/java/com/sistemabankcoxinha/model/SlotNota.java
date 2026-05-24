package com.sistemabankcoxinha.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "slot_notas")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SlotNota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer valorNota;
    private Integer qtd;
}
