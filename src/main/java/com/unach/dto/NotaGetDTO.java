package com.unach.dto;

import java.time.LocalDateTime;

public record NotaGetDTO(
    Integer id,
    String nombre,
    String cuerpo,
    LocalDateTime fechaCreacion,
    Integer idActividad,
    String tituloActividad,
    String hexColorActividad
) {
}
