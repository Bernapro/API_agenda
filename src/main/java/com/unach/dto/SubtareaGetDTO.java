package com.unach.dto;

import java.time.LocalDateTime;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

public record SubtareaGetDTO(
    Integer idActividad,
    String titulo,
    EstadoTareaEnum estado,
    PrioridadTareaEnum prioridad,
    LocalDateTime fechaLimite
) {
}
