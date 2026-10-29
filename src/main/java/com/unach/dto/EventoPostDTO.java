package com.unach.dto;

import java.util.List;

public record EventoPostDTO(
    ActividadPostDTO actividad,
    String direccion,
    Boolean todoElDia,
    RecurrenciaPostDTO recurrencia,
    List<RecordatorioPostDTO> recordatorios,
    List<Integer> idEtiquetas,
    List<ContactoEventoPostDTO> contactosEventos
) {
}
