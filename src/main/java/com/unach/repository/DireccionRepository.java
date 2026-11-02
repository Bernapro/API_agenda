package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.Direccion;
import com.unach.entity.DireccionId;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, DireccionId> {
}
