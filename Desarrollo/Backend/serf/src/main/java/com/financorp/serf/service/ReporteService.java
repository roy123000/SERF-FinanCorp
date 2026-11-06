
package com.financorp.serf.service;

import com.financorp.serf.entity.Reporte;
import com.financorp.serf.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }

    public Reporte obtenerReporte(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Reporte no encontrado: " + id));
    }

    public List<Reporte> buscarPorPais(String pais) {
        return reporteRepository.findByPais(pais);
    }

    public List<Reporte> buscarPorTipo(String tipo) {
        return reporteRepository.findByTipo(tipo);
    }

    public List<Reporte> buscarPorEstado(String estado) {
        return reporteRepository.findByEstado(estado);
    }

    public List<Reporte> buscarPorPaisYTipo(
            String pais, String tipo) {
        return reporteRepository.findByPaisAndTipo(pais, tipo);
    }

    public Reporte registrarReporte(Reporte reporte) {
        if (reporte.getTitulo() == null
                || reporte.getTitulo().isBlank()) {
            throw new IllegalArgumentException(
                    "El título del reporte es obligatorio.");
        }

        reporte.generar();

        return reporteRepository.save(reporte);
    }

    public Reporte actualizarReporte(
            Long id, Reporte datosReporte) {

        Reporte reporte = obtenerReporte(id);

        reporte.setTitulo(datosReporte.getTitulo());
        reporte.setTipo(datosReporte.getTipo());
        reporte.setPais(datosReporte.getPais());
        reporte.setPeriodoInicio(datosReporte.getPeriodoInicio());
        reporte.setPeriodoFin(datosReporte.getPeriodoFin());
        reporte.setMoneda(datosReporte.getMoneda());
        reporte.setEstado(datosReporte.getEstado());

        return reporteRepository.save(reporte);
    }

    public void eliminarReporte(Long id) {
        Reporte reporte = obtenerReporte(id);
        reporteRepository.delete(reporte);
    }
}
