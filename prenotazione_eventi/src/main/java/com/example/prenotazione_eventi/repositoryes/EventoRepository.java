package com.example.prenotazione_eventi.repositoryes;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prenotazione_eventi.models.Evento;

public interface EventoRepository  extends JpaRepository<Evento, Long>{
    List<Evento> findByDataBetween(LocalDate start, java.time.LocalDate end); // Ricerca per data
    List<Evento> findByPrezzoLessThan(double prezzo); // Ricerca per prezzo
}
