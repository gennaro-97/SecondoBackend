package com.example.prenotazione_eventi.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.prenotazione_eventi.models.Utente;
import com.example.prenotazione_eventi.models.enums.Role;
import com.example.prenotazione_eventi.repositoryes.UtenteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteService {
    
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Gestisce la registrazione di un nuovo utente.
     * Codifica la password e salva l'utente nel database.
     *
     * @param utente L'utente da registrare
     */
    @Transactional
    public void register(Utente utente) {
        // Codifica la password prima di salvarla
        String encodedPassword = passwordEncoder.encode(utente.getPassword());
        utente.setPassword(encodedPassword);
        utente.setRuolo(Role.USER);  // Ruolo di default USER
        utenteRepository.save(utente);
    }

    public Utente findByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }
}
