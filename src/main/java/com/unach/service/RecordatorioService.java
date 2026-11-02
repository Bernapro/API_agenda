package com.unach.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.RecordatorioGetDTO;
import com.unach.dto.RecordatorioPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Recordatorio;
import com.unach.entity.RecordatorioId;
import com.unach.repository.RecordatorioRepository;

@Service
public class RecordatorioService {

    private final RecordatorioRepository recordatorioRepository;

    public RecordatorioService(RecordatorioRepository recordatorioRepository) {
        this.recordatorioRepository = recordatorioRepository;
    }

    @Transactional
    public List<Recordatorio> guardarRecordatorios(Actividad actividad, List<RecordatorioPostDTO> recordatoriosDto) {
        if (actividad == null || actividad.getId() == null) {
            throw new IllegalArgumentException("La actividad debe existir antes de registrar sus recordatorios.");
        }
        if (recordatoriosDto == null || recordatoriosDto.isEmpty()) {
            return List.of();
        }

        List<Recordatorio> recordatorios = new ArrayList<>();
        short disc = (short) (actividad.getRecordatorios().size() + 1);

        for (RecordatorioPostDTO dto : recordatoriosDto) {
            if (dto == null) {
                throw new IllegalArgumentException("El recordatorio no puede ser nulo.");
            }
            if (dto.tiempoAnticipacion() == null || dto.tiempoAnticipacion() < 0) {
                throw new IllegalArgumentException("El tiempo de anticipación del recordatorio es obligatorio y no puede ser negativo.");
            }
            if (dto.unidadTiempo() == null) {
                throw new IllegalArgumentException("La unidad de tiempo del recordatorio es obligatoria.");
            }

            Recordatorio recordatorio = new Recordatorio();
            recordatorio.setId(new RecordatorioId(actividad.getId(), disc++));
            recordatorio.setActividad(actividad);
            recordatorio.setTiempoAnticipacion(dto.tiempoAnticipacion());
            recordatorio.setUnidadTiempo(dto.unidadTiempo());

            recordatorios.add(recordatorio);
        }

        List<Recordatorio> guardados = recordatorioRepository.saveAll(recordatorios);
        actividad.getRecordatorios().addAll(guardados);
        return guardados;
    }

    public List<RecordatorioGetDTO> mapearADTOList(List<Recordatorio> recordatorios) {
        if (recordatorios == null || recordatorios.isEmpty()) {
            return List.of();
        }
        return recordatorios.stream()
                .map(r -> new RecordatorioGetDTO(
                        r.getId() != null ? r.getId().getDisc() : null,
                        r.getTiempoAnticipacion(),
                        r.getUnidadTiempo()
                ))
                .toList();
    }
}
