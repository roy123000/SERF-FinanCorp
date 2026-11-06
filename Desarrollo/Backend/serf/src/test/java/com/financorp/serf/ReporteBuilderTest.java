
package com.financorp.serf;

import com.financorp.serf.entity.Reporte;
import com.financorp.serf.patterns.creational.ReporteBuilder;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReporteBuilderTest {

    @Test
    void debeConstruirUnReporteFinanciero() {

        ReporteBuilder builder = new ReporteBuilder();

        Reporte reporte = builder
                .conTitulo("Reporte financiero corporativo")
                .conTipo("Financiero")
                .conPais("Perú")
                .conPeriodo(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 6, 30))
                .conMoneda("PEN")
                .conSeccion("Resumen ejecutivo")
                .conSeccion("Ingresos y ventas")
                .conIngresos(new BigDecimal("450000"))
                .conCostos(new BigDecimal("285000"))
                .build();

        assertNotNull(reporte);
        assertEquals("Reporte financiero corporativo", reporte.getTitulo());
        assertEquals("Financiero", reporte.getTipo());
        assertEquals("Perú", reporte.getPais());
        assertEquals("PEN", reporte.getMoneda());

        assertEquals(
                new BigDecimal("165000"),
                reporte.getUtilidadBruta());

        assertEquals(2, reporte.getSecciones().size());
    }

    @Test
    void debeRechazarUnPeriodoInvalido() {

        ReporteBuilder builder = new ReporteBuilder();

        assertThrows(IllegalArgumentException.class, () ->
                builder.conPeriodo(
                        LocalDate.of(2026, 6, 30),
                        LocalDate.of(2026, 1, 1))
        );
    }
}
