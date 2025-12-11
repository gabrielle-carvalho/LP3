package br.uneb.lp3.av3.controler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.uneb.lp3.av3.model.Sala;
import br.uneb.lp3.av3.service.SalaService;

@RestController
@RequestMapping("/api/salas")
public class SalaController {
    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public List<Sala> list(){
      return salaService.list();
   }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> findById(Long id){
      return salaService.findById(id);
    }
    

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Sala criar(@RequestBody Sala sala) {
        return salaService.salvar(sala);
    }
}