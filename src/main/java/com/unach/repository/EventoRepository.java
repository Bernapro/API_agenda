package com.unach.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    @Query("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.actividad a
        WHERE a.fechaInicio BETWEEN :fechaInicio AND :fechaFin
        ORDER BY e.idActividad ASC
    """)
    List<Evento> obtenerPrimerosResumen(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );

    @Query("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.actividad a
        WHERE e.idActividad > :ultimoId
          AND a.fechaInicio BETWEEN :fechaInicio AND :fechaFin
        ORDER BY e.idActividad ASC
    """)
    List<Evento> obtenerResumenDespuesDeId(
            @Param("ultimoId") Integer ultimoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable
    );
}
