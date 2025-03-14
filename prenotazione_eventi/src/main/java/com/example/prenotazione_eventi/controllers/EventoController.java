package com.example.prenotazione_eventi.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.services.EventoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping("/eventi")
    public String listEventi(Model model) {
        model.addAttribute("eventi", eventoService.getAll());
        return "lista";
    }

    // Ricerca eventi per data
    @GetMapping("/ricercaPerData")
    public String ricercaPerData(@RequestParam("start") String start,
            @RequestParam("end") String end,
            Model model) {
        // Converte le stringhe in LocalDate
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        List<Evento> eventi = eventoService.getByDataBetween(startDate, endDate);
        model.addAttribute("eventi", eventi);
        return "lista"; // Restituisce la vista con gli eventi filtrati
    }

    // Ricerca eventi per prezzo
    @GetMapping("/ricercaPerPrezzo")
    public String ricercaPerPrezzo(@RequestParam("prezzo") double prezzo, Model model) {
        List<Evento> eventi = eventoService.getByPrezzoLessThan(prezzo);
        model.addAttribute("eventi", eventi);
        return "lista"; // Restituisce la vista con gli eventi filtrati
    }

}
