package com.unach.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unach.dto.ContactoGetResumenDTO;
import com.unach.dto.ContactoPostDTO;
import com.unach.dto.ContactoPostResponseDTO;
import com.unach.service.ContactoService;

@RestController
@RequestMapping("/agenda/contactos")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
    public ResponseEntity<ContactoPostResponseDTO> altaContacto(@RequestBody ContactoPostDTO dto) {
        ContactoPostResponseDTO respuesta = contactoService.altaContacto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/cursor")
    public ResponseEntity<List<ContactoGetResumenDTO>> obtenerResumenContactos(
            @RequestParam(defaultValue = "0") Integer id,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<ContactoGetResumenDTO> resumen = contactoService.obtenerResumenContactos(id, limit);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<ContactoGetResumenDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<ContactoGetResumenDTO> coincidencias = contactoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(coincidencias);
    }

    @GetMapping("/etiqueta")
    public ResponseEntity<List<ContactoGetResumenDTO>> buscarPorEtiqueta(@RequestParam String etiqueta) {
        List<ContactoGetResumenDTO> coincidencias = contactoService.buscarPorEtiqueta(etiqueta);
        return ResponseEntity.ok(coincidencias);
    }
}
