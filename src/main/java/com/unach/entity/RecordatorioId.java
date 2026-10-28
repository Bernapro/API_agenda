package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecordatorioId implements Serializable {

    @Column(name = "id_actividad", nullable = false)
    private Integer idActividad;

    @Column(name = "disc", nullable = false)
    private Short disc;

    public RecordatorioId() {
    }

    public RecordatorioId(Integer idActividad, Short disc) {
        this.idActividad = idActividad;
        this.disc = disc;
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Integer idActividad) {
        this.idActividad = idActividad;
    }

    public Short getDisc() {
        return disc;
    }

    public void setDisc(Short disc) {
        this.disc = disc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordatorioId that = (RecordatorioId) o;
        return Objects.equals(idActividad, that.idActividad) && Objects.equals(disc, that.disc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActividad, disc);
    }
}
