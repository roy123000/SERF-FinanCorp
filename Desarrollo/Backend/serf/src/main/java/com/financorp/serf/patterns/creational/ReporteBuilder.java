
package com.financorp.serf.patterns.creational;

import com.financorp.serf.entity.Reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReporteBuilder {

    private Reporte reporte;

    public ReporteBuilder() {
        this.reiniciar();
    }

    // Inicializa un nuevo reporte
    public ReporteBuilder reiniciar() {
        this.reporte = new Reporte();
        return this;
    }

    // Define el título
    public ReporteBuilder conTitulo(String titulo) {
        reporte.setTitulo(titulo);
        return this;
    }

    // Define el tipo de reporte
    public ReporteBuilder conTipo(String tipo) {
        reporte.setTipo(tipo);
        return this;
    }

    // Define el país o filial
    public ReporteBuilder conPais(String pais) {
        reporte.setPais(pais);
        return this;
    }

    // Define el periodo
    public ReporteBuilder conPeriodo(
            LocalDate inicio,
            LocalDate fin) {

        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            throw new IllegalArgumentException(
                    "El periodo del reporte no es válido.");
        }

        reporte.setPeriodoInicio(inicio);
        reporte.setPeriodoFin(fin);

        return this;
    }

    // Define la moneda
    public ReporteBuilder conMoneda(String moneda) {
        reporte.setMoneda(moneda);
        return this;
    }

    // Agrega una plantilla
    public ReporteBuilder conPlantilla(String plantilla) {
        reporte.agregarSeccion("Plantilla: " + plantilla);
        return this;
    }

    // Agrega una sección
    public ReporteBuilder conSeccion(String seccion) {
        reporte.agregarSeccion(seccion);
        return this;
    }

    // Define los ingresos
    public ReporteBuilder conIngresos(BigDecimal ingresos) {
        reporte.setIngresosTotales(ingresos);
        return this;
    }

    // Define los costos
    public ReporteBuilder conCostos(BigDecimal costos) {
        reporte.setCostosTotales(costos);
        return this;
    }

    // Construye el reporte terminado
    public Reporte build() {
        reporte.calcularUtilidadBruta();

        Reporte resultado = reporte;
        reporte = new Reporte();

        return resultado;
    }
}
