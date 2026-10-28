package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CorreoElectronicoId implements Serializable {

    @Column(name = "id_contacto", nullable = false)
    private Integer idContacto;

    @Column(name = "disc", nullable = false)
    private Short disc;

    public CorreoElectronicoId() {
    }

    public CorreoElectronicoId(Integer idContacto, Short disc) {
        this.idContacto = idContacto;
        this.disc = disc;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
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
        CorreoElectronicoId that = (CorreoElectronicoId) o;
        return Objects.equals(idContacto, that.idContacto) && Objects.equals(disc, that.disc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idContacto, disc);
    }
}
