
package com.financorp.serf.patterns.creational;

import com.financorp.serf.entity.PlantillaReporte;

public class ReportePrototype {

    private PlantillaReporte plantilla;

    public ReportePrototype(PlantillaReporte plantilla) {
        if (plantilla == null) {
            throw new IllegalArgumentException(
                    "La plantilla no puede ser nula.");
        }

        this.plantilla = plantilla;
    }

    // Clona la plantilla original
    public PlantillaReporte clonar() {
        return plantilla.clonarPlantilla();
    }

    // Configura una copia sin modificar la original
    public PlantillaReporte configurarCopia(
            String nombre,
            String pais) {

        PlantillaReporte copia = clonar();

        copia.aplicarPlantilla(nombre, pais);

        return copia;
    }

    // Obtiene la plantilla original
    public PlantillaReporte obtenerCopia() {
        return plantilla.clonarPlantilla();
    }

    // Permite reemplazar la plantilla original
    public void clonarPlantilla(PlantillaReporte nuevaPlantilla) {
        if (nuevaPlantilla == null) {
            throw new IllegalArgumentException(
                    "La plantilla no puede ser nula.");
        }

        this.plantilla = nuevaPlantilla;
    }
}
