package com.unach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unach.entity.CorreoElectronico;
import com.unach.entity.CorreoElectronicoId;

@Repository
public interface CorreoElectronicoRepository extends JpaRepository<CorreoElectronico, CorreoElectronicoId> {
}
