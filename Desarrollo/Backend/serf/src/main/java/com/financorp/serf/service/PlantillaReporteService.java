
package com.financorp.serf.service;

import com.financorp.serf.entity.PlantillaReporte;
import com.financorp.serf.repository.PlantillaReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlantillaReporteService {

    private final PlantillaReporteRepository plantillaRepository;

    public PlantillaReporteService(
            PlantillaReporteRepository plantillaRepository) {
        this.plantillaRepository = plantillaRepository;
    }

    public List<PlantillaReporte> listarPlantillas() {
        return plantillaRepository.findAll();
    }

    public List<PlantillaReporte> listarPlantillasActivas() {
        return plantillaRepository.findByEstadoTrue();
    }

    public PlantillaReporte obtenerPlantilla(Long id) {
        return plantillaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Plantilla no encontrada: " + id));
    }

    public List<PlantillaReporte> buscarPorPais(String pais) {
        return plantillaRepository.findByPais(pais);
    }

    public List<PlantillaReporte> buscarPorTipo(String tipo) {
        return plantillaRepository.findByTipoReporte(tipo);
    }

    public List<PlantillaReporte> buscarPorPaisYTipo(
            String pais, String tipo) {
        return plantillaRepository.findByPaisAndTipoReporte(
                pais, tipo);
    }

    public PlantillaReporte registrarPlantilla(
            PlantillaReporte plantilla) {

        if (plantilla.getNombre() == null
                || plantilla.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la plantilla es obligatorio.");
        }

        return plantillaRepository.save(plantilla);
    }

    public PlantillaReporte actualizarPlantilla(
            Long id, PlantillaReporte datosPlantilla) {

        PlantillaReporte plantilla = obtenerPlantilla(id);

        plantilla.setNombre(datosPlantilla.getNombre());
        plantilla.setDescripcion(datosPlantilla.getDescripcion());
        plantilla.setPais(datosPlantilla.getPais());
        plantilla.setTipoReporte(datosPlantilla.getTipoReporte());
        plantilla.setEstado(datosPlantilla.getEstado());

        return plantillaRepository.save(plantilla);
    }

    public PlantillaReporte clonarPlantilla(Long id) {
        PlantillaReporte original = obtenerPlantilla(id);

        PlantillaReporte copia = original.clonarPlantilla();

        copia.setId(null);
        copia.setNombre(original.getNombre() + " - Copia");
        copia.setVersion(1);
        copia.setEstado(true);

        return plantillaRepository.save(copia);
    }

    public void eliminarPlantilla(Long id) {
        PlantillaReporte plantilla = obtenerPlantilla(id);
        plantillaRepository.delete(plantilla);
    }
}
