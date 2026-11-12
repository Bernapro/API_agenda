package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.CorreoElectronico;
import com.unach.entity.CorreoElectronicoId;

@Repository
public interface CorreoElectronicoRepository extends JpaRepository<CorreoElectronico, CorreoElectronicoId> {

    @Modifying
    @Query("DELETE FROM CorreoElectronico c WHERE c.id.idContacto = :idContacto")
    void eliminarPorIdContacto(@Param("idContacto") Integer idContacto);
}
