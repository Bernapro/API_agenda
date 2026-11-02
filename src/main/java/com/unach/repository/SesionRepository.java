package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.Sesion;
import com.unach.entity.SesionId;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, SesionId> {

    @Query("""
        SELECT COALESCE(MAX(s.id.disc), 0)
        FROM Sesion s
        WHERE s.id.idTarea = :idTarea
    """)
    Short obtenerMaxDiscPorTarea(@Param("idTarea") Integer idTarea);
}
