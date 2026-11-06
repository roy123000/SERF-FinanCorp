
package com.financorp.serf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 100)
    private String categoria;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 80)
    private String paisOrigen;

    @NotNull(message = "El precio de compra es obligatorio")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioCompra;

    @NotNull(message = "El precio de venta es obligatorio")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Boolean estado = true;

    public Producto() {
    }

    public Producto(String codigo, String nombre, String categoria,
                    String descripcion, String paisOrigen,
                    BigDecimal precioCompra, BigDecimal precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.paisOrigen = paisOrigen;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.estado = true;
    }

    public void registrarProducto() {
        this.estado = true;
    }

    public void actualizarProducto(String nombre, String categoria,
                                    String descripcion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public void consultarProducto() {
        System.out.println("Producto: " + nombre + " - " + codigo);
    }

    public void modificarPrecio(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void desactivarProducto() {
        this.estado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
