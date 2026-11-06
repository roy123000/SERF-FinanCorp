
package com.financorp.serf.patterns.structural;

import java.time.LocalDate;

public class EncabezadoDecorador implements DecoradorReporte {

    private ComponenteReporte componente;
    private String tituloEncabezado;
    private String subtitulo;
    private LocalDate fecha;

    public EncabezadoDecorador(
            ComponenteReporte componente,
            String tituloEncabezado,
            String subtitulo,
            LocalDate fecha) {

        if (componente == null) {
            throw new IllegalArgumentException(
                    "El componente no puede ser nulo."
            );
        }

        this.componente = componente;
        this.tituloEncabezado = tituloEncabezado;
        this.subtitulo = subtitulo;
        this.fecha = fecha;
    }

    @Override
    public String generarContenido() {
        return tituloEncabezado + "\n"
                + subtitulo + "\n"
                + "Fecha de generación: " + fecha + "\n\n"
                + componente.obtenerContenido();
    }

    @Override
    public String obtenerDescripcion() {
        return "Reporte con encabezado: " + tituloEncabezado;
    }

    public String getTituloEncabezado() {
        return tituloEncabezado;
    }

    public void setTituloEncabezado(String tituloEncabezado) {
        this.tituloEncabezado = tituloEncabezado;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ComponenteReporte getComponente() {
        return componente;
    }
}
