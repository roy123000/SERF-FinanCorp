package com.financorp.serf.service;

import com.financorp.serf.entity.Filial;
import com.financorp.serf.entity.Inventario;
import com.financorp.serf.entity.Producto;
import com.financorp.serf.repository.FilialRepository;
import com.financorp.serf.repository.InventarioRepository;
import com.financorp.serf.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final FilialRepository filialRepository;

    public InventarioService(
            InventarioRepository inventarioRepository,
            ProductoRepository productoRepository,
            FilialRepository filialRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.filialRepository = filialRepository;
    }

    public List<Inventario> listarInventarios() {
        return inventarioRepository.findAll();
    }

    public Inventario obtenerInventario(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Inventario no encontrado: " + id));
    }

    public List<Inventario> buscarPorFilial(Long filialId) {
        return inventarioRepository.findByFilial_Id(filialId);
    }

    public List<Inventario> buscarPorProducto(Long productoId) {
        return inventarioRepository.findByProducto_Id(productoId);
    }

    public Inventario registrarInventario(
            Long productoId,
            Long filialId,
            Integer cantidadInicial,
            Integer stockMinimo) {

        if (cantidadInicial == null || cantidadInicial < 0
                || stockMinimo == null || stockMinimo < 0) {
            throw new IllegalArgumentException(
                    "Las cantidades no pueden ser negativas.");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado: " + productoId));

        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() ->
                        new RuntimeException("Filial no encontrada: " + filialId));

        if (inventarioRepository
                .findByProducto_IdAndFilial_Id(productoId, filialId)
                .isPresent()) {
            throw new RuntimeException(
                    "Ya existe un inventario para este producto en esta filial.");
        }

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setFilial(filial);
        inventario.setCantidadDisponible(cantidadInicial);
        inventario.setStockMinimo(stockMinimo);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventario.setEstado(true);

        return inventarioRepository.save(inventario);
    }

    public Inventario registrarEntrada(Long id, Integer cantidad) {
        Inventario inventario = obtenerInventario(id);

        validarCantidad(cantidad);

        inventario.setCantidadDisponible(
                inventario.getCantidadDisponible() + cantidad);

        inventario.setFechaActualizacion(LocalDateTime.now());

        return inventarioRepository.save(inventario);
    }

    public Inventario registrarSalida(Long id, Integer cantidad) {
        Inventario inventario = obtenerInventario(id);

        validarCantidad(cantidad);

        if (inventario.getCantidadDisponible() < cantidad) {
            throw new IllegalArgumentException(
                    "No existe suficiente stock disponible.");
        }

        inventario.setCantidadDisponible(
                inventario.getCantidadDisponible() - cantidad);

        inventario.setFechaActualizacion(LocalDateTime.now());

        return inventarioRepository.save(inventario);
    }

    public List<Inventario> consultarStockBajo(Integer cantidad) {
        validarCantidad(cantidad);

        return inventarioRepository
                .findByCantidadDisponibleLessThanEqual(cantidad);
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }
    }
}