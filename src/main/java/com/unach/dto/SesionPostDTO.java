package com.unach.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.unach.enums.EstadoSesionEnum;

public record SesionPostDTO(
    Integer idActividad,
    LocalDate fechaProgramada,
    LocalTime horaInicio,
    LocalTime horaFin,
    EstadoSesionEnum estado,
    String notas
) {
}
