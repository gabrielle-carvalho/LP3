package br.uneb.lp3.av3;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.uneb.lp3.av3.model.Reserva;
import br.uneb.lp3.av3.model.Sala;
import br.uneb.lp3.av3.repository.SalaRepository;

@SpringBootApplication
public class Av3Application {

	public static void main(String[] args) {
		SpringApplication.run(Av3Application.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(SalaRepository salaRepository){
		return args -> {
			salaRepository.deleteAll();
			Sala s = new Sala();
			s.setNome("Azulão");

			Reserva r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T09:00:00"));
			r.setResponsavel("Sucupira");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T10:00:00"));
			r.setResponsavel("Jaracatiá");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);
			
			s = new Sala();
			s.setNome("Chauá");
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T09:00:00"));
			r.setResponsavel("Juçara");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T10:00:00"));
			r.setResponsavel("Jaracatiá");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);

			s = new Sala();
			s.setNome("Crejoá");
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-16T08:00:00"));
			r.setResponsavel("Jaracatiá");
			r.setPeriodo(30);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-16T09:00:00"));
			r.setResponsavel("Mutambo");
			r.setPeriodo(30);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);

			s = new Sala();
			s.setNome("Ipecuá");
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T07:00:00"));
			r.setResponsavel("Canafístula");
			r.setPeriodo(90);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T08:30:00"));
			r.setResponsavel("Jaracatiá");
			r.setPeriodo(90);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);

			s = new Sala();
			s.setNome("Jacuguaçu");
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-16T09:00:00"));
			r.setResponsavel("Canafístula");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-16T10:00:00"));
			r.setResponsavel("Mutambo");
			r.setPeriodo(60);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);

			s = new Sala();
			s.setNome("Pixoxó");
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T07:30:00"));
			r.setResponsavel("Sucupira");
			r.setPeriodo(150);
			r.setSala(s);
			s.getReserva().add(r);
			r = new Reserva();
			r.setDataHora(LocalDateTime.parse("2024-04-15T10:00:00"));
			r.setResponsavel("Juçara");
			r.setPeriodo(120);
			r.setSala(s);
			s.getReserva().add(r);
			salaRepository.save(s);

		};
	}

}
