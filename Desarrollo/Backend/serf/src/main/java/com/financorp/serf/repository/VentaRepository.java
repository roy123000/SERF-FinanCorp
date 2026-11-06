package com.financorp.serf.repository;

import com.financorp.serf.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByNumeroVenta(String numeroVenta);

    List<Venta> findByFilial_Id(Long filialId);

    List<Venta> findByFechaVentaBetween(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );

    List<Venta> findByFilial_IdAndFechaVentaBetween(
            Long filialId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );

    boolean existsByNumeroVenta(String numeroVenta);
}