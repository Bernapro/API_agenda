package com.unach.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.RecurrenciaGetDTO;
import com.unach.dto.RecurrenciaPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Recurrencia;
import com.unach.repository.RecurrenciaRepository;

@Service
public class RecurrenciaService {

    private final RecurrenciaRepository recurrenciaRepository;

    public RecurrenciaService(RecurrenciaRepository recurrenciaRepository) {
        this.recurrenciaRepository = recurrenciaRepository;
    }

    @Transactional
    public Recurrencia guardarRecurrencia(Actividad actividad, RecurrenciaPostDTO dto) {
        if (actividad == null || actividad.getId() == null) {
            throw new IllegalArgumentException("La actividad debe existir antes de registrar su recurrencia.");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de recurrencia no pueden ser nulos.");
        }
        if (dto.frecuencia() == null) {
            throw new IllegalArgumentException("La frecuencia de la recurrencia es obligatoria.");
        }
        if (dto.fechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio de la recurrencia es obligatoria.");
        }
        if (dto.intervalo() != null && dto.intervalo() <= 0) {
            throw new IllegalArgumentException("El intervalo de recurrencia debe ser mayor a 0.");
        }
        if (dto.fechaFin() != null && dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin de la recurrencia no puede ser anterior a la fecha de inicio.");
        }

        Recurrencia recurrencia = new Recurrencia();
        recurrencia.setActividad(actividad);
        recurrencia.setFrecuencia(dto.frecuencia());
        recurrencia.setIntervalo(dto.intervalo() != null ? dto.intervalo() : (short) 1);
        recurrencia.setDiasSemana(dto.diasSemana());
        recurrencia.setDiaMes(dto.diaMes());
        recurrencia.setMesAnio(dto.mesAnio());
        recurrencia.setFechaInicio(dto.fechaInicio());
        recurrencia.setFechaFin(dto.fechaFin());

        Recurrencia guardada = recurrenciaRepository.save(recurrencia);
        actividad.setRecurrencia(guardada);
        return guardada;
    }

    public RecurrenciaGetDTO mapearADTO(Recurrencia recurrencia) {
        if (recurrencia == null) {
            return null;
        }
        return new RecurrenciaGetDTO(
                recurrencia.getFrecuencia(),
                recurrencia.getIntervalo(),
                recurrencia.getDiasSemana(),
                recurrencia.getDiaMes(),
                recurrencia.getMesAnio(),
                recurrencia.getFechaInicio(),
                recurrencia.getFechaFin()
        );
    }
}
