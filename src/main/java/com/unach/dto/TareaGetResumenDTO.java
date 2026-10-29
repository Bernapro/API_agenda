package com.unach.dto;

import java.time.LocalDateTime;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

public record TareaGetResumenDTO(
    Integer idActividad,
    String titulo,
    String descripcion,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    LocalDateTime fechaLimite,
    EstadoTareaEnum estado,
    PrioridadTareaEnum prioridad
) {
}
