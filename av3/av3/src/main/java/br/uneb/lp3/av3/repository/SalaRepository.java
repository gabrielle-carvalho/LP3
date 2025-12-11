package br.uneb.lp3.av3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uneb.lp3.av3.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    boolean existsByNome(String nome);
}