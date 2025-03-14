package com.example.prenotazione_eventi.repositoryes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.models.Prenotazione;
import com.example.prenotazione_eventi.models.Utente;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtente(Utente utente); // Per visualizzare lo storico prenotazioni dell'utente
    List<Prenotazione> findByEvento(Evento evento); // Per visualizzare le prenotazioni per un evento
}
