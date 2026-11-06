package com.financorp.serf.repository;

import com.financorp.serf.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByFilial_Id(Long filialId);

    List<Inventario> findByProducto_Id(Long productoId);

    Optional<Inventario> findByProducto_IdAndFilial_Id(
            Long productoId,
            Long filialId
    );

    List<Inventario> findByCantidadDisponibleLessThanEqual(
            Integer cantidad
    );
}