
package com.financorp.serf.service;

import com.financorp.serf.entity.DetalleVenta;
import com.financorp.serf.entity.Filial;
import com.financorp.serf.entity.Inventario;
import com.financorp.serf.entity.Venta;
import com.financorp.serf.repository.FilialRepository;
import com.financorp.serf.repository.InventarioRepository;
import com.financorp.serf.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final FilialRepository filialRepository;
    private final InventarioRepository inventarioRepository;

    public VentaService(
            VentaRepository ventaRepository,
            FilialRepository filialRepository,
            InventarioRepository inventarioRepository) {

        this.ventaRepository = ventaRepository;
        this.filialRepository = filialRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta obtenerVenta(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Venta no encontrada: " + id));
    }

    public List<Venta> buscarPorFilial(Long filialId) {
        return ventaRepository.findByFilial_Id(filialId);
    }

    public Venta registrarVenta(Venta venta) {

        if (ventaRepository.existsByNumeroVenta(
                venta.getNumeroVenta())) {
            throw new RuntimeException(
                    "El número de venta ya está registrado.");
        }

        if (venta.getDetalles() == null
                || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos un producto.");
        }

        if (venta.getFilial() == null
                || venta.getFilial().getId() == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una filial.");
        }

        Filial filial = filialRepository.findById(
                venta.getFilial().getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Filial no encontrada."));

        // Verificar los productos y establecer la relación
        // entre cada detalle y su venta.
        for (DetalleVenta detalle : venta.getDetalles()) {

            if (detalle.getProducto() == null
                    || detalle.getProducto().getId() == null) {
                throw new IllegalArgumentException(
                        "Cada detalle debe tener un producto.");
            }

            if (detalle.getCantidad() == null
                    || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero.");
            }

            // CORRECCIÓN: asignar la venta a cada detalle.
            detalle.setVenta(venta);

            Inventario inventario = inventarioRepository
                    .findByProducto_IdAndFilial_Id(
                            detalle.getProducto().getId(),
                            filial.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No existe inventario para uno de los productos."));

            if (inventario.getCantidadDisponible()
                    < detalle.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: "
                                + detalle.getProducto().getId());
            }
        }

        // Descontar el stock de cada producto.
        for (DetalleVenta detalle : venta.getDetalles()) {

            Inventario inventario = inventarioRepository
                    .findByProducto_IdAndFilial_Id(
                            detalle.getProducto().getId(),
                            filial.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No se encontró el inventario del producto."));

            inventario.registrarSalida(detalle.getCantidad());

            inventarioRepository.save(inventario);
        }

        venta.setFilial(filial);
        venta.setFechaVenta(LocalDateTime.now());

        venta.registrarVenta();

        return ventaRepository.save(venta);
    }

    public List<Venta> buscarPorPeriodo(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        if (fechaInicio == null
                || fechaFin == null
                || fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "El periodo de consulta no es válido.");
        }

        return ventaRepository.findByFechaVentaBetween(
                fechaInicio,
                fechaFin);
    }

    public void anularVenta(Long id) {

        Venta venta = obtenerVenta(id);

        if ("ANULADA".equals(venta.getEstado())) {
            throw new IllegalArgumentException(
                    "La venta ya se encuentra anulada.");
        }

        // Devolver al inventario los productos de la venta.
        for (DetalleVenta detalle : venta.getDetalles()) {

            Inventario inventario = inventarioRepository
                    .findByProducto_IdAndFilial_Id(
                            detalle.getProducto().getId(),
                            venta.getFilial().getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No se encontró el inventario del producto."));

            inventario.registrarEntrada(detalle.getCantidad());

            inventarioRepository.save(inventario);
        }

        venta.anularVenta();

        ventaRepository.save(venta);
    }
}
