package com.unach.dto;

public record DireccionGetDTO(
    Short disc,
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
