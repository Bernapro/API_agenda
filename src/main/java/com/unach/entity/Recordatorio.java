package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.unach.enums.UnidadTiempoEnum;

@Entity
@Table(name = "recordatorios")
public class Recordatorio {

    @EmbeddedId
    private RecordatorioId id;

    @MapsId("idActividad")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @Column(name = "tiempo_anticipacion", nullable = false)
    private Integer tiempoAnticipacion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "unidad_tiempo", nullable = false, columnDefinition = "unidad_tiempo_enum")
    private UnidadTiempoEnum unidadTiempo;

    public Recordatorio() {
    }

    public RecordatorioId getId() {
        return id;
    }

    public void setId(RecordatorioId id) {
        this.id = id;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Integer getTiempoAnticipacion() {
        return tiempoAnticipacion;
    }

    public void setTiempoAnticipacion(Integer tiempoAnticipacion) {
        this.tiempoAnticipacion = tiempoAnticipacion;
    }

    public UnidadTiempoEnum getUnidadTiempo() {
        return unidadTiempo;
    }

    public void setUnidadTiempo(UnidadTiempoEnum unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }
}
