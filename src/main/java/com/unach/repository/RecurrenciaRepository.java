package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.Recurrencia;

@Repository
public interface RecurrenciaRepository extends JpaRepository<Recurrencia, Integer> {

    @Modifying
    @Query("DELETE FROM Recurrencia r WHERE r.idActividad = :idActividad")
    void eliminarPorIdActividad(@Param("idActividad") Integer idActividad);
}
