package com.unach.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.ContactoGetResumenDTO;
import com.unach.dto.ContactoPostDTO;
import com.unach.dto.ContactoPostResponseDTO;
import com.unach.entity.Contacto;
import com.unach.entity.CorreoElectronico;
import com.unach.entity.Direccion;
import com.unach.entity.Etiqueta;
import com.unach.entity.NoTelefonico;
import com.unach.errorHandling.Exception.ResourceNotFoundException;
import com.unach.repository.ContactoRepository;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;
    private final EtiquetaService etiquetaService;
    private final DireccionService direccionService;
    private final NoTelefonicoService noTelefonicoService;
    private final CorreoElectronicoService correoElectronicoService;

    public ContactoService(
            ContactoRepository contactoRepository,
            EtiquetaService etiquetaService,
            DireccionService direccionService,
            NoTelefonicoService noTelefonicoService,
            CorreoElectronicoService correoElectronicoService) {
        this.contactoRepository = contactoRepository;
        this.etiquetaService = etiquetaService;
        this.direccionService = direccionService;
        this.noTelefonicoService = noTelefonicoService;
        this.correoElectronicoService = correoElectronicoService;
    }

    @Transactional
    public ContactoPostResponseDTO altaContacto(ContactoPostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del contacto no pueden ser nulos.");
        }
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del contacto es obligatorio.");
        }
        if (dto.nombre().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre del contacto no puede exceder 100 caracteres.");
        }
        if (dto.apellidoPaterno() != null && dto.apellidoPaterno().length() > 100) {
            throw new IllegalArgumentException("El apellido paterno no puede exceder 100 caracteres.");
        }
        if (dto.apellidoMaterno() != null && dto.apellidoMaterno().length() > 100) {
            throw new IllegalArgumentException("El apellido materno no puede exceder 100 caracteres.");
        }
        if (dto.apodo() != null && dto.apodo().length() > 50) {
            throw new IllegalArgumentException("El apodo no puede exceder 50 caracteres.");
        }

        Contacto contacto = new Contacto();
        contacto.setNombre(dto.nombre().trim());
        contacto.setApellidoPaterno(dto.apellidoPaterno());
        contacto.setApellidoMaterno(dto.apellidoMaterno());
        contacto.setApodo(dto.apodo());
        contacto.setCumpleanos(dto.cumpleanos());
        contacto.setAvatar(dto.avatar());
        contacto.setNotas(dto.notas());
        contacto.setFavorito(dto.favorito() != null ? dto.favorito() : false);
        contacto.setEmpresa(dto.empresa());
        contacto.setDepartamento(dto.departamento());
        contacto.setPuesto(dto.puesto());

        if (dto.etiquetas() != null && !dto.etiquetas().isEmpty()) {
            Set<Etiqueta> etiquetas = etiquetaService.obtenerOCrearEtiquetas(dto.etiquetas());
            contacto.setEtiquetas(etiquetas);
        }

        Contacto contactoGuardado = contactoRepository.save(contacto);

        if (dto.direcciones() != null && !dto.direcciones().isEmpty()) {
            List<Direccion> direcciones = direccionService.guardarDirecciones(contactoGuardado, dto.direcciones());
            contactoGuardado.getDirecciones().addAll(direcciones);
        }

        if (dto.noTelefonicos() != null && !dto.noTelefonicos().isEmpty()) {
            List<NoTelefonico> telefonos = noTelefonicoService.guardarNumerosTelefonicos(contactoGuardado, dto.noTelefonicos());
            contactoGuardado.getNumerosTelefonicos().addAll(telefonos);
        }

        if (dto.correosElectronicos() != null && !dto.correosElectronicos().isEmpty()) {
            List<CorreoElectronico> correos = correoElectronicoService.guardarCorreosElectronicos(contactoGuardado, dto.correosElectronicos());
            contactoGuardado.getCorreosElectronicos().addAll(correos);
        }

        return new ContactoPostResponseDTO(
                contactoGuardado.getId(),
                contactoGuardado.getNombre(),
                contactoGuardado.getApodo()
        );
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> obtenerResumenContactos(Integer idRecibido, Integer limit) {
        int ultimoId = 0;
        if (idRecibido != null) {
            if (idRecibido < 0) {
                throw new IllegalArgumentException("El id del cursor no puede ser negativo.");
            }
            ultimoId = ultimoId + idRecibido;
        }

        int limite = (limit == null) ? 10 : limit;
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0.");
        }

        Pageable pageable = PageRequest.of(0, limite);
        if (ultimoId == 0) {
            return contactoRepository.obtenerPrimerosResumen(pageable);
        }
        return contactoRepository.obtenerResumenDespuesDeId(ultimoId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorAtributoNombre(String cadena) {
        return contactoRepository.buscarPorNombre(cadena);
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorApellidoPaterno(String cadena) {
        return contactoRepository.buscarPorApellidoPaterno(cadena);
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorApellidoMaterno(String cadena) {
        return contactoRepository.buscarPorApellidoMaterno(cadena);
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorApodo(String cadena) {
        return contactoRepository.buscarPorApodo(cadena);
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorNombre(String cadena) {
        if (cadena == null || cadena.isBlank()) {
            throw new IllegalArgumentException("El parámetro de búsqueda por nombre no puede estar vacío.");
        }

        String termino = cadena.trim();
        Map<Integer, ContactoGetResumenDTO> coincidencias = new LinkedHashMap<>();

        buscarPorAtributoNombre(termino).forEach(c -> coincidencias.put(c.id(), c));
        buscarPorApellidoPaterno(termino).forEach(c -> coincidencias.put(c.id(), c));
        buscarPorApellidoMaterno(termino).forEach(c -> coincidencias.put(c.id(), c));
        buscarPorApodo(termino).forEach(c -> coincidencias.put(c.id(), c));

        if (coincidencias.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron contactos que coincidan con: " + termino);
        }

        return new ArrayList<>(coincidencias.values());
    }

    @Transactional(readOnly = true)
    public List<ContactoGetResumenDTO> buscarPorEtiqueta(String etiqueta) {
        if (etiqueta == null || etiqueta.isBlank()) {
            throw new IllegalArgumentException("La etiqueta a buscar no puede estar vacía.");
        }

        List<ContactoGetResumenDTO> resultados = contactoRepository.buscarPorEtiqueta(etiqueta.trim());
        if (resultados.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron contactos con la etiqueta: " + etiqueta.trim());
        }
        return resultados;
    }

    @Transactional(readOnly = true)
    public Contacto obtenerEntidadPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id del contacto debe ser un entero positivo.");
        }
        return contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el contacto con id: " + id));
    }
}
