package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.Recordatorio;
import com.unach.entity.RecordatorioId;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, RecordatorioId> {

    @Modifying
    @Query("DELETE FROM Recordatorio r WHERE r.id.idActividad = :idActividad")
    void eliminarPorIdActividad(@Param("idActividad") Integer idActividad);
}
