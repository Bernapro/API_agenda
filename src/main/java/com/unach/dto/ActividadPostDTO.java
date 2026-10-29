package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.unach.enums.TipoActividadEnum;

public record ActividadPostDTO(
    String titulo,
    String descripcion,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    TipoActividadEnum tipoActividad,
    RecurrenciaPostDTO recurrencia,
    List<RecordatorioPostDTO> recordatorios,
    List<Integer> idEtiquetas,
    List<NotaPostDTO> notas
) {
}
