
package com.financorp.serf.patterns.structural;

import java.util.ArrayList;
import java.util.List;

public class GrupoSecciones implements ComponenteReporte {

    private Long id;
    private String titulo;
    private Integer orden;
    private List<ComponenteReporte> componentes;

    public GrupoSecciones(Long id, String titulo, Integer orden) {
        this.id = id;
        this.titulo = titulo;
        this.orden = orden;
        this.componentes = new ArrayList<>();
    }

    @Override
    public void mostrar() {
        System.out.println("Grupo: " + titulo);

        for (ComponenteReporte componente : componentes) {
            componente.mostrar();
        }
    }

    @Override
    public String obtenerContenido() {
        StringBuilder contenido = new StringBuilder();

        contenido.append(titulo).append("\n");

        for (ComponenteReporte componente : componentes) {
            contenido.append(componente.obtenerContenido())
                     .append("\n");
        }

        return contenido.toString();
    }

    @Override
    public void agregar(ComponenteReporte componente) {
        if (componente == null) {
            throw new IllegalArgumentException(
                    "El componente no puede ser nulo."
            );
        }

        componentes.add(componente);
    }

    @Override
    public void eliminar(ComponenteReporte componente) {
        componentes.remove(componente);
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<ComponenteReporte> getComponentes() {
        return componentes;
    }
}
