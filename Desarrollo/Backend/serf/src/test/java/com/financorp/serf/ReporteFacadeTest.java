
package com.financorp.serf;

import com.financorp.serf.entity.PlantillaReporte;
import com.financorp.serf.entity.Reporte;
import com.financorp.serf.patterns.structural.ReporteFacade;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReporteFacadeTest {

    @Test
    void debeCrearUnReporteFinanciero() {

        ReporteFacade facade = new ReporteFacade();

        Reporte reporte = facade.crearReporte(
                "Reporte financiero corporativo",
                "Financiero",
                "Perú",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                "PEN",
                null
        );

        assertNotNull(reporte);
        assertEquals("Reporte financiero corporativo", reporte.getTitulo());
        assertEquals("Financiero", reporte.getTipo());
        assertEquals("Perú", reporte.getPais());
        assertEquals("PEN", reporte.getMoneda());
    }

    @Test
    void debeCrearUnReporteUtilizandoUnaPlantilla() {

        PlantillaReporte plantilla = new PlantillaReporte();

        plantilla.setNombre("Plantilla corporativa");
        plantilla.agregarSeccion("Ingresos y ventas");
        plantilla.agregarSeccion("Costos y gastos");

        ReporteFacade facade = new ReporteFacade();

        Reporte reporte = facade.crearReporte(
                "Reporte de Perú",
                "Financiero",
                "Perú",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                "PEN",
                plantilla
        );

        assertNotNull(reporte);
        assertEquals(3, reporte.getSecciones().size());
        assertTrue(reporte.getSecciones().contains(
                "Plantilla: Plantilla corporativa"
        ));
    }

    @Test
    void debePersonalizarUnReporte() {

        ReporteFacade facade = new ReporteFacade();

        Reporte reporte = facade.crearReporte(
                "Reporte financiero",
                "Financiero",
                "Perú",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                "PEN",
                null
        );

        facade.personalizarReporte(reporte, "Resumen ejecutivo");

        assertTrue(reporte.getSecciones().contains("Resumen ejecutivo"));
    }

    @Test
    void debeGenerarElContenidoDelReporte() {

        ReporteFacade facade = new ReporteFacade();

        Reporte reporte = facade.crearReporte(
                "Reporte financiero",
                "Financiero",
                "Perú",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 6, 30),
                "PEN",
                null
        );

        String contenido = facade.generarReporte(reporte);

        assertNotNull(contenido);
        assertTrue(contenido.contains("Reporte financiero"));
        assertEquals("GENERADO", reporte.getEstado());
    }

    @Test
    void noDebePermitirGenerarUnReporteNulo() {

        ReporteFacade facade = new ReporteFacade();

        assertThrows(
                IllegalArgumentException.class,
                () -> facade.generarReporte(null)
        );
    }
}
