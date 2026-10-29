package com.unach.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.unach.enums.EstadoSesionEnum;

public record SesionGetDTO(
    Short disc,
    LocalDate fechaProgramada,
    LocalTime horaInicio,
    LocalTime horaFin,
    EstadoSesionEnum estado,
    String notas
) {
}
