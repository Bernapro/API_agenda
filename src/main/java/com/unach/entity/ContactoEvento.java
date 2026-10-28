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
@Table(name = "contactos_eventos")
public class ContactoEvento {

    @EmbeddedId
    private ContactoEventoId id;

    @MapsId("idContacto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contacto", nullable = false)
    private Contacto contacto;

    @MapsId("idEvento")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(name = "nota_participacion", length = 255)
    private String notaParticipacion;

    public ContactoEvento() {
    }

    public ContactoEventoId getId() {
        return id;
    }

    public void setId(ContactoEventoId id) {
        this.id = id;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getNotaParticipacion() {
        return notaParticipacion;
    }

    public void setNotaParticipacion(String notaParticipacion) {
        this.notaParticipacion = notaParticipacion;
    }
}
