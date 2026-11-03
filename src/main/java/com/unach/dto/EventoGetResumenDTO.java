package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventoGetResumenDTO(
    Integer idActividad,
    String titulo,
    String hexColor,
    Boolean todoElDia,
    String direccion,
    Integer cantidadInvitados,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    List<EtiquetaGetDTO> etiquetas
) {
}
