package br.uneb.lp3.av3.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.uneb.lp3.av3.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySalaId(Long salaId);
    boolean existsBySalaIdAndDataHora(Long salaId, LocalDateTime dataHora);
    @Query("SELECT r.sala.nome AS nomeSala, COUNT(r) AS totalReservas FROM Reserva r GROUP BY r.sala.nome")
    List<Object[]> gerarRelatorio();
}
