
package com.financorp.serf;

import com.financorp.serf.patterns.structural.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DecoradorReporteTest {

    @Test
    void debeAgregarUnEncabezadoAlReporte() {

        SeccionReporte seccion = new SeccionReporte(
                1L, "Ingresos", "Ingresos totales: 450000", 1
        );

        EncabezadoDecorador encabezado = new EncabezadoDecorador(
                seccion,
                "REPORTE FINANCIERO CORPORATIVO",
                "Resultados de FinanCorp",
                LocalDate.of(2026, 11, 5)
        );

        String contenido = encabezado.generarContenido();

        assertTrue(contenido.contains("REPORTE FINANCIERO CORPORATIVO"));
        assertTrue(contenido.contains("Resultados de FinanCorp"));
        assertTrue(contenido.contains("Ingresos totales: 450000"));
    }

    @Test
    void debeAgregarUnGraficoAlReporte() {

        SeccionReporte seccion = new SeccionReporte(
                1L, "Ingresos", "Ingresos totales: 450000", 1
        );

        GraficoDecorador grafico = new GraficoDecorador(
                seccion,
                "Barras",
                "Ingresos por filial",
                "Perú: 120000, Chile: 95000",
                "Estadística"
        );

        String contenido = grafico.generarContenido();

        assertTrue(contenido.contains("Ingresos por filial"));
        assertTrue(contenido.contains("Tipo de gráfico: Barras"));
        assertTrue(contenido.contains("Perú: 120000"));
    }

    @Test
    void debePermitirEncadenarVariosDecoradores() {

        SeccionReporte seccion = new SeccionReporte(
                1L, "Resultados", "Utilidad bruta: 165000", 1
        );

        EncabezadoDecorador encabezado = new EncabezadoDecorador(
                seccion,
                "FINANCORP",
                "Reporte corporativo",
                LocalDate.now()
        );

        GraficoDecorador grafico = new GraficoDecorador(
                encabezado,
                "Circular",
                "Distribución de ingresos",
                "Perú 40%, Chile 60%",
                "Gráfico"
        );

        String contenidoFinal = grafico.generarContenido();

        assertTrue(contenidoFinal.contains("FINANCORP"));
        assertTrue(contenidoFinal.contains("Reporte corporativo"));
        assertTrue(contenidoFinal.contains("Utilidad bruta: 165000"));
        assertTrue(contenidoFinal.contains("Distribución de ingresos"));
    }

    @Test
    void noDebeAceptarUnComponenteNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new EncabezadoDecorador(
                        null, "Título", "Subtítulo", LocalDate.now()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new GraficoDecorador(
                        null, "Barras", "Ingresos", "Datos", "Estadística"
                )
        );
    }
}
