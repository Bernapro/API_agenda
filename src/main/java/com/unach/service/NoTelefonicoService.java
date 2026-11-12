package com.unach.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.NoTelefonicoGetDTO;
import com.unach.dto.NoTelefonicoPostDTO;
import com.unach.entity.Contacto;
import com.unach.entity.NoTelefonico;
import com.unach.entity.NoTelefonicoId;
import com.unach.repository.NoTelefonicoRepository;

@Service
public class NoTelefonicoService {

    private final NoTelefonicoRepository noTelefonicoRepository;

    public NoTelefonicoService(NoTelefonicoRepository noTelefonicoRepository) {
        this.noTelefonicoRepository = noTelefonicoRepository;
    }

    @Transactional
    public List<NoTelefonico> guardarNumerosTelefonicos(Contacto contacto, List<NoTelefonicoPostDTO> telefonosDto) {
        if (contacto == null || contacto.getId() == null) {
            throw new IllegalArgumentException("El contacto debe existir antes de registrar sus números telefónicos.");
        }
        if (telefonosDto == null || telefonosDto.isEmpty()) {
            return List.of();
        }

        List<NoTelefonico> telefonos = new ArrayList<>();
        short disc = 1;

        for (NoTelefonicoPostDTO dto : telefonosDto) {
            if (dto == null) {
                throw new IllegalArgumentException("Los datos del número telefónico no pueden ser nulos.");
            }
            if (dto.noTelefonico() == null || dto.noTelefonico().isBlank()) {
                throw new IllegalArgumentException("El número telefónico es obligatorio.");
            }
            if (dto.tipo() == null || dto.tipo().isBlank()) {
                throw new IllegalArgumentException("El tipo de número telefónico es obligatorio.");
            }

            NoTelefonico telefono = new NoTelefonico();
            telefono.setId(new NoTelefonicoId(contacto.getId(), disc++));
            telefono.setContacto(contacto);
            telefono.setPrefijo((dto.prefijo() != null && !dto.prefijo().isBlank()) ? dto.prefijo().trim() : "+52");
            telefono.setNoTelefonico(dto.noTelefonico().trim());
            telefono.setTipo(dto.tipo().trim());

            telefonos.add(telefono);
        }

        return noTelefonicoRepository.saveAll(telefonos);
    }

    @Transactional
    public List<NoTelefonico> reemplazarNumerosTelefonicos(Contacto contacto, List<NoTelefonicoPostDTO> telefonosDto) {
        if (contacto == null || contacto.getId() == null) {
            throw new IllegalArgumentException("El contacto debe existir antes de actualizar sus números telefónicos.");
        }
        noTelefonicoRepository.eliminarPorIdContacto(contacto.getId());
        noTelefonicoRepository.flush();
        contacto.getNumerosTelefonicos().clear();
        List<NoTelefonico> nuevos = guardarNumerosTelefonicos(contacto, telefonosDto);
        contacto.getNumerosTelefonicos().addAll(nuevos);
        return nuevos;
    }

    public List<NoTelefonicoGetDTO> mapearADTOList(List<NoTelefonico> telefonos) {
        if (telefonos == null || telefonos.isEmpty()) {
            return List.of();
        }
        return telefonos.stream()
                .map(t -> new NoTelefonicoGetDTO(
                        t.getId() != null ? t.getId().getDisc() : null,
                        t.getPrefijo(),
                        t.getNoTelefonico(),
                        t.getTipo()
                ))
                .toList();
    }
}
