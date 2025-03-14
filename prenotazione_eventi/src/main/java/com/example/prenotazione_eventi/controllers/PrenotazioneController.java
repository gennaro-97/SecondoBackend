package com.example.prenotazione_eventi.controllers;

import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.models.Prenotazione;
import com.example.prenotazione_eventi.models.Utente;
import com.example.prenotazione_eventi.services.EventoService;
import com.example.prenotazione_eventi.services.PrenotazioneService;
import com.example.prenotazione_eventi.services.UtenteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/prenotazioni")
public class PrenotazioneController {

    private final EventoService eventoService;
    private final PrenotazioneService prenotazioneService;
    private final UtenteService utenteService;

    public PrenotazioneController(EventoService eventoService, PrenotazioneService prenotazioneService,
            UtenteService utenteService) {
        this.eventoService = eventoService;
        this.prenotazioneService = prenotazioneService;
        this.utenteService = utenteService;
    }

    // Mostra il modulo di prenotazione per un evento specifico
    @GetMapping("/prenota/{eventoId}")
    public String showPrenotazioneForm(@PathVariable Long eventoId, Model model) {
        // Recupera l'evento tramite Optional
        Evento evento = eventoService.getById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato!")); // Gestiamo l'errore se l'evento non
                                                                                 // esiste

        model.addAttribute("evento", evento);
        model.addAttribute("prenotazione", new Prenotazione()); // Oggetto vuoto per la prenotazione
        return "user/prenotazione"; // Mostra la vista di prenotazione
    }

    // Gestisce la prenotazione per un evento
    @PostMapping("/prenota/{eventoId}")
    public String prenotaEvento(@ModelAttribute Prenotazione prenotazione,
                                 @PathVariable Long eventoId,
                                 @AuthenticationPrincipal User userDetails) {
    
        // Recupera l'utente autenticato
        Utente utente = utenteService.findByEmail(userDetails.getUsername());
    
        // Recupera l'evento tramite Optional
        Evento evento = eventoService.getById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato!"));
    
        // Assegniamo l'utente e l'evento alla prenotazione
        prenotazione.setUtente(utente);
        prenotazione.setEvento(evento);
        
        try {
            // Creiamo la prenotazione e aggiorniamo i biglietti disponibili
            prenotazioneService.prenota(prenotazione);
        } catch (IllegalStateException e) {
            // Gestione dell'errore se non ci sono abbastanza biglietti
            return "error";  // Puoi reindirizzare a una pagina di errore o mostrare un messaggio
        }
    
        return "redirect:/user/prenotazioni/storico";  // Reindirizza allo storico delle prenotazioni
    }

    // Mostra lo storico delle prenotazioni dell'utente
    @GetMapping("/storico")
    public String getStoricoPrenotazioni(Model model, @AuthenticationPrincipal User userDetails) {
        Utente utente = utenteService.findByEmail(userDetails.getUsername()); // Recuperiamo l'utente dal servizio

        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniByUtente(utente);
        model.addAttribute("prenotazioni", prenotazioni);
        return "user/storico-prenotazioni"; // Mostra la lista delle prenotazioni effettuate
    }
}
