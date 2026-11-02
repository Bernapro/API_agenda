package com.unach.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unach.dto.ContactoGetResumenDTO;
import com.unach.entity.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> obtenerPrimerosResumen(Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        WHERE c.id > :ultimoId
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> obtenerResumenDespuesDeId(@Param("ultimoId") Integer ultimoId, Pageable pageable);

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :cadena, '%'))
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> buscarPorNombre(@Param("cadena") String cadena);

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        WHERE LOWER(c.apellidoPaterno) LIKE LOWER(CONCAT('%', :cadena, '%'))
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> buscarPorApellidoPaterno(@Param("cadena") String cadena);

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        WHERE LOWER(c.apellidoMaterno) LIKE LOWER(CONCAT('%', :cadena, '%'))
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> buscarPorApellidoMaterno(@Param("cadena") String cadena);

    @Query("""
        SELECT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        WHERE LOWER(c.apodo) LIKE LOWER(CONCAT('%', :cadena, '%'))
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> buscarPorApodo(@Param("cadena") String cadena);

    @Query("""
        SELECT DISTINCT new com.unach.dto.ContactoGetResumenDTO(
            c.id, c.nombre, c.apellidoPaterno, c.apellidoMaterno, c.apodo, c.avatar, c.favorito
        )
        FROM Contacto c
        JOIN c.etiquetas e
        WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :etiqueta, '%'))
        ORDER BY c.id ASC
    """)
    List<ContactoGetResumenDTO> buscarPorEtiqueta(@Param("etiqueta") String etiqueta);
}
