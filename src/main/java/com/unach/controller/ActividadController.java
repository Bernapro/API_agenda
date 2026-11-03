package com.unach.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unach.dto.ActividadGetResumenDTO;
import com.unach.service.ActividadService;

@RestController
@RequestMapping("/agenda/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping("/etiqueta")
    public ResponseEntity<List<ActividadGetResumenDTO>> buscarPorEtiqueta(@RequestParam String etiqueta) {
        List<ActividadGetResumenDTO> coincidencias = actividadService.buscarPorEtiqueta(etiqueta);
        return ResponseEntity.ok(coincidencias);
    }

    @GetMapping("/cursor")
    public ResponseEntity<List<ActividadGetResumenDTO>> obtenerResumenActividad(
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<ActividadGetResumenDTO> resumen = actividadService.obtenerResumenActividad(id, limit, fechaInicio, fechaFin);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/titulo")
    public ResponseEntity<List<ActividadGetResumenDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<ActividadGetResumenDTO> coincidencias = actividadService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(coincidencias);
    }

    @GetMapping("/mes")
    public ResponseEntity<List<ActividadGetResumenDTO>> obtenerPorMes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd/MM/yyyy", "dd-MM-yyyy"}) LocalDate fecha) {
        List<ActividadGetResumenDTO> actividades = actividadService.obtenerPorMes(fecha);
        return ResponseEntity.ok(actividades);
    }
}
