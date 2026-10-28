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
    name = "correos_electronicos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_contacto", "direccion"})
)
public class CorreoElectronico {

    @EmbeddedId
    private CorreoElectronicoId id;

    @MapsId("idContacto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contacto", nullable = false)
    private Contacto contacto;

    @Column(name = "direccion", length = 150, nullable = false)
    private String direccion;

    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    public CorreoElectronico() {
    }

    public CorreoElectronicoId getId() {
        return id;
    }

    public void setId(CorreoElectronicoId id) {
        this.id = id;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
