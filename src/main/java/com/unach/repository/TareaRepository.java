package com.unach.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.SubtareaGetDTO;
import com.unach.dto.TareaGetResumenDTO;
import com.unach.entity.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    @Query("""
        SELECT new com.unach.dto.TareaGetResumenDTO(
            t.idActividad, a.titulo, a.descripcion, a.hexColor,
            a.fechaInicio, a.fechaFin, t.fechaLimite, t.estado, t.prioridad
        )
        FROM Tarea t
        JOIN t.actividad a
        ORDER BY t.idActividad ASC
    """)
    List<TareaGetResumenDTO> obtenerPrimerasResumen(Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.TareaGetResumenDTO(
            t.idActividad, a.titulo, a.descripcion, a.hexColor,
            a.fechaInicio, a.fechaFin, t.fechaLimite, t.estado, t.prioridad
        )
        FROM Tarea t
        JOIN t.actividad a
        WHERE t.idActividad > :ultimoId
        ORDER BY t.idActividad ASC
    """)
    List<TareaGetResumenDTO> obtenerResumenDespuesDeId(@Param("ultimoId") Integer ultimoId, Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.SubtareaGetDTO(
            t.idActividad, a.titulo, t.estado, t.prioridad, t.fechaLimite
        )
        FROM Tarea t
        JOIN t.actividad a
        WHERE t.tareaPadre.idActividad = :idTareaPadre
        ORDER BY t.idActividad ASC
    """)
    List<SubtareaGetDTO> obtenerSubtareasDirectas(@Param("idTareaPadre") Integer idTareaPadre);
}
