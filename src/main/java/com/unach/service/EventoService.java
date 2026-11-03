package com.unach.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.ContactoEventoGetDTO;
import com.unach.dto.EtiquetaGetDTO;
import com.unach.dto.EventoGetDTO;
import com.unach.dto.EventoGetResumenDTO;
import com.unach.dto.EventoPostDTO;
import com.unach.dto.NotaGetDTO;
import com.unach.dto.RecordatorioGetDTO;
import com.unach.dto.RecurrenciaGetDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Evento;
import com.unach.enums.TipoActividadEnum;
import com.unach.errorHandling.Exception.ResourceNotFoundException;
import com.unach.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final ActividadService actividadService;
    private final RecurrenciaService recurrenciaService;
    private final RecordatorioService recordatorioService;
    private final ContactoEventoService contactoEventoService;
    private final EtiquetaService etiquetaService;
    private final NotaService notaService;

    public EventoService(
            EventoRepository eventoRepository,
            ActividadService actividadService,
            RecurrenciaService recurrenciaService,
            RecordatorioService recordatorioService,
            ContactoEventoService contactoEventoService,
            EtiquetaService etiquetaService,
            NotaService notaService) {
        this.eventoRepository = eventoRepository;
        this.actividadService = actividadService;
        this.recurrenciaService = recurrenciaService;
        this.recordatorioService = recordatorioService;
        this.contactoEventoService = contactoEventoService;
        this.etiquetaService = etiquetaService;
        this.notaService = notaService;
    }

    @Transactional
    public EventoGetDTO altaEvento(EventoPostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del evento no pueden ser nulos.");
        }
        if (dto.actividad() == null) {
            throw new IllegalArgumentException("Los datos de la actividad son obligatorios para crear un evento.");
        }
        if (dto.actividad().tipoActividad() != null && dto.actividad().tipoActividad() != TipoActividadEnum.EVENTO) {
            throw new IllegalArgumentException("El tipo de actividad para un evento debe ser EVENTO.");
        }

        Actividad actividad = actividadService.crearActividad(dto.actividad(), TipoActividadEnum.EVENTO);

        if (dto.recordatorios() != null && !dto.recordatorios().isEmpty()
                && (dto.actividad().recordatorios() == null || dto.actividad().recordatorios().isEmpty())) {
            recordatorioService.guardarRecordatorios(actividad, dto.recordatorios());
        }

        Evento evento = new Evento();
        evento.setActividad(actividad);
        evento.setDireccion(dto.direccion());
        evento.setTodoElDia(dto.todoElDia() != null ? dto.todoElDia() : false);

        Evento eventoGuardado = eventoRepository.save(evento);
        actividad.setEvento(eventoGuardado);

        if (dto.contactosEventos() != null && !dto.contactosEventos().isEmpty()) {
            contactoEventoService.guardarContactosEvento(eventoGuardado, dto.contactosEventos());
        }

        return mapearADTO(eventoGuardado);
    }

    @Transactional(readOnly = true)
    public List<EventoGetResumenDTO> obtenerResumenEvento(
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
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias para filtrar el resumen de eventos.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        Pageable pageable = PageRequest.of(0, limite);
        List<Evento> eventos = (ultimoId == 0)
                ? eventoRepository.obtenerPrimerosResumen(fechaInicio, fechaFin, pageable)
                : eventoRepository.obtenerResumenDespuesDeId(ultimoId, fechaInicio, fechaFin, pageable);

        return eventos.stream()
                .map(this::mapearAResumenDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventoGetDTO obtenerEvento(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id del evento debe ser un entero positivo.");
        }

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el evento con id: " + id));

        return mapearADTO(evento);
    }

    private EventoGetResumenDTO mapearAResumenDTO(Evento evento) {
        Actividad actividad = evento.getActividad();
        int cantidadInvitados = evento.getContactoEventos() != null ? evento.getContactoEventos().size() : 0;

        return new EventoGetResumenDTO(
                evento.getIdActividad(),
                actividad.getTitulo(),
                actividad.getHexColor(),
                evento.getTodoElDia(),
                evento.getDireccion(),
                cantidadInvitados,
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                etiquetaService.mapearADTOList(actividad.getEtiquetas())
        );
    }

    private EventoGetDTO mapearADTO(Evento evento) {
        Actividad actividad = evento.getActividad();

        RecurrenciaGetDTO recurrenciaDto = recurrenciaService.mapearADTO(actividad.getRecurrencia());
        List<RecordatorioGetDTO> recordatoriosDto = recordatorioService.mapearADTOList(actividad.getRecordatorios());
        List<ContactoEventoGetDTO> contactosEventosDto = contactoEventoService.mapearADTOList(evento.getContactoEventos());
        List<EtiquetaGetDTO> etiquetasDto = etiquetaService.mapearADTOList(actividad.getEtiquetas());
        List<NotaGetDTO> notasDto = notaService.mapearADTOList(actividad.getNotas(), actividad);

        return new EventoGetDTO(
                evento.getIdActividad(),
                actividad.getTitulo(),
                actividad.getDescripcion(),
                actividad.getHexColor(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                evento.getDireccion(),
                evento.getTodoElDia(),
                recurrenciaDto,
                recordatoriosDto,
                contactosEventosDto,
                etiquetasDto,
                notasDto
        );
    }
}
