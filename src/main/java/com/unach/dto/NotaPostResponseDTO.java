package com.unach.dto;

import java.time.LocalDateTime;

public record NotaPostResponseDTO(
    Integer id,
    String nombre,
    LocalDateTime fechaCreacion,
    Integer idActividad
) {
}
