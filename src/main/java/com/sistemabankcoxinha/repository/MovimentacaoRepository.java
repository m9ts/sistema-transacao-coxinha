package com.sistemabankcoxinha.repository;

import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}