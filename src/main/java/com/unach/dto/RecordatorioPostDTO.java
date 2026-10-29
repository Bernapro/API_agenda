package com.unach.dto;

import com.unach.enums.UnidadTiempoEnum;

public record RecordatorioPostDTO(
    Integer tiempoAnticipacion,
    UnidadTiempoEnum unidadTiempo
) {
}
