package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.ContactoEvento;
import com.unach.entity.ContactoEventoId;

@Repository
public interface ContactoEventoRepository extends JpaRepository<ContactoEvento, ContactoEventoId> {
}
