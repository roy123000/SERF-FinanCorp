package com.financorp.serf.repository;

import com.financorp.serf.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findByPais(String pais);

    List<Reporte> findByTipo(String tipo);

    List<Reporte> findByEstado(String estado);

    List<Reporte> findByPaisAndTipo(String pais, String tipo);
}