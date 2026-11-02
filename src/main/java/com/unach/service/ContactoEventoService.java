package com.unach.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.ContactoEventoGetDTO;
import com.unach.dto.ContactoEventoPostDTO;
import com.unach.entity.Contacto;
import com.unach.entity.ContactoEvento;
import com.unach.entity.ContactoEventoId;
import com.unach.entity.Evento;
import com.unach.repository.ContactoEventoRepository;

@Service
public class ContactoEventoService {

    private final ContactoEventoRepository contactoEventoRepository;
    private final ContactoService contactoService;

    public ContactoEventoService(
            ContactoEventoRepository contactoEventoRepository,
            ContactoService contactoService) {
        this.contactoEventoRepository = contactoEventoRepository;
        this.contactoService = contactoService;
    }

    @Transactional
    public List<ContactoEvento> guardarContactosEvento(Evento evento, List<ContactoEventoPostDTO> contactosEventosDto) {
        if (evento == null || evento.getIdActividad() == null) {
            throw new IllegalArgumentException("El evento debe existir antes de asociar contactos.");
        }
        if (contactosEventosDto == null || contactosEventosDto.isEmpty()) {
            return List.of();
        }

        List<ContactoEvento> contactosEventos = new ArrayList<>();
        for (ContactoEventoPostDTO ceDto : contactosEventosDto) {
            if (ceDto == null || ceDto.idContacto() == null) {
                throw new IllegalArgumentException("El id del contacto participante en el evento es obligatorio.");
            }
            if (ceDto.notaParticipacion() != null && ceDto.notaParticipacion().length() > 255) {
                throw new IllegalArgumentException("La nota de participación no puede exceder 255 caracteres.");
            }

            Contacto contacto = contactoService.obtenerEntidadPorId(ceDto.idContacto());

            ContactoEvento contactoEvento = new ContactoEvento();
            contactoEvento.setId(new ContactoEventoId(contacto.getId(), evento.getIdActividad()));
            contactoEvento.setContacto(contacto);
            contactoEvento.setEvento(evento);
            contactoEvento.setNotaParticipacion(ceDto.notaParticipacion());

            contactosEventos.add(contactoEvento);
        }

        List<ContactoEvento> guardados = contactoEventoRepository.saveAll(contactosEventos);
        evento.getContactoEventos().addAll(guardados);
        return guardados;
    }

    public List<ContactoEventoGetDTO> mapearADTOList(List<ContactoEvento> contactosEventos) {
        if (contactosEventos == null || contactosEventos.isEmpty()) {
            return List.of();
        }
        return contactosEventos.stream()
                .map(ce -> {
                    Contacto c = ce.getContacto();
                    return new ContactoEventoGetDTO(
                            c.getId(),
                            c.getNombre(),
                            c.getApellidoPaterno(),
                            c.getAvatar(),
                            ce.getNotaParticipacion()
                    );
                })
                .toList();
    }
}
