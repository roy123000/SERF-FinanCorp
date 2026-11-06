
package com.financorp.serf.patterns.structural;

public class GraficoDecorador implements DecoradorReporte {

    private ComponenteReporte componente;
    private String tipoGrafico;
    private String tituloGrafico;
    private String datos;
    private String tipoVisualizacion;

    public GraficoDecorador(
            ComponenteReporte componente,
            String tipoGrafico,
            String tituloGrafico,
            String datos,
            String tipoVisualizacion) {

        if (componente == null) {
            throw new IllegalArgumentException(
                    "El componente no puede ser nulo."
            );
        }

        this.componente = componente;
        this.tipoGrafico = tipoGrafico;
        this.tituloGrafico = tituloGrafico;
        this.datos = datos;
        this.tipoVisualizacion = tipoVisualizacion;
    }

    @Override
    public String generarContenido() {
        return componente.obtenerContenido()
                + "\n"
                + tituloGrafico + "\n"
                + "Tipo de gráfico: " + tipoGrafico + "\n"
                + "Visualización: " + tipoVisualizacion + "\n"
                + "Datos: " + datos + "\n";
    }

    @Override
    public String obtenerDescripcion() {
        return "Reporte con gráfico: " + tituloGrafico;
    }

    public String generarGrafico() {
        return "Gráfico " + tipoGrafico
                + " - " + tituloGrafico
                + " - Datos: " + datos;
    }

    public String generarTabla() {
        return "Tabla financiera: " + tituloGrafico
                + " - Datos: " + datos;
    }

    public ComponenteReporte getComponente() {
        return componente;
    }

    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    public String getTituloGrafico() {
        return tituloGrafico;
    }

    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getTipoVisualizacion() {
        return tipoVisualizacion;
    }

    public void setTipoVisualizacion(String tipoVisualizacion) {
        this.tipoVisualizacion = tipoVisualizacion;
    }
}
