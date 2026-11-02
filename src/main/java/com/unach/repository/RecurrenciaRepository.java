package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.Recurrencia;

@Repository
public interface RecurrenciaRepository extends JpaRepository<Recurrencia, Integer> {
}
