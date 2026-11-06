
package com.financorp.serf.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String titulo;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Column(nullable = false, length = 80)
    private String pais;

    private LocalDateTime fechaGeneracion;

    private LocalDate periodoInicio;

    private LocalDate periodoFin;

    @Column(nullable = false, length = 10)
    private String moneda;

    @Column(nullable = false, length = 30)
    private String estado;

    @ElementCollection
    @CollectionTable(
        name = "reporte_secciones",
        joinColumns = @JoinColumn(name = "reporte_id")
    )
    @Column(name = "seccion", length = 200)
    private List<String> secciones = new ArrayList<>();

    @Column(precision = 14, scale = 2)
    private BigDecimal ingresosTotales = BigDecimal.ZERO;

    @Column(precision = 14, scale = 2)
    private BigDecimal costosTotales = BigDecimal.ZERO;

    @Column(precision = 14, scale = 2)
    private BigDecimal utilidadBruta = BigDecimal.ZERO;

    public Reporte() {
        this.fechaGeneracion = LocalDateTime.now();
        this.estado = "BORRADOR";
    }

    public void agregarSeccion(String seccion) {
        if (seccion != null && !seccion.isBlank()) {
            secciones.add(seccion);
        }
    }

    public BigDecimal calcularUtilidadBruta() {
        this.utilidadBruta = ingresosTotales.subtract(costosTotales);
        return utilidadBruta;
    }

    public void generar() {
        calcularUtilidadBruta();
        this.fechaGeneracion = LocalDateTime.now();
        this.estado = "GENERADO";
    }

    public String obtenerContenido() {
        StringBuilder contenido = new StringBuilder();

        contenido.append("REPORTE: ").append(titulo).append("\n");
        contenido.append("Tipo: ").append(tipo).append("\n");
        contenido.append("País: ").append(pais).append("\n");
        contenido.append("Moneda: ").append(moneda).append("\n");
        contenido.append("Periodo: ").append(periodoInicio)
                .append(" - ").append(periodoFin).append("\n");
        contenido.append("Ingresos: ").append(ingresosTotales).append("\n");
        contenido.append("Costos: ").append(costosTotales).append("\n");
        contenido.append("Utilidad bruta: ").append(utilidadBruta).append("\n");

        contenido.append("Secciones:\n");

        for (String seccion : secciones) {
            contenido.append("- ").append(seccion).append("\n");
        }

        return contenido.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDate getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(LocalDate periodoFin) {
        this.periodoFin = periodoFin;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<String> secciones) {
        this.secciones = new ArrayList<>(secciones);
    }

    public BigDecimal getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(BigDecimal ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public BigDecimal getCostosTotales() {
        return costosTotales;
    }

    public void setCostosTotales(BigDecimal costosTotales) {
        this.costosTotales = costosTotales;
    }

    public BigDecimal getUtilidadBruta() {
        return utilidadBruta;
    }

    public void setUtilidadBruta(BigDecimal utilidadBruta) {
        this.utilidadBruta = utilidadBruta;
    }
}
