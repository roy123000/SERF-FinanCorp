package com.financorp.serf.repository;

import com.financorp.serf.entity.PlantillaReporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantillaReporteRepository
        extends JpaRepository<PlantillaReporte, Long> {

    List<PlantillaReporte> findByPais(String pais);

    List<PlantillaReporte> findByTipoReporte(String tipoReporte);

    List<PlantillaReporte> findByEstadoTrue();

    List<PlantillaReporte> findByPaisAndTipoReporte(
            String pais,
            String tipoReporte
    );
}