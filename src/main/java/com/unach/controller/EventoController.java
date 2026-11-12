package com.unach.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unach.dto.EventoGetDTO;
import com.unach.dto.EventoGetResumenDTO;
import com.unach.dto.EventoPostDTO;
import com.unach.service.EventoService;

@RestController
@RequestMapping("/agenda/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoGetDTO> altaEvento(@RequestBody EventoPostDTO dto) {
        EventoGetDTO eventoCreado = eventoService.altaEvento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCreado);
    }

    @GetMapping("/cursor")
    public ResponseEntity<List<EventoGetResumenDTO>> obtenerResumenEvento(
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<EventoGetResumenDTO> resumen = eventoService.obtenerResumenEvento(id, limit, fechaInicio, fechaFin);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoGetDTO> obtenerEvento(@PathVariable Integer id) {
        EventoGetDTO evento = eventoService.obtenerEvento(id);
        return ResponseEntity.ok(evento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoGetDTO> actualizarEvento(
            @PathVariable Integer id,
            @RequestBody EventoPostDTO dto) {
        return ResponseEntity.ok(eventoService.actualizarEvento(id, dto));
    }
}
