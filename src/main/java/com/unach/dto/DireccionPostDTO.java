package com.unach.dto;

public record DireccionPostDTO(
    String direccion,
    String segundaLinea,
    String apartadoPostal,
    String codigoPostal,
    String ciudad,
    String estado,
    String pais,
    String tipo
) {
}
