package com.sistemabankcoxinha.repository;

import com.sistemabankcoxinha.model.Cliente;
import com.sistemabankcoxinha.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    Optional<Movimentacao> findFirstByClienteOrderByDataHoraDesc(Cliente cliente);
}