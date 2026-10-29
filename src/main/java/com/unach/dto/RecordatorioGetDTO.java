package com.unach.dto;

import com.unach.enums.UnidadTiempoEnum;

public record RecordatorioGetDTO(
    Short disc,
    Integer tiempoAnticipacion,
    UnidadTiempoEnum unidadTiempo
) {
}
