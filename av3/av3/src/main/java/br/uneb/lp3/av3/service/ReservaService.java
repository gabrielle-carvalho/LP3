package br.uneb.lp3.av3.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.uneb.lp3.av3.exceptions.HorarioIndisponivelException;
import br.uneb.lp3.av3.exceptions.SalaDuplicadaException;
import br.uneb.lp3.av3.exceptions.SalaNaoEncontradaException;
import br.uneb.lp3.av3.model.Reserva;
import br.uneb.lp3.av3.model.Sala;
import br.uneb.lp3.av3.repository.ReservaRepository;
import br.uneb.lp3.av3.repository.SalaRepository;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;

    public ReservaService(ReservaRepository reservaRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.salaRepository = salaRepository;
    }

    /**
     * QUESTÃO 02 (2,0 pontos)
     */
    public List<Reserva> listarPorSala(Long salaId) {
        Sala sala = this.salaRepository.findById(salaId)
                   .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada"));

        return this.reservaRepository.findBySalaId(sala.getId());

    }

    /**
     * QUESTÃO 03 (2,0 pontos)
     */
    public Reserva criarReserva(Reserva novaReserva) {

        if(this.verificaConflitoDeHorario(novaReserva, null))
        {
            throw new HorarioIndisponivelException("Horário Indisponível.");
        }

        return novaReserva;
    }

    /**
     * QUESTÃO 04 (2,0 pontos)
     */
    public Reserva atualizarReserva(Long id, Reserva novaReserva) {
        if(this.verificaConflitoDeHorario(novaReserva, id))
            {
                throw new HorarioIndisponivelException("Horário Indisponível.");
            }

        return novaReserva;
    }

    public List<Reserva> buscarReservasPorSala(Long salaId) {
        return reservaRepository.findBySalaId(salaId);
    }

    public Map<String, Object> buscarReservaPorId(Long id) {
        return null;
    }

    /**
     * QUESTÃO 05 (2,0 pontos)
     */
    public List<Map<String, Object>> gerarRelatorio() {
        List<Map<String, Object>> listaDoRelatorio = new ArrayList<>();
        for(Sala sala : this.salaRepository.findAll()){
            String nome = sala.getNome();
            int totalReservas = this.reservaRepository.findBySalaId(sala.getId()).size();

            Map<String, Object> a = new HashMap<>();
            a.put(nome, totalReservas);
            listaDoRelatorio.add(a);
        }
        return listaDoRelatorio;
    }

    /**
     * Método auxiliar para verificar conflitos de horário
     * 
     * @param novaReserva - A reserva a ser verificada
     * @param idAtual - ID da reserva atual (null se for nova reserva)
     * @return true se houver conflito, false caso contrário
     */
    public boolean verificaConflitoDeHorario(Reserva novaReserva, Long idAtual) {
        List<Reserva> reservasExistentes = reservaRepository.findBySalaId(novaReserva.getSala().getId());
    
        for (Reserva reserva : reservasExistentes) {
            if (reserva.getId().equals(idAtual) && idAtual != null) {
                continue;
            }
            
            LocalDateTime inicioExistente = reserva.getDataHora();
            LocalDateTime fimExistente = inicioExistente.plusMinutes(reserva.getPeriodo());
    
            LocalDateTime inicioNova = novaReserva.getDataHora();
            LocalDateTime fimNova = inicioNova.plusMinutes(novaReserva.getPeriodo());
    
            if ((inicioNova.isBefore(fimExistente) && inicioNova.isAfter(inicioExistente)) ||
                (fimNova.isAfter(inicioExistente) && fimNova.isBefore(fimExistente)) ||
                (inicioNova.isEqual(inicioExistente) || fimNova.isEqual(fimExistente)) ||
                (inicioNova.isBefore(inicioExistente) && fimNova.isAfter(fimExistente))) {
                return true;
            }
        }
    
        return false;
    }
}
