package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.entity.ContactoEvento;
import com.unach.entity.ContactoEventoId;

@Repository
public interface ContactoEventoRepository extends JpaRepository<ContactoEvento, ContactoEventoId> {

    @Modifying
    @Query("DELETE FROM ContactoEvento ce WHERE ce.id.idEvento = :idEvento")
    void eliminarPorIdEvento(@Param("idEvento") Integer idEvento);
}
