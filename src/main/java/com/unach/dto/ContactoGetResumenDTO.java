package com.unach.dto;

public record ContactoGetResumenDTO(
    Integer id,
    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    String apodo,
    String avatar,
    Boolean favorito
) {
}
