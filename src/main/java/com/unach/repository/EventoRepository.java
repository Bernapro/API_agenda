package com.unach.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.EventoGetResumenDTO;
import com.unach.entity.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    @Query("""
        SELECT new com.unach.dto.EventoGetResumenDTO(
            e.idActividad, a.titulo, a.hexColor, e.todoElDia
        )
        FROM Evento e
        JOIN e.actividad a
        ORDER BY e.idActividad ASC
    """)
    List<EventoGetResumenDTO> obtenerPrimerosResumen(Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.EventoGetResumenDTO(
            e.idActividad, a.titulo, a.hexColor, e.todoElDia
        )
        FROM Evento e
        JOIN e.actividad a
        WHERE e.idActividad > :ultimoId
        ORDER BY e.idActividad ASC
    """)
    List<EventoGetResumenDTO> obtenerResumenDespuesDeId(@Param("ultimoId") Integer ultimoId, Pageable pageable);
}
