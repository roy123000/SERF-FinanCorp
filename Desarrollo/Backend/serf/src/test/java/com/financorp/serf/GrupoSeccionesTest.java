
package com.financorp.serf;

import com.financorp.serf.patterns.structural.ComponenteReporte;
import com.financorp.serf.patterns.structural.GrupoSecciones;
import com.financorp.serf.patterns.structural.SeccionReporte;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrupoSeccionesTest {

    @Test
    void debeAgregarSeccionesAUnGrupo() {

        GrupoSecciones grupo = new GrupoSecciones(
                1L, "Reporte financiero", 1
        );

        SeccionReporte ingresos = new SeccionReporte(
                1L, "Ingresos", "Ingresos totales: 450000", 1
        );

        SeccionReporte costos = new SeccionReporte(
                2L, "Costos", "Costos totales: 285000", 2
        );

        grupo.agregar(ingresos);
        grupo.agregar(costos);

        assertEquals(2, grupo.getComponentes().size());
    }

    @Test
    void debePermitirGruposDentroDeOtrosGrupos() {

        GrupoSecciones principal = new GrupoSecciones(
                1L, "Reporte financiero", 1
        );

        GrupoSecciones ventas = new GrupoSecciones(
                2L, "Ingresos y ventas", 1
        );

        SeccionReporte nacional = new SeccionReporte(
                3L, "Ventas nacionales", "Total: 120000", 1
        );

        ventas.agregar(nacional);
        principal.agregar(ventas);

        assertEquals(1, principal.getComponentes().size());

        ComponenteReporte componente = principal.getComponentes().get(0);

        assertInstanceOf(GrupoSecciones.class, componente);
    }

    @Test
    void debeEliminarUnaSeccionDelGrupo() {

        GrupoSecciones grupo = new GrupoSecciones(
                1L, "Reporte financiero", 1
        );

        SeccionReporte ingresos = new SeccionReporte(
                1L, "Ingresos", "Total: 450000", 1
        );

        grupo.agregar(ingresos);
        grupo.eliminar(ingresos);

        assertTrue(grupo.getComponentes().isEmpty());
    }

    @Test
    void unaSeccionIndividualNoDebePermitirAgregarOtrasSecciones() {

        SeccionReporte seccion = new SeccionReporte(
                1L, "Ingresos", "Total: 450000", 1
        );

        SeccionReporte subseccion = new SeccionReporte(
                2L, "Ventas nacionales", "Total: 120000", 1
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> seccion.agregar(subseccion)
        );
    }

    @Test
    void noDebePermitirAgregarComponentesNulos() {

        GrupoSecciones grupo = new GrupoSecciones(
                1L, "Reporte financiero", 1
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> grupo.agregar(null)
        );
    }
}
