package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.NoTelefonico;
import com.unach.entity.NoTelefonicoId;

@Repository
public interface NoTelefonicoRepository extends JpaRepository<NoTelefonico, NoTelefonicoId> {

    @Modifying
    @Query("DELETE FROM NoTelefonico t WHERE t.id.idContacto = :idContacto")
    void eliminarPorIdContacto(@Param("idContacto") Integer idContacto);
}
