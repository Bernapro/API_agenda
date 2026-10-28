package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.unach.enums.FrecuenciaEnum;

@Entity
@Table(name = "recurrencia")
public class Recurrencia {

    @Id
    @Column(name = "id_actividad")
    private Integer idActividad;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "frecuencia", nullable = false, columnDefinition = "frecuencia_enum")
    private FrecuenciaEnum frecuencia;

    @Column(name = "intervalo", nullable = false)
    private Short intervalo = 1;

    @Column(name = "dias_semana", length = 20)
    private String diasSemana;

    @Column(name = "dia_mes")
    private Short diaMes;

    @Column(name = "mes_anio")
    private Short mesAnio;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    public Recurrencia() {
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

    public FrecuenciaEnum getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(FrecuenciaEnum frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Short getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Short intervalo) {
        this.intervalo = intervalo;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public Short getDiaMes() {
        return diaMes;
    }

    public void setDiaMes(Short diaMes) {
        this.diaMes = diaMes;
    }

    public Short getMesAnio() {
        return mesAnio;
    }

    public void setMesAnio(Short mesAnio) {
        this.mesAnio = mesAnio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
