package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.Direccion;
import com.unach.entity.DireccionId;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, DireccionId> {

    @Modifying
    @Query("DELETE FROM Direccion d WHERE d.id.idContacto = :idContacto")
    void eliminarPorIdContacto(@Param("idContacto") Integer idContacto);
}
