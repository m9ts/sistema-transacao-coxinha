package com.sistemabankcoxinha.repository;

import com.sistemabankcoxinha.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// cria métodos SQL com JpaRepository
public interface ClienteRepository extends JpaRepository <Cliente, Long> {
}
