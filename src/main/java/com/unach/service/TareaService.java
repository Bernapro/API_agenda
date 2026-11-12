package com.unach.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.SesionGetDTO;
import com.unach.dto.SesionPostDTO;
import com.unach.dto.SubtareaGetDTO;
import com.unach.dto.TareaGetDTO;
import com.unach.dto.TareaGetResumenDTO;
import com.unach.dto.TareaPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Tarea;
import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;
import com.unach.enums.TipoActividadEnum;
import com.unach.errorHandling.Exception.ResourceNotFoundException;
import com.unach.repository.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ActividadService actividadService;
    private final RecurrenciaService recurrenciaService;
    private final RecordatorioService recordatorioService;
    private final SesionService sesionService;
    private final EtiquetaService etiquetaService;
    private final NotaService notaService;

    public TareaService(
            TareaRepository tareaRepository,
            ActividadService actividadService,
            RecurrenciaService recurrenciaService,
            RecordatorioService recordatorioService,
            SesionService sesionService,
            EtiquetaService etiquetaService,
            NotaService notaService) {
        this.tareaRepository = tareaRepository;
        this.actividadService = actividadService;
        this.recurrenciaService = recurrenciaService;
        this.recordatorioService = recordatorioService;
        this.sesionService = sesionService;
        this.etiquetaService = etiquetaService;
        this.notaService = notaService;
    }

    @Transactional
    public TareaGetDTO altaTarea(TareaPostDTO dto) {
        if (dto == null || dto.actividad() == null) {
            throw new IllegalArgumentException("Los datos de la tarea y su actividad son obligatorios.");
        }
        if (dto.actividad().tipoActividad() != null && dto.actividad().tipoActividad() != TipoActividadEnum.TAREA) {
            throw new IllegalArgumentException("El tipo de actividad para una tarea debe ser TAREA.");
        }
        if (dto.fechaLimite() != null && dto.actividad().fechaInicio() != null
                && dto.fechaLimite().isBefore(dto.actividad().fechaInicio())) {
            throw new IllegalArgumentException("La fecha límite de la tarea no puede ser anterior a la fecha de inicio de la actividad.");
        }

        Tarea tareaPadre = null;
        if (dto.idTareaPadre() != null) {
            tareaPadre = obtenerEntidadPorId(dto.idTareaPadre());
        }

        Actividad actividad = actividadService.crearActividad(dto.actividad(), TipoActividadEnum.TAREA);

        Tarea tarea = new Tarea();
        tarea.setActividad(actividad);
        tarea.setFechaLimite(dto.fechaLimite());
        tarea.setEstado(dto.estado() != null ? dto.estado() : EstadoTareaEnum.PENDIENTE);
        tarea.setPrioridad(dto.prioridad() != null ? dto.prioridad() : PrioridadTareaEnum.MEDIA);
        tarea.setTareaPadre(tareaPadre);

        Tarea tareaGuardada = tareaRepository.save(tarea);
        actividad.setTarea(tareaGuardada);

        return mapearADTO(tareaGuardada);
    }

    @Transactional(readOnly = true)
    public List<TareaGetResumenDTO> obtenerResumenTarea(
            Integer idRecibido,
            Integer limit,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        int ultimoId = 0;
        if (idRecibido != null) {
            if (idRecibido < 0) {
                throw new IllegalArgumentException("El id del cursor no puede ser negativo.");
            }
            ultimoId = ultimoId + idRecibido;
        }

        int limite = (limit == null) ? 10 : limit;
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0.");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias para filtrar el resumen de tareas.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        Pageable pageable = PageRequest.of(0, limite);
        List<Tarea> tareas = (ultimoId == 0)
                ? tareaRepository.obtenerPrimerasResumen(fechaInicio, fechaFin, pageable)
                : tareaRepository.obtenerResumenDespuesDeId(ultimoId, fechaInicio, fechaFin, pageable);

        return tareas.stream()
                .map(this::mapearAResumenDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Tarea obtenerEntidadPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id de la tarea debe ser un entero positivo.");
        }
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la tarea con id: " + id));
    }

    @Transactional(readOnly = true)
    public TareaGetDTO obtenerTarea(Integer id) {
        return mapearADTO(obtenerEntidadPorId(id));
    }

    @Transactional
    public TareaGetDTO actualizarTarea(Integer id, TareaPostDTO dto) {
        if (dto == null || dto.actividad() == null) {
            throw new IllegalArgumentException("Los datos de la tarea y su actividad son obligatorios.");
        }
        if (dto.actividad().tipoActividad() != null && dto.actividad().tipoActividad() != TipoActividadEnum.TAREA) {
            throw new IllegalArgumentException("El tipo de actividad para una tarea debe ser TAREA.");
        }
        if (dto.fechaLimite() != null && dto.actividad().fechaInicio() != null
                && dto.fechaLimite().isBefore(dto.actividad().fechaInicio())) {
            throw new IllegalArgumentException("La fecha límite de la tarea no puede ser anterior a la fecha de inicio de la actividad.");
        }

        Tarea tarea = obtenerEntidadPorId(id);

        Tarea tareaPadre = null;
        if (dto.idTareaPadre() != null) {
            tareaPadre = obtenerEntidadPorId(dto.idTareaPadre());
        }

        actividadService.actualizarActividad(tarea.getActividad(), dto.actividad(), TipoActividadEnum.TAREA);

        tarea.setFechaLimite(dto.fechaLimite());
        tarea.setEstado(dto.estado() != null ? dto.estado() : EstadoTareaEnum.PENDIENTE);
        tarea.setPrioridad(dto.prioridad() != null ? dto.prioridad() : PrioridadTareaEnum.MEDIA);
        tarea.setTareaPadre(tareaPadre);

        return mapearADTO(tareaRepository.save(tarea));
    }

    @Transactional(readOnly = true)
    public List<SubtareaGetDTO> obtenerSubtareas(Integer idTareaPadre) {
        if (idTareaPadre == null || idTareaPadre <= 0) {
            throw new IllegalArgumentException("El id de la tarea padre debe ser un entero positivo.");
        }
        if (!tareaRepository.existsById(idTareaPadre)) {
            throw new ResourceNotFoundException("No se encontró la tarea padre con id: " + idTareaPadre);
        }

        List<SubtareaGetDTO> subtareas = tareaRepository.obtenerSubtareasDirectas(idTareaPadre);
        if (subtareas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron subtareas para la tarea con id: " + idTareaPadre);
        }
        return subtareas;
    }

    @Transactional
    public SesionGetDTO registrarSesion(SesionPostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la sesión no pueden ser nulos.");
        }
        if (dto.idActividad() == null || dto.idActividad() <= 0) {
            throw new IllegalArgumentException("El id de la tarea (idActividad) es obligatorio y debe ser positivo.");
        }

        Tarea tarea = obtenerEntidadPorId(dto.idActividad());
        return sesionService.registrarSesion(tarea, dto);
    }

    private TareaGetResumenDTO mapearAResumenDTO(Tarea tarea) {
        Actividad actividad = tarea.getActividad();
        return new TareaGetResumenDTO(
                tarea.getIdActividad(),
                actividad.getTitulo(),
                actividad.getHexColor(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                tarea.getFechaLimite(),
                tarea.getEstado(),
                tarea.getPrioridad(),
                etiquetaService.mapearADTOList(actividad.getEtiquetas())
        );
    }

    private TareaGetDTO mapearADTO(Tarea tarea) {
        Actividad actividad = tarea.getActividad();
        Tarea padre = tarea.getTareaPadre();

        List<SubtareaGetDTO> subtareasDto = tarea.getSubtareas().stream()
                .map(st -> new SubtareaGetDTO(
                        st.getIdActividad(),
                        st.getActividad() != null ? st.getActividad().getTitulo() : null,
                        st.getEstado(),
                        st.getPrioridad(),
                        st.getFechaLimite()
                ))
                .toList();

        return new TareaGetDTO(
                tarea.getIdActividad(),
                actividad.getTitulo(),
                actividad.getDescripcion(),
                actividad.getHexColor(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                tarea.getFechaLimite(),
                tarea.getEstado(),
                tarea.getPrioridad(),
                padre != null ? padre.getIdActividad() : null,
                (padre != null && padre.getActividad() != null) ? padre.getActividad().getTitulo() : null,
                recurrenciaService.mapearADTO(actividad.getRecurrencia()),
                recordatorioService.mapearADTOList(actividad.getRecordatorios()),
                subtareasDto,
                sesionService.mapearADTOList(tarea.getSesiones()),
                etiquetaService.mapearADTOList(actividad.getEtiquetas()),
                notaService.mapearADTOList(actividad.getNotas(), actividad)
        );
    }
}
