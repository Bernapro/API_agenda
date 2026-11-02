package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {
}
