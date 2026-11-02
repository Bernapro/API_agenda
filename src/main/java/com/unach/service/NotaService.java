package com.unach.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.NotaGetDTO;
import com.unach.dto.NotaPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Nota;
import com.unach.repository.NotaRepository;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    @Transactional
    public List<Nota> guardarNotas(Actividad actividad, List<NotaPostDTO> notasDto) {
        if (actividad == null || actividad.getId() == null) {
            throw new IllegalArgumentException("La actividad debe existir antes de registrar sus notas.");
        }
        if (notasDto == null || notasDto.isEmpty()) {
            return List.of();
        }

        List<Nota> notas = new ArrayList<>();
        for (NotaPostDTO notaDto : notasDto) {
            if (notaDto == null || notaDto.cuerpo() == null || notaDto.cuerpo().isBlank()) {
                throw new IllegalArgumentException("El cuerpo de la nota es obligatorio.");
            }
            if (notaDto.nombre() != null && notaDto.nombre().length() > 150) {
                throw new IllegalArgumentException("El nombre de la nota no puede exceder 150 caracteres.");
            }

            Nota nota = new Nota();
            nota.setNombre(notaDto.nombre() != null ? notaDto.nombre().trim() : null);
            nota.setCuerpo(notaDto.cuerpo().trim());
            nota.setFechaCreacion(LocalDateTime.now());
            nota.setActividad(actividad);
            notas.add(nota);
        }

        List<Nota> guardadas = notaRepository.saveAll(notas);
        actividad.getNotas().addAll(guardadas);
        return guardadas;
    }

    public List<NotaGetDTO> mapearADTOList(List<Nota> notas, Actividad actividad) {
        if (notas == null || notas.isEmpty()) {
            return List.of();
        }
        return notas.stream()
                .map(n -> new NotaGetDTO(
                        n.getId(),
                        n.getNombre(),
                        n.getCuerpo(),
                        n.getFechaCreacion(),
                        actividad != null ? actividad.getId() : null,
                        actividad != null ? actividad.getTitulo() : null,
                        actividad != null ? actividad.getHexColor() : null
                ))
                .toList();
    }
}
