
package com.financorp.serf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La venta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    public DetalleVenta() {
    }

    public DetalleVenta(Producto producto, Integer cantidad,
                        BigDecimal precioUnitario,
                        BigDecimal descuento) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento == null
                ? BigDecimal.ZERO : descuento;
        calcularSubtotal();
    }

    public BigDecimal calcularSubtotal() {
        validarDatos();

        BigDecimal importe = precioUnitario.multiply(
                BigDecimal.valueOf(cantidad)
        );

        this.subtotal = importe.subtract(calcularDescuento());

        return subtotal;
    }

    public BigDecimal calcularDescuento() {
        return descuento == null ? BigDecimal.ZERO : descuento;
    }

    public void actualizarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal obtenerImporte() {
        return calcularSubtotal();
    }

    private void validarDatos() {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        if (precioUnitario == null
                || precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo ni nulo."
            );
        }

        if (descuento != null
                && (descuento.compareTo(BigDecimal.ZERO) < 0
                || descuento.compareTo(
                        precioUnitario.multiply(
                                BigDecimal.valueOf(cantidad)
                        )
                ) > 0)) {
            throw new IllegalArgumentException(
                    "El descuento no es válido."
            );
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        actualizarCantidad(cantidad);
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
