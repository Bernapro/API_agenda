package com.unach.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.ActividadGetResumenDTO;
import com.unach.entity.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Integer> {

    @Query("""
        SELECT DISTINCT new com.unach.dto.ActividadGetResumenDTO(
            a.id, a.titulo, a.hexColor, a.fechaInicio, a.fechaFin, a.tipoActividad
        )
        FROM Actividad a
        JOIN a.etiquetas e
        WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :etiqueta, '%'))
        ORDER BY a.id ASC
    """)
    List<ActividadGetResumenDTO> buscarPorEtiqueta(@Param("etiqueta") String etiqueta);

    @Query("""
        SELECT new com.unach.dto.ActividadGetResumenDTO(
            a.id, a.titulo, a.hexColor, a.fechaInicio, a.fechaFin, a.tipoActividad
        )
        FROM Actividad a
        WHERE a.fechaInicio <= :fechaFin
          AND COALESCE(a.fechaFin, a.fechaInicio) >= :fechaInicio
        ORDER BY a.id ASC
    """)
    List<ActividadGetResumenDTO> obtenerPrimerasResumen(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );

    @Query("""
        SELECT new com.unach.dto.ActividadGetResumenDTO(
            a.id, a.titulo, a.hexColor, a.fechaInicio, a.fechaFin, a.tipoActividad
        )
        FROM Actividad a
        WHERE a.id > :ultimoId
          AND a.fechaInicio <= :fechaFin
          AND COALESCE(a.fechaFin, a.fechaInicio) >= :fechaInicio
        ORDER BY a.id ASC
    """)
    List<ActividadGetResumenDTO> obtenerResumenDespuesDeId(
            @Param("ultimoId") Integer ultimoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );

    @Query("""
        SELECT new com.unach.dto.ActividadGetResumenDTO(
            a.id, a.titulo, a.hexColor, a.fechaInicio, a.fechaFin, a.tipoActividad
        )
        FROM Actividad a
        WHERE LOWER(a.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
        ORDER BY a.id ASC
    """)
    List<ActividadGetResumenDTO> buscarPorTitulo(@Param("titulo") String titulo);

    @Query("""
        SELECT new com.unach.dto.ActividadGetResumenDTO(
            a.id, a.titulo, a.hexColor, a.fechaInicio, a.fechaFin, a.tipoActividad
        )
        FROM Actividad a
        WHERE a.fechaInicio <= :fechaFin
          AND COALESCE(a.fechaFin, a.fechaInicio) >= :fechaInicio
        ORDER BY a.fechaInicio ASC, a.id ASC
    """)
    List<ActividadGetResumenDTO> obtenerPorRangoFechas(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
