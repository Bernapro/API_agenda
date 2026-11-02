package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.Recordatorio;
import com.unach.entity.RecordatorioId;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, RecordatorioId> {
}
