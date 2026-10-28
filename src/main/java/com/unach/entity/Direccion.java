package com.unach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @EmbeddedId
    private DireccionId id;

    @MapsId("idContacto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contacto", nullable = false)
    private Contacto contacto;

    @Column(name = "direccion", length = 150, nullable = false)
    private String direccion;

    @Column(name = "segunda_linea", length = 150)
    private String segundaLinea;

    @Column(name = "apartado_postal", length = 20)
    private String apartadoPostal;

    @Column(name = "codigo_postal", length = 15)
    private String codigoPostal;

    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;

    @Column(name = "estado", length = 100, nullable = false)
    private String estado;

    @Column(name = "pais", length = 100, nullable = false)
    private String pais = "México";

    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    public Direccion() {
    }

    public DireccionId getId() {
        return id;
    }

    public void setId(DireccionId id) {
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

    public String getSegundaLinea() {
        return segundaLinea;
    }

    public void setSegundaLinea(String segundaLinea) {
        this.segundaLinea = segundaLinea;
    }

    public String getApartadoPostal() {
        return apartadoPostal;
    }

    public void setApartadoPostal(String apartadoPostal) {
        this.apartadoPostal = apartadoPostal;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
