package com.unach.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

public record TareaGetDTO(
    Integer idActividad,
    String titulo,
    String descripcion,
    String hexColor,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    LocalDateTime fechaLimite,
    EstadoTareaEnum estado,
    PrioridadTareaEnum prioridad,
    Integer idTareaPadre,
    String tituloTareaPadre,
    RecurrenciaGetDTO recurrencia,
    List<RecordatorioGetDTO> recordatorios,
    List<SubtareaGetDTO> subtareas,
    List<SesionGetDTO> sesiones,
    List<EtiquetaGetDTO> etiquetas,
    List<NotaGetDTO> notas
) {
}
