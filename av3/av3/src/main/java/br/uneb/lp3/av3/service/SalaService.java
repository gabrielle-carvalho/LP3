package br.uneb.lp3.av3.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.uneb.lp3.av3.controler.SalaController;
import br.uneb.lp3.av3.exceptions.SalaDuplicadaException;
import br.uneb.lp3.av3.model.Sala;
import br.uneb.lp3.av3.repository.SalaRepository;

@Service
public class SalaService {

    private final SalaController salaController;
    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository, SalaController salaController) {
        this.salaRepository = salaRepository;
        this.salaController = salaController;
    }

    /**
     * QUESTÃO 01 (2,0 pontos)
     implemente os metodos salvar() e list()
     */
    
    public Sala salvar(Sala sala) {
        if(this.salaRepository.existsByNome(sala.getNome())) {
            throw new SalaDuplicadaException("Sala já existe.");
        }

        return sala;
    }

    public List<Sala> list(){
        return this.salaRepository.findAll();
    }

    public ResponseEntity<Sala> findById(@PathVariable Long id){
        return salaRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
