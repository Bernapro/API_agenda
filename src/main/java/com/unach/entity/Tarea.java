package com.unach.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.unach.enums.EstadoTareaEnum;
import com.unach.enums.PrioridadTareaEnum;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @Column(name = "id_actividad")
    private Integer idActividad;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_tarea_enum")
    private EstadoTareaEnum estado = EstadoTareaEnum.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "prioridad", nullable = false, columnDefinition = "prioridad_tarea_enum")
    private PrioridadTareaEnum prioridad = PrioridadTareaEnum.MEDIA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarea_padre")
    private Tarea tareaPadre;

    @OneToMany(mappedBy = "tareaPadre", fetch = FetchType.LAZY)
    private List<Tarea> subtareas = new ArrayList<>();

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sesion> sesiones = new ArrayList<>();

    public Tarea() {
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public EstadoTareaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoTareaEnum estado) {
        this.estado = estado;
    }

    public PrioridadTareaEnum getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadTareaEnum prioridad) {
        this.prioridad = prioridad;
    }

    public Tarea getTareaPadre() {
        return tareaPadre;
    }

    public void setTareaPadre(Tarea tareaPadre) {
        this.tareaPadre = tareaPadre;
    }

    public List<Tarea> getSubtareas() {
        return subtareas;
    }

    public void setSubtareas(List<Tarea> subtareas) {
        this.subtareas = subtareas;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }
}
