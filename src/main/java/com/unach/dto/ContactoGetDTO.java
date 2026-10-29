package com.unach.dto;

import java.time.LocalDate;
import java.util.List;

public record ContactoGetDTO(
    Integer id,
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
    List<DireccionGetDTO> direcciones,
    List<NoTelefonicoGetDTO> noTelefonicos,
    List<CorreoElectronicoGetDTO> correosElectronicos,
    List<EtiquetaGetDTO> etiquetas
) {
}
