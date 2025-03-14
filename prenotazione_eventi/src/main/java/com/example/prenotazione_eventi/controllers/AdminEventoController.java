package com.example.prenotazione_eventi.controllers;

import com.example.prenotazione_eventi.models.Evento;
import com.example.prenotazione_eventi.services.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminEventoController {

    private final EventoService eventoService;

    @GetMapping("/eventi")
    public String listaEventi(Model model) {
        List<Evento> eventi = eventoService.getAll();
        model.addAttribute("eventi", eventi);
        return "eventiAdmin";
    }

    @GetMapping("/crea")
    public String creaEventoForm(Model model) {
        model.addAttribute("evento", new Evento());
        return "crea";
    }

    @PostMapping("/crea")
    public String creaEvento(@ModelAttribute Evento evento) {
        eventoService.save(evento);
        return "redirect:/admin/eventi";
    }

    @GetMapping("/modifica/{id}")
    public String modificaEventoForm(@PathVariable Long id, Model model) {
        Evento evento = eventoService.getById(id).orElseThrow(() -> new RuntimeException("Evento non trovato"));
        model.addAttribute("evento", evento);
        return "modifica";
    }

    @PostMapping("/modifica/{id}")
    public String modificaEvento(@PathVariable Long id, @ModelAttribute Evento eventoDettagli) {
        eventoService.update(id, eventoDettagli);
        return "redirect:/admin/eventi";
    }

    @GetMapping("/elimina/{id}")
    public String eliminaEvento(@PathVariable Long id) {
        eventoService.delete(id);
        return "redirect:/admin/eventi";
    }
}
