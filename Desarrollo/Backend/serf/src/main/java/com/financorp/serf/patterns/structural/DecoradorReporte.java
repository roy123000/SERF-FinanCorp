
package com.financorp.serf.patterns.structural;

public interface DecoradorReporte extends ComponenteReporte {

    String generarContenido();

    String obtenerDescripcion();

    @Override
    default void mostrar() {
        System.out.println(generarContenido());
    }

    @Override
    default String obtenerContenido() {
        return generarContenido();
    }

    @Override
    default void agregar(ComponenteReporte componente) {
        throw new UnsupportedOperationException(
                "La operación agregar no está disponible en este decorador."
        );
    }

    @Override
    default void eliminar(ComponenteReporte componente) {
        throw new UnsupportedOperationException(
                "La operación eliminar no está disponible en este decorador."
        );
    }
}
