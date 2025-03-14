package com.example.prenotazione_eventi.services;

import com.example.prenotazione_eventi.models.Prenotazione;
import com.example.prenotazione_eventi.models.Utente;
import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.repositoryes.PrenotazioneRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoService eventoService;

    @Transactional
     // Salva una nuova prenotazione
     public Prenotazione prenota(Prenotazione prenotazione) {
        Evento evento = prenotazione.getEvento();
        
        // Verifica se ci sono biglietti sufficienti per la prenotazione
        if (prenotazione.getNumeroBiglietti() > evento.getNumeroBigliettiDisponibili()) {
            throw new IllegalStateException("Non ci sono abbastanza biglietti disponibili per questo evento.");
        }

        // Diminuisci il numero di biglietti disponibili
        evento.setNumeroBigliettiDisponibili(evento.getNumeroBigliettiDisponibili() - prenotazione.getNumeroBiglietti());

        // Salva la prenotazione
        Prenotazione savedPrenotazione = prenotazioneRepository.save(prenotazione);

        // Salva l'evento con il nuovo numero di biglietti disponibili
        eventoService.save(evento);

        return savedPrenotazione;
    }

    // Recupera tutte le prenotazioni di un utente
    public List<Prenotazione> getPrenotazioniByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    // Recupera tutte le prenotazioni per un evento
    public List<Prenotazione> getPrenotazioniByEvento(Evento evento) {
        return prenotazioneRepository.findByEvento(evento);
    }

    // Elimina una prenotazione
    public void cancellaPrenotazione(Long id) {
        prenotazioneRepository.deleteById(id);
    }
}

