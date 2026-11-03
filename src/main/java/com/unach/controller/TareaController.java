package com.unach.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unach.dto.SesionGetDTO;
import com.unach.dto.SesionPostDTO;
import com.unach.dto.SubtareaGetDTO;
import com.unach.dto.TareaGetDTO;
import com.unach.dto.TareaGetResumenDTO;
import com.unach.dto.TareaPostDTO;
import com.unach.service.TareaService;

@RestController
@RequestMapping("/agenda/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    public ResponseEntity<TareaGetDTO> altaTarea(@RequestBody TareaPostDTO dto) {
        TareaGetDTO tareaCreada = tareaService.altaTarea(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaCreada);
    }

    @GetMapping("/cursor")
    public ResponseEntity<List<TareaGetResumenDTO>> obtenerResumenTarea(
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<TareaGetResumenDTO> resumen = tareaService.obtenerResumenTarea(id, limit, fechaInicio, fechaFin);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaGetDTO> obtenerTarea(@PathVariable Integer id) {
        TareaGetDTO tarea = tareaService.obtenerTarea(id);
        return ResponseEntity.ok(tarea);
    }

    @GetMapping("/subtareas")
    public ResponseEntity<List<SubtareaGetDTO>> obtenerSubtareas(@RequestParam Integer id) {
        List<SubtareaGetDTO> subtareas = tareaService.obtenerSubtareas(id);
        return ResponseEntity.ok(subtareas);
    }

    @PostMapping("/sesion")
    public ResponseEntity<SesionGetDTO> registrarSesion(@RequestBody SesionPostDTO dto) {
        SesionGetDTO sesionRegistrada = tareaService.registrarSesion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionRegistrada);
    }
}
