package com.unach.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @Column(name = "id_actividad")
    private Integer idActividad;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_actividad", nullable = false)
    private Actividad actividad;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "todo_el_dia", nullable = false)
    private Boolean todoElDia = false;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContactoEvento> contactoEventos = new ArrayList<>();

    public Evento() {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getTodoElDia() {
        return todoElDia;
    }

    public void setTodoElDia(Boolean todoElDia) {
        this.todoElDia = todoElDia;
    }

    public List<ContactoEvento> getContactoEventos() {
        return contactoEventos;
    }

    public void setContactoEventos(List<ContactoEvento> contactoEventos) {
        this.contactoEventos = contactoEventos;
    }
}
