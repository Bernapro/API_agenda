package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "no_telefonicos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_contacto", "prefijo", "no_telefonico"})
)
public class NoTelefonico {

    @EmbeddedId
    private NoTelefonicoId id;

    @MapsId("idContacto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contacto", nullable = false)
    private Contacto contacto;

    @Column(name = "prefijo", length = 10, nullable = false)
    private String prefijo = "+52";

    @Column(name = "no_telefonico", length = 20, nullable = false)
    private String noTelefonico;

    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    public NoTelefonico() {
    }

    public NoTelefonicoId getId() {
        return id;
    }

    public void setId(NoTelefonicoId id) {
        this.id = id;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public String getNoTelefonico() {
        return noTelefonico;
    }

    public void setNoTelefonico(String noTelefonico) {
        this.noTelefonico = noTelefonico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
