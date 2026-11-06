
package com.financorp.serf.patterns.structural;

public class SeccionReporte implements ComponenteReporte {

    private Long id;
    private String titulo;
    private String contenido;
    private Integer orden;

    public SeccionReporte(Long id, String titulo, String contenido, Integer orden) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.orden = orden;
    }

    @Override
    public void mostrar() {
        System.out.println("Sección: " + titulo);
        System.out.println(contenido);
    }

    @Override
    public String obtenerContenido() {
        return titulo + "\n" + contenido;
    }

    @Override
    public void agregar(ComponenteReporte componente) {
        throw new UnsupportedOperationException(
                "Una sección individual no puede contener otras secciones."
        );
    }

    @Override
    public void eliminar(ComponenteReporte componente) {
        throw new UnsupportedOperationException(
                "Una sección individual no puede eliminar subsecciones."
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
