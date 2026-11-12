package com.unach.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.CorreoElectronicoGetDTO;
import com.unach.dto.CorreoElectronicoPostDTO;
import com.unach.entity.Contacto;
import com.unach.entity.CorreoElectronico;
import com.unach.entity.CorreoElectronicoId;
import com.unach.repository.CorreoElectronicoRepository;

@Service
public class CorreoElectronicoService {

    private final CorreoElectronicoRepository correoElectronicoRepository;

    public CorreoElectronicoService(CorreoElectronicoRepository correoElectronicoRepository) {
        this.correoElectronicoRepository = correoElectronicoRepository;
    }

    @Transactional
    public List<CorreoElectronico> guardarCorreosElectronicos(Contacto contacto, List<CorreoElectronicoPostDTO> correosDto) {
        if (contacto == null || contacto.getId() == null) {
            throw new IllegalArgumentException("El contacto debe existir antes de registrar sus correos electrónicos.");
        }
        if (correosDto == null || correosDto.isEmpty()) {
            return List.of();
        }

        List<CorreoElectronico> correos = new ArrayList<>();
        short disc = 1;

        for (CorreoElectronicoPostDTO dto : correosDto) {
            if (dto == null) {
                throw new IllegalArgumentException("Los datos del correo electrónico no pueden ser nulos.");
            }
            if (dto.direccion() == null || dto.direccion().isBlank() || !dto.direccion().contains("@")) {
                throw new IllegalArgumentException("La dirección de correo electrónico es obligatoria y debe ser válida.");
            }
            if (dto.tipo() == null || dto.tipo().isBlank()) {
                throw new IllegalArgumentException("El tipo de correo electrónico es obligatorio.");
            }

            CorreoElectronico correo = new CorreoElectronico();
            correo.setId(new CorreoElectronicoId(contacto.getId(), disc++));
            correo.setContacto(contacto);
            correo.setDireccion(dto.direccion().trim());
            correo.setTipo(dto.tipo().trim());

            correos.add(correo);
        }

        return correoElectronicoRepository.saveAll(correos);
    }

    @Transactional
    public List<CorreoElectronico> reemplazarCorreosElectronicos(Contacto contacto, List<CorreoElectronicoPostDTO> correosDto) {
        if (contacto == null || contacto.getId() == null) {
            throw new IllegalArgumentException("El contacto debe existir antes de actualizar sus correos electrónicos.");
        }
        correoElectronicoRepository.eliminarPorIdContacto(contacto.getId());
        correoElectronicoRepository.flush();
        contacto.getCorreosElectronicos().clear();
        List<CorreoElectronico> nuevos = guardarCorreosElectronicos(contacto, correosDto);
        contacto.getCorreosElectronicos().addAll(nuevos);
        return nuevos;
    }

    public List<CorreoElectronicoGetDTO> mapearADTOList(List<CorreoElectronico> correos) {
        if (correos == null || correos.isEmpty()) {
            return List.of();
        }
        return correos.stream()
                .map(c -> new CorreoElectronicoGetDTO(
                        c.getId() != null ? c.getId().getDisc() : null,
                        c.getDireccion(),
                        c.getTipo()
                ))
                .toList();
    }
}
