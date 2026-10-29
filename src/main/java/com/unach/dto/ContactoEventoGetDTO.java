package com.unach.dto;

public record ContactoEventoGetDTO(
    Integer idContacto,
    String nombreContacto,
    String apellidoPaternoContacto,
    String avatarContacto,
    String notaParticipacion
) {
}
