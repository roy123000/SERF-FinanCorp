package com.financorp.serf.repository;

import com.financorp.serf.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByEstadoTrue();

    boolean existsByCodigo(String codigo);
}