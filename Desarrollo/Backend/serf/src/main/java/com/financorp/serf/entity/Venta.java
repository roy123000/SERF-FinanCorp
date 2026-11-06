
package com.financorp.serf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroVenta;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @NotNull(message = "La filial es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @Column(nullable = false, length = 10)
    private String moneda;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 8, scale = 4)
    private BigDecimal tasaImpuesto = new BigDecimal("18.00");

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal impuesto = BigDecimal.ZERO;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String estado = "REGISTRADA";

    @OneToMany(
        mappedBy = "venta",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
        this.fechaVenta = LocalDateTime.now();
    }

    public Venta(String numeroVenta, Filial filial, String moneda) {
        this.numeroVenta = numeroVenta;
        this.filial = filial;
        this.moneda = moneda;
        this.fechaVenta = LocalDateTime.now();
        this.estado = "REGISTRADA";
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException(
                "El detalle de venta no puede ser nulo."
            );
        }

        detalles.add(detalle);
        detalle.setVenta(this);
        calcularTotal();
    }

    public void eliminarDetalle(DetalleVenta detalle) {
        if (detalles.remove(detalle)) {
            detalle.setVenta(null);
            calcularTotal();
        }
    }

    public BigDecimal calcularSubtotal() {
        this.subtotal = detalles.stream()
            .map(DetalleVenta::calcularSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return subtotal;
    }

    public BigDecimal calcularImpuesto() {
        this.impuesto = calcularSubtotal()
            .multiply(tasaImpuesto)
            .divide(new BigDecimal("100"), 2,
                    java.math.RoundingMode.HALF_UP);

        return impuesto;
    }

    public BigDecimal calcularTotal() {
        this.total = calcularSubtotal().add(calcularImpuesto());
        return total;
    }

    public void registrarVenta() {
        calcularTotal();
        this.estado = "REGISTRADA";
    }

    public void anularVenta() {
        this.estado = "ANULADA";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTasaImpuesto() {
        return tasaImpuesto;
    }

    public void setTasaImpuesto(BigDecimal tasaImpuesto) {
        this.tasaImpuesto = tasaImpuesto;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
}
