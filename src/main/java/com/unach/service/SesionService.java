package com.unach.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.SesionGetDTO;
import com.unach.dto.SesionPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Sesion;
import com.unach.entity.SesionId;
import com.unach.entity.Tarea;
import com.unach.enums.EstadoSesionEnum;
import com.unach.repository.SesionRepository;

@Service
public class SesionService {

    private final SesionRepository sesionRepository;

    public SesionService(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    @Transactional
    public SesionGetDTO registrarSesion(Tarea tarea, SesionPostDTO dto) {
        if (tarea == null || tarea.getIdActividad() == null) {
            throw new IllegalArgumentException("La tarea debe existir antes de registrar una sesión.");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la sesión no pueden ser nulos.");
        }
        if (dto.horaInicio() != null && dto.horaFin() != null && dto.horaFin().isBefore(dto.horaInicio())) {
            throw new IllegalArgumentException("La hora de fin de la sesión no puede ser anterior a la hora de inicio.");
        }

        LocalDate fechaProgramada = validarYObtenerFechaSesion(tarea, dto.fechaProgramada());

        short siguienteDisc = (short) (sesionRepository.obtenerMaxDiscPorTarea(tarea.getIdActividad()) + 1);

        Sesion sesion = new Sesion();
        sesion.setId(new SesionId(tarea.getIdActividad(), siguienteDisc));
        sesion.setTarea(tarea);
        sesion.setFechaProgramada(fechaProgramada);
        sesion.setHoraInicio(dto.horaInicio());
        sesion.setHoraFin(dto.horaFin());
        sesion.setEstado(dto.estado() != null ? dto.estado() : EstadoSesionEnum.PENDIENTE);
        sesion.setNotas(dto.notas());

        Sesion guardada = sesionRepository.save(sesion);
        tarea.getSesiones().add(guardada);

        return mapearADTO(guardada);
    }

    @Transactional
    public List<Sesion> guardarSesiones(Tarea tarea, List<SesionPostDTO> sesionesDto) {
        if (tarea == null || tarea.getIdActividad() == null) {
            throw new IllegalArgumentException("La tarea debe existir antes de registrar sus sesiones.");
        }
        if (sesionesDto == null || sesionesDto.isEmpty()) {
            return List.of();
        }

        List<Sesion> sesiones = new ArrayList<>();
        short disc = (short) (sesionRepository.obtenerMaxDiscPorTarea(tarea.getIdActividad()) + 1);

        for (SesionPostDTO dto : sesionesDto) {
            if (dto == null) {
                throw new IllegalArgumentException("Los datos de la sesión no pueden ser nulos.");
            }
            if (dto.horaInicio() != null && dto.horaFin() != null && dto.horaFin().isBefore(dto.horaInicio())) {
                throw new IllegalArgumentException("La hora de fin de la sesión no puede ser anterior a la hora de inicio.");
            }

            LocalDate fechaProgramada = validarYObtenerFechaSesion(tarea, dto.fechaProgramada());

            Sesion sesion = new Sesion();
            sesion.setId(new SesionId(tarea.getIdActividad(), disc++));
            sesion.setTarea(tarea);
            sesion.setFechaProgramada(fechaProgramada);
            sesion.setHoraInicio(dto.horaInicio());
            sesion.setHoraFin(dto.horaFin());
            sesion.setEstado(dto.estado() != null ? dto.estado() : EstadoSesionEnum.PENDIENTE);
            sesion.setNotas(dto.notas());

            sesiones.add(sesion);
        }

        List<Sesion> guardadas = sesionRepository.saveAll(sesiones);
        tarea.getSesiones().addAll(guardadas);
        return guardadas;
    }

    private LocalDate validarYObtenerFechaSesion(Tarea tarea, LocalDate fechaSesion) {
        Actividad actividad = tarea.getActividad();
        LocalDateTime fechaInicioAct = actividad != null ? actividad.getFechaInicio() : null;
        LocalDateTime fechaFinAct = actividad != null ? actividad.getFechaFin() : null;
        LocalDateTime fechaLimiteTarea = tarea.getFechaLimite();

        boolean esRutinaInfinita = (fechaInicioAct == null && fechaFinAct == null && fechaLimiteTarea == null);

        if (esRutinaInfinita) {
            return null;
        }

        if (fechaSesion == null) {
            throw new IllegalArgumentException("La fecha programada de la sesión es obligatoria cuando la tarea posee fechas definidas.");
        }

        if (fechaInicioAct != null && fechaSesion.isBefore(fechaInicioAct.toLocalDate())) {
            throw new IllegalArgumentException(
                    "La fecha de la sesión (" + fechaSesion + ") no puede ser anterior a la fecha de inicio de la actividad (" + fechaInicioAct.toLocalDate() + ")."
            );
        }

        if (fechaFinAct != null && fechaSesion.isAfter(fechaFinAct.toLocalDate())) {
            throw new IllegalArgumentException(
                    "La fecha de la sesión (" + fechaSesion + ") debe estar dentro del rango de la actividad y no superar la fecha fin (" + fechaFinAct.toLocalDate() + ")."
            );
        }

        if (fechaLimiteTarea != null && fechaSesion.isAfter(fechaLimiteTarea.toLocalDate())) {
            throw new IllegalArgumentException(
                    "La fecha de la sesión (" + fechaSesion + ") debe ser anterior o igual a la fecha límite de la tarea (" + fechaLimiteTarea.toLocalDate() + ")."
            );
        }

        return fechaSesion;
    }

    public SesionGetDTO mapearADTO(Sesion sesion) {
        if (sesion == null) {
            return null;
        }
        return new SesionGetDTO(
                sesion.getId() != null ? sesion.getId().getDisc() : null,
                sesion.getFechaProgramada(),
                sesion.getHoraInicio(),
                sesion.getHoraFin(),
                sesion.getEstado(),
                sesion.getNotas()
        );
    }

    public List<SesionGetDTO> mapearADTOList(List<Sesion> sesiones) {
        if (sesiones == null || sesiones.isEmpty()) {
            return List.of();
        }
        return sesiones.stream()
                .map(this::mapearADTO)
                .toList();
    }
}
