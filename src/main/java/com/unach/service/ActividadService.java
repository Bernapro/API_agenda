package com.unach.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.ActividadGetResumenDTO;
import com.unach.dto.ActividadPostDTO;
import com.unach.entity.Actividad;
import com.unach.entity.Etiqueta;
import com.unach.enums.TipoActividadEnum;
import com.unach.errorHandling.Exception.ResourceNotFoundException;
import com.unach.repository.ActividadRepository;
import com.unach.repository.RecordatorioRepository;
import com.unach.repository.RecurrenciaRepository;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EtiquetaService etiquetaService;
    private final RecurrenciaService recurrenciaService;
    private final RecordatorioService recordatorioService;
    private final RecurrenciaRepository recurrenciaRepository;
    private final RecordatorioRepository recordatorioRepository;
    private final NotaService notaService;

    public ActividadService(
            ActividadRepository actividadRepository,
            EtiquetaService etiquetaService,
            RecurrenciaService recurrenciaService,
            RecordatorioService recordatorioService,
            RecurrenciaRepository recurrenciaRepository,
            RecordatorioRepository recordatorioRepository,
            NotaService notaService) {
        this.actividadRepository = actividadRepository;
        this.etiquetaService = etiquetaService;
        this.recurrenciaService = recurrenciaService;
        this.recordatorioService = recordatorioService;
        this.recurrenciaRepository = recurrenciaRepository;
        this.recordatorioRepository = recordatorioRepository;
        this.notaService = notaService;
    }

    @Transactional(readOnly = true)
    public List<ActividadGetResumenDTO> buscarPorEtiqueta(String etiqueta) {
        if (etiqueta == null || etiqueta.isBlank()) {
            throw new IllegalArgumentException("La etiqueta a buscar no puede estar vacía.");
        }

        List<ActividadGetResumenDTO> resultados = actividadRepository.buscarPorEtiqueta(etiqueta.trim());
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron actividades con la etiqueta: " + etiqueta.trim());
        }
        return resultados;
    }

    @Transactional(readOnly = true)
    public List<ActividadGetResumenDTO> obtenerResumenActividad(
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
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias para filtrar el resumen de actividades.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        Pageable pageable = PageRequest.of(0, limite);
        if (ultimoId == 0) {
            return actividadRepository.obtenerPrimerasResumen(fechaInicio, fechaFin, pageable);
        }
        return actividadRepository.obtenerResumenDespuesDeId(ultimoId, fechaInicio, fechaFin, pageable);
    }

    @Transactional(readOnly = true)
    public List<ActividadGetResumenDTO> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título a buscar no puede estar vacío.");
        }

        List<ActividadGetResumenDTO> resultados = actividadRepository.buscarPorTitulo(titulo.trim());
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron actividades con el título: " + titulo.trim());
        }
        return resultados;
    }

    @Transactional(readOnly = true)
    public List<ActividadGetResumenDTO> obtenerPorMes(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria para consultar las actividades del mes.");
        }

        LocalDate primerDiaMes = fecha.withDayOfMonth(1);
        LocalDate finPrimeraSemanaSiguienteMes = primerDiaMes.plusMonths(1).withDayOfMonth(7);

        LocalDateTime fechaInicioRango = primerDiaMes.atStartOfDay();
        LocalDateTime fechaFinRango = finPrimeraSemanaSiguienteMes.atTime(LocalTime.MAX);

        List<ActividadGetResumenDTO> resultados = actividadRepository.obtenerPorRangoFechas(fechaInicioRango, fechaFinRango);
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron actividades en el rango " + primerDiaMes + " a " + finPrimeraSemanaSiguienteMes
            );
        }
        return resultados;
    }

    @Transactional
    public Actividad crearActividad(ActividadPostDTO dto, TipoActividadEnum tipoPorDefecto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la actividad son obligatorios.");
        }
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("El título de la actividad es obligatorio.");
        }
        if (dto.titulo().trim().length() > 150) {
            throw new IllegalArgumentException("El título de la actividad no puede exceder 150 caracteres.");
        }
        if (dto.fechaInicio() != null && dto.fechaFin() != null && dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        TipoActividadEnum tipo = dto.tipoActividad() != null ? dto.tipoActividad() : tipoPorDefecto;
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de actividad es obligatorio.");
        }

        Actividad actividad = new Actividad();
        actividad.setTitulo(dto.titulo().trim());
        actividad.setDescripcion(dto.descripcion());

        if (dto.hexColor() != null && !dto.hexColor().isBlank()) {
            String color = dto.hexColor().trim();
            if (color.length() != 7 || !color.startsWith("#")) {
                throw new IllegalArgumentException("El color hexadecimal de la actividad debe tener formato #RRGGBB.");
            }
            actividad.setHexColor(color);
        } else {
            actividad.setHexColor("#3B82F6");
        }

        actividad.setFechaInicio(dto.fechaInicio());
        actividad.setFechaFin(dto.fechaFin());
        actividad.setTipoActividad(tipo);

        if (dto.etiquetas() != null && !dto.etiquetas().isEmpty()) {
            Set<Etiqueta> etiquetas = etiquetaService.obtenerOCrearEtiquetas(dto.etiquetas());
            actividad.setEtiquetas(etiquetas);
        }

        Actividad actividadGuardada = actividadRepository.save(actividad);

        if (dto.recurrencia() != null) {
            recurrenciaService.guardarRecurrencia(actividadGuardada, dto.recurrencia());
        }

        if (dto.recordatorios() != null && !dto.recordatorios().isEmpty()) {
            recordatorioService.guardarRecordatorios(actividadGuardada, dto.recordatorios());
        }

        if (dto.notas() != null && !dto.notas().isEmpty()) {
            notaService.guardarNotas(actividadGuardada, dto.notas());
        }

        return actividadGuardada;
    }

    @Transactional
    public Actividad actualizarActividad(Actividad actividad, ActividadPostDTO dto, TipoActividadEnum tipoEsperado) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad a actualizar no puede ser nula.");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la actividad son obligatorios.");
        }
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("El título de la actividad es obligatorio.");
        }
        if (dto.titulo().trim().length() > 150) {
            throw new IllegalArgumentException("El título de la actividad no puede exceder 150 caracteres.");
        }
        if (dto.fechaInicio() != null && dto.fechaFin() != null && dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        if (dto.tipoActividad() != null && tipoEsperado != null && dto.tipoActividad() != tipoEsperado) {
            throw new IllegalArgumentException("El tipo de actividad debe ser " + tipoEsperado + ".");
        }
        TipoActividadEnum tipo = dto.tipoActividad() != null ? dto.tipoActividad() : tipoEsperado;
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de actividad es obligatorio.");
        }

        actividad.setTitulo(dto.titulo().trim());
        actividad.setDescripcion(dto.descripcion());

        if (dto.hexColor() != null && !dto.hexColor().isBlank()) {
            String color = dto.hexColor().trim();
            if (color.length() != 7 || !color.startsWith("#")) {
                throw new IllegalArgumentException("El color hexadecimal de la actividad debe tener formato #RRGGBB.");
            }
            actividad.setHexColor(color);
        } else {
            actividad.setHexColor("#3B82F6");
        }

        actividad.setFechaInicio(dto.fechaInicio());
        actividad.setFechaFin(dto.fechaFin());
        actividad.setTipoActividad(tipo);

        if (dto.etiquetas() == null || dto.etiquetas().isEmpty()) {
            actividad.getEtiquetas().clear();
        } else {
            Set<Etiqueta> etiquetas = etiquetaService.obtenerOCrearEtiquetas(dto.etiquetas());
            actividad.setEtiquetas(etiquetas);
        }

        actividad.setRecurrencia(null);
        actividadRepository.flush();
        recurrenciaRepository.eliminarPorIdActividad(actividad.getId());
        recurrenciaRepository.flush();
        if (dto.recurrencia() != null) {
            recurrenciaService.guardarRecurrencia(actividad, dto.recurrencia());
        }

        actividad.getRecordatorios().clear();
        recordatorioRepository.eliminarPorIdActividad(actividad.getId());
        recordatorioRepository.flush();
        if (dto.recordatorios() != null && !dto.recordatorios().isEmpty()) {
            recordatorioService.guardarRecordatorios(actividad, dto.recordatorios());
        }

        return actividadRepository.save(actividad);
    }
}
