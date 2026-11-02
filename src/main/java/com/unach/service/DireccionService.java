package com.unach.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.DireccionPostDTO;
import com.unach.entity.Contacto;
import com.unach.entity.Direccion;
import com.unach.entity.DireccionId;
import com.unach.repository.DireccionRepository;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    @Transactional
    public List<Direccion> guardarDirecciones(Contacto contacto, List<DireccionPostDTO> direccionesDto) {
        if (contacto == null || contacto.getId() == null) {
            throw new IllegalArgumentException("El contacto debe existir antes de registrar sus direcciones.");
        }
        if (direccionesDto == null || direccionesDto.isEmpty()) {
            return List.of();
        }

        List<Direccion> direcciones = new ArrayList<>();
        short disc = 1;

        for (DireccionPostDTO dto : direccionesDto) {
            if (dto == null) {
                throw new IllegalArgumentException("Los datos de la dirección no pueden ser nulos.");
            }
            if (dto.direccion() == null || dto.direccion().isBlank()) {
                throw new IllegalArgumentException("La calle/dirección es obligatoria.");
            }
            if (dto.ciudad() == null || dto.ciudad().isBlank()) {
                throw new IllegalArgumentException("La ciudad es obligatoria en la dirección.");
            }
            if (dto.estado() == null || dto.estado().isBlank()) {
                throw new IllegalArgumentException("El estado es obligatorio en la dirección.");
            }
            if (dto.tipo() == null || dto.tipo().isBlank()) {
                throw new IllegalArgumentException("El tipo de dirección es obligatorio.");
            }

            Direccion direccion = new Direccion();
            direccion.setId(new DireccionId(contacto.getId(), disc++));
            direccion.setContacto(contacto);
            direccion.setDireccion(dto.direccion().trim());
            direccion.setSegundaLinea(dto.segundaLinea());
            direccion.setApartadoPostal(dto.apartadoPostal());
            direccion.setCodigoPostal(dto.codigoPostal());
            direccion.setCiudad(dto.ciudad().trim());
            direccion.setEstado(dto.estado().trim());
            direccion.setPais((dto.pais() != null && !dto.pais().isBlank()) ? dto.pais().trim() : "México");
            direccion.setTipo(dto.tipo().trim());

            direcciones.add(direccion);
        }

        return direccionRepository.saveAll(direcciones);
    }
}
