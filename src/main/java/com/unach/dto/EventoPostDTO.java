package com.unach.dto;

import java.util.List;

public record EventoPostDTO(
    ActividadPostDTO actividad,
    String direccion,
    Boolean todoElDia,
    List<RecordatorioPostDTO> recordatorios,
    List<ContactoEventoPostDTO> contactosEventos
) {
}