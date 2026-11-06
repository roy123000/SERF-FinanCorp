
package com.financorp.serf.patterns.structural;

import com.financorp.serf.entity.PlantillaReporte;
import com.financorp.serf.entity.Reporte;
import com.financorp.serf.patterns.ConfiguracionSistema;
import com.financorp.serf.patterns.creational.ReporteBuilder;

import java.time.LocalDate;

public class ReporteFacade {

    private ReporteBuilder reporteBuilder;
    private ConfiguracionSistema configuracion;
    private PlantillaReporte plantilla;

    public ReporteFacade() {
        this.reporteBuilder = new ReporteBuilder();
        this.configuracion = ConfiguracionSistema.getInstancia();
    }

    // Crea un reporte utilizando el patrón Builder
    public Reporte crearReporte(
            String titulo,
            String tipo,
            String pais,
            LocalDate periodoInicio,
            LocalDate periodoFin,
            String moneda,
            PlantillaReporte plantilla) {

        reporteBuilder.reiniciar();

        String monedaReporte = (moneda == null || moneda.isBlank())
                ? configuracion.getMonedaBase()
                : moneda;

        reporteBuilder
                .conTitulo(titulo)
                .conTipo(tipo)
                .conPais(pais)
                .conPeriodo(periodoInicio, periodoFin)
                .conMoneda(monedaReporte);

        if (plantilla != null) {
            reporteBuilder.conPlantilla(plantilla.getNombre());

            for (String seccion : plantilla.getSecciones()) {
                reporteBuilder.conSeccion(seccion);
            }
        }

        return reporteBuilder.build();
    }

    // Genera el contenido del reporte
    public String generarReporte(Reporte reporte) {

        validarReporte(reporte);

        reporte.generar();

        return reporte.obtenerContenido();
    }

    // Personaliza el reporte agregando una sección
    public void personalizarReporte(
            Reporte reporte,
            String seccion) {

        validarReporte(reporte);

        reporte.agregarSeccion(seccion);
    }

    // Prepara el contenido para su posterior exportación a PDF
    public String exportarPDF(Reporte reporte) {

        validarReporte(reporte);

        return reporte.obtenerContenido();
    }

    // Verifica que el reporte exista
    private void validarReporte(Reporte reporte) {

        if (reporte == null) {
            throw new IllegalArgumentException(
                    "El reporte no puede ser nulo."
            );
        }
    }

    public ConfiguracionSistema getConfiguracion() {
        return configuracion;
    }

    public PlantillaReporte getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(PlantillaReporte plantilla) {
        this.plantilla = plantilla;
    }
}
