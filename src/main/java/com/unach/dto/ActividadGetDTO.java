package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ActividadGetDTO(
    Integer id,
    String titulo,
    String descripcion,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    List<EtiquetaGetDTO> etiquetas
) {
}
