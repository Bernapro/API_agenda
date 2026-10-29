package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventoGetDTO(
    Integer idActividad,
    String titulo,
    String descripcion,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    String direccion,
    Boolean todoElDia,
    RecurrenciaGetDTO recurrencia,
    List<RecordatorioGetDTO> recordatorios,
    List<ContactoEventoGetDTO> contactosEventos,
    List<EtiquetaGetDTO> etiquetas,
    List<NotaGetDTO> notas
) {
}
