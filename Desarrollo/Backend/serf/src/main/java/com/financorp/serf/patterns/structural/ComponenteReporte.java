
package com.financorp.serf.patterns.structural;

public interface ComponenteReporte {

    void mostrar();

    String obtenerContenido();

    void agregar(ComponenteReporte componente);

    void eliminar(ComponenteReporte componente);
}
