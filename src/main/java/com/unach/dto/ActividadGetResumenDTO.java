package com.unach.dto;

import java.time.LocalDateTime;

import com.unach.enums.TipoActividadEnum;

public record ActividadGetResumenDTO(
    Integer id,
    String titulo,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    TipoActividadEnum tipoActividad
) {
}
