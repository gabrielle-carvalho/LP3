package br.uneb.lp3.av3.controler;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.uneb.lp3.av3.model.Reserva;
import br.uneb.lp3.av3.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Reserva criar(@RequestBody Reserva reserva) {
        return reservaService.criarReserva(reserva);
    }

    @GetMapping("/sala/{id}")
    public List<Reserva> listarPorSala(@PathVariable Long id) {
        return reservaService.listarPorSala(id);
    }

    @PutMapping("/{id}")
    public Reserva atualizar(@PathVariable Long id, @RequestBody Reserva novaReserva) {
        return reservaService.atualizarReserva(id, novaReserva);
    }

    @GetMapping("/relatorio")
    public List<Map<String, Object>> relatorio() {
        return reservaService.gerarRelatorio();
    }

    @GetMapping("/{id}")
    public Map<String, Object> buscarPorId(@PathVariable Long id) {
        return reservaService.buscarReservaPorId(id);
    }
}
