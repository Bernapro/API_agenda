package com.unach.dto;

import java.time.LocalDate;
import java.util.List;

public record ContactoPostDTO(
    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    String apodo,
    LocalDate cumpleanos,
    String avatar,
    String notas,
    Boolean favorito,
    String empresa,
    String departamento,
    String puesto,
    List<DireccionPostDTO> direcciones,
    List<NoTelefonicoPostDTO> noTelefonicos,
    List<CorreoElectronicoPostDTO> correosElectronicos,
    List<Integer> idEtiquetas
) {
}
