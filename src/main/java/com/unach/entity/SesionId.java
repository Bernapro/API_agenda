package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SesionId implements Serializable {

    @Column(name = "id_tarea", nullable = false)
    private Integer idTarea;

    @Column(name = "disc", nullable = false)
    private Short disc;

    public SesionId() {
    }

    public SesionId(Integer idTarea, Short disc) {
        this.idTarea = idTarea;
        this.disc = disc;
    }

    public Integer getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Integer idTarea) {
        this.idTarea = idTarea;
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
        SesionId that = (SesionId) o;
        return Objects.equals(idTarea, that.idTarea) && Objects.equals(disc, that.disc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTarea, disc);
    }
}
