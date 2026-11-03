package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

public record TareaGetResumenDTO(
    Integer idActividad,
    String titulo,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    LocalDateTime fechaLimite,
    EstadoTareaEnum estado,
    PrioridadTareaEnum prioridad,
    List<EtiquetaGetDTO> etiquetas
) {
}
