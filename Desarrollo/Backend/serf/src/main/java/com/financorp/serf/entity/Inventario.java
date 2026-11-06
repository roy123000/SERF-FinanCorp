
package com.financorp.serf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inventarios",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"producto_id", "filial_id"}
        )
    }
)
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La filial es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @Column(nullable = false)
    private Integer cantidadDisponible = 0;

    @Column(nullable = false)
    private Integer stockMinimo = 0;

    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private Boolean estado = true;

    public Inventario() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Inventario(Producto producto, Filial filial,
                     Integer cantidadDisponible, Integer stockMinimo) {
        this.producto = producto;
        this.filial = filial;
        this.cantidadDisponible = cantidadDisponible;
        this.stockMinimo = stockMinimo;
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = true;
    }

    public void registrarEntrada(Integer cantidad) {
        validarCantidad(cantidad);
        this.cantidadDisponible += cantidad;
        actualizarFecha();
    }

    public void registrarSalida(Integer cantidad) {
        validarCantidad(cantidad);

        if (cantidad > cantidadDisponible) {
            throw new IllegalArgumentException(
                "No existe suficiente stock disponible."
            );
        }

        this.cantidadDisponible -= cantidad;
        actualizarFecha();
    }

    public Integer consultarStock() {
        return cantidadDisponible;
    }

    public void actualizarStock(Integer cantidad) {
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException(
                "El stock no puede ser negativo."
            );
        }

        this.cantidadDisponible = cantidad;
        actualizarFecha();
    }

    public boolean verificarStockMinimo() {
        return cantidadDisponible <= stockMinimo;
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                "La cantidad debe ser mayor que cero."
            );
        }
    }

    private void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
        actualizarFecha();
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
