package com.example.prenotazione_eventi.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.repositoryes.EventoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;

    public List<Evento> getAll() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> getById(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento update(Long id, Evento eventoDettagli) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    evento.setTitolo(eventoDettagli.getTitolo());
                    evento.setDescrizione(eventoDettagli.getDescrizione());
                    evento.setData(eventoDettagli.getData());
                    evento.setPrezzo(eventoDettagli.getPrezzo());
                    return eventoRepository.save(evento);
                })
                .orElseThrow(() -> new RuntimeException("Evento non trovato!"));
    }

    public void delete(Long id) {
        eventoRepository.deleteById(id);
    }

        // Ricerca eventi per data
        public List<Evento> getByDataBetween(LocalDate start, LocalDate end) {
            return eventoRepository.findByDataBetween(start, end);
        }
    
        // Ricerca eventi per prezzo
        public List<Evento> getByPrezzoLessThan(double prezzo) {
            return eventoRepository.findByPrezzoLessThan(prezzo);
        }
}

