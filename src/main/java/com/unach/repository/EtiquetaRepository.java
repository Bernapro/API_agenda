package com.unach.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.EtiquetaGetDTO;
import com.unach.entity.Etiqueta;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Integer> {

    Optional<Etiqueta> findByNombreIgnoreCase(String nombre);

    @Query("""
        SELECT new com.unach.dto.EtiquetaGetDTO(e.id, e.nombre, e.hexColor)
        FROM Etiqueta e
        ORDER BY e.id ASC
    """)
    List<EtiquetaGetDTO> obtenerPrimerasEtiquetas(Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.EtiquetaGetDTO(e.id, e.nombre, e.hexColor)
        FROM Etiqueta e
        WHERE e.id > :ultimoId
        ORDER BY e.id ASC
    """)
    List<EtiquetaGetDTO> obtenerEtiquetasDespuesDeId(@Param("ultimoId") Integer ultimoId, Pageable pageable);
}
