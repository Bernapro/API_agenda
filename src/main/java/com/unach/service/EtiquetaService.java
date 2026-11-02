package com.unach.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unach.dto.EtiquetaGetDTO;
import com.unach.dto.EtiquetaPostDTO;
import com.unach.entity.Etiqueta;
import com.unach.repository.EtiquetaRepository;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Transactional(readOnly = true)
    public List<EtiquetaGetDTO> obtenerEtiquetas(Integer idRecibido, Integer limit) {
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
            return etiquetaRepository.obtenerPrimerasEtiquetas(pageable);
        }
        return etiquetaRepository.obtenerEtiquetasDespuesDeId(ultimoId, pageable);
    }

    @Transactional
    public Set<Etiqueta> obtenerOCrearEtiquetas(List<EtiquetaPostDTO> etiquetasDto) {
        Set<Etiqueta> etiquetas = new HashSet<>();
        if (etiquetasDto == null || etiquetasDto.isEmpty()) {
            return etiquetas;
        }

        for (EtiquetaPostDTO dto : etiquetasDto) {
            if (dto == null || dto.nombre() == null || dto.nombre().isBlank()) {
                throw new IllegalArgumentException("El nombre de la etiqueta es obligatorio.");
            }
            String nombreLimpio = dto.nombre().trim();
            if (nombreLimpio.length() > 50) {
                throw new IllegalArgumentException("El nombre de la etiqueta no puede exceder 50 caracteres.");
            }

            Etiqueta etiqueta = etiquetaRepository.findByNombreIgnoreCase(nombreLimpio)
                    .orElseGet(() -> {
                        Etiqueta nueva = new Etiqueta();
                        nueva.setNombre(nombreLimpio);
                        if (dto.hexColor() != null && !dto.hexColor().isBlank()) {
                            String color = dto.hexColor().trim();
                            if (color.length() != 7 || !color.startsWith("#")) {
                                throw new IllegalArgumentException("El color hexadecimal de la etiqueta debe tener formato #RRGGBB.");
                            }
                            nueva.setHexColor(color);
                        } else {
                            nueva.setHexColor("#6B7280");
                        }
                        return etiquetaRepository.save(nueva);
                    });

            etiquetas.add(etiqueta);
        }

        return etiquetas;
    }

    public List<EtiquetaGetDTO> mapearADTOList(Collection<Etiqueta> etiquetas) {
        if (etiquetas == null || etiquetas.isEmpty()) {
            return List.of();
        }
        return etiquetas.stream()
                .map(e -> new EtiquetaGetDTO(e.getId(), e.getNombre(), e.getHexColor()))
                .toList();
    }
}
