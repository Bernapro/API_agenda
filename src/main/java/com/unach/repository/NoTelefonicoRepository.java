package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.NoTelefonico;
import com.unach.entity.NoTelefonicoId;

@Repository
public interface NoTelefonicoRepository extends JpaRepository<NoTelefonico, NoTelefonicoId> {
}
