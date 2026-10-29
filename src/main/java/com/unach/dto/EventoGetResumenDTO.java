package com.unach.dto;

public record EventoGetResumenDTO(
    Integer idActividad,
    String titulo,
    String hexColor,
    Boolean todoElDia
) {
}
