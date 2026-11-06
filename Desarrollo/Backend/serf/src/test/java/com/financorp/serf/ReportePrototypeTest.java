
package com.financorp.serf;

import com.financorp.serf.entity.PlantillaReporte;
import com.financorp.serf.patterns.creational.ReportePrototype;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportePrototypeTest {

    @Test
    void debeClonarUnaPlantillaSinModificarLaOriginal() {

        PlantillaReporte original = new PlantillaReporte();
        original.setNombre("Reporte corporativo");
        original.setPais("España");
        original.setTipoReporte("Financiero");
        original.agregarSeccion("Ingresos");
        original.agregarSeccion("Costos");

        ReportePrototype prototype = new ReportePrototype(original);

        PlantillaReporte copia = prototype.configurarCopia(
                "Reporte financiero Perú",
                "Perú"
        );

        assertNotSame(original, copia);
        assertEquals("Reporte corporativo", original.getNombre());
        assertEquals("España", original.getPais());

        assertEquals("Reporte financiero Perú", copia.getNombre());
        assertEquals("Perú", copia.getPais());

        assertEquals(2, copia.getSecciones().size());
    }

    @Test
    void debeMantenerIndependientesLasSeccionesDeLaCopia() {

        PlantillaReporte original = new PlantillaReporte();
        original.agregarSeccion("Ingresos");

        ReportePrototype prototype = new ReportePrototype(original);

        PlantillaReporte copia = prototype.clonar();

        copia.agregarSeccion("Costos");

        assertEquals(1, original.getSecciones().size());
        assertEquals(2, copia.getSecciones().size());
    }

    @Test
    void noDebeAceptarUnaPlantillaNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ReportePrototype(null)
        );
    }
}
