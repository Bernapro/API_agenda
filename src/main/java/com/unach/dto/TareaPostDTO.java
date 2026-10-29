package com.unach.dto;

import java.time.LocalDateTime;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

public record TareaPostDTO(
    ActividadPostDTO actividad,
    LocalDateTime fechaLimite,
    EstadoTareaEnum estado,
    PrioridadTareaEnum prioridad,
    Integer idTareaPadre
) {
}
