package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContactoEventoId implements Serializable {

    @Column(name = "id_contacto", nullable = false)
    private Integer idContacto;

    @Column(name = "id_evento", nullable = false)
    private Integer idEvento;

    public ContactoEventoId() {
    }

    public ContactoEventoId(Integer idContacto, Integer idEvento) {
        this.idContacto = idContacto;
        this.idEvento = idEvento;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactoEventoId that = (ContactoEventoId) o;
        return Objects.equals(idContacto, that.idContacto) && Objects.equals(idEvento, that.idEvento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idContacto, idEvento);
    }
}
