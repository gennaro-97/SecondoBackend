package com.example.prenotazione_eventi.repositoryes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prenotazione_eventi.models.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByEmail(String email);

}
