package com.unach.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unach.dto.EtiquetaGetDTO;
import com.unach.service.EtiquetaService;

@RestController
@RequestMapping("/agenda/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping("/etiquetaCursor")
    public ResponseEntity<List<EtiquetaGetDTO>> obtenerEtiquetas(
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<EtiquetaGetDTO> etiquetas = etiquetaService.obtenerEtiquetas(id, limit);
        return ResponseEntity.ok(etiquetas);
    }
}
