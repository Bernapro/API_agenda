package com.unach.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.SubtareaGetDTO;
import com.unach.entity.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    @Query("""
        SELECT t
        FROM Tarea t
        JOIN FETCH t.actividad a
        WHERE a.fechaInicio <= :fechaFin
          AND COALESCE(a.fechaFin, a.fechaInicio) >= :fechaInicio
        ORDER BY t.idActividad ASC
    """)
    List<Tarea> obtenerPrimerasResumen(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );

    @Query("""
        SELECT t
        FROM Tarea t
        JOIN FETCH t.actividad a
        WHERE t.idActividad > :ultimoId
          AND a.fechaInicio <= :fechaFin
          AND COALESCE(a.fechaFin, a.fechaInicio) >= :fechaInicio
        ORDER BY t.idActividad ASC
    """)
    List<Tarea> obtenerResumenDespuesDeId(
            @Param("ultimoId") Integer ultimoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );

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
