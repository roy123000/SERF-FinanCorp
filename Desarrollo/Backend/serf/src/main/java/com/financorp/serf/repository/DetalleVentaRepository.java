package com.financorp.serf.repository;

import com.financorp.serf.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVenta_Id(Long ventaId);

    List<DetalleVenta> findByProducto_Id(Long productoId);
}
