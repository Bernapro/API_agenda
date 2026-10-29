package com.unach.dto;

import java.time.LocalDate;

import com.unach.enums.FrecuenciaEnum;

public record RecurrenciaGetDTO(
    FrecuenciaEnum frecuencia,
    Short intervalo,
    String diasSemana,
    Short diaMes,
    Short mesAnio,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {
}
