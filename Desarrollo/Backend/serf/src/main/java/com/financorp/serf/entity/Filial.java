
package com.financorp.serf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "filiales")
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la filial es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El país es obligatorio")
    @Column(nullable = false, length = 80)
    private String pais;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 200)
    private String direccion;

    @Column(nullable = false)
    private Boolean estado = true;

    public Filial() {
    }

    public Filial(String nombre, String pais,
                  String ciudad, String direccion) {
        this.nombre = nombre;
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.estado = true;
    }

    public void registrarFilial() {
        this.estado = true;
    }

    public void actualizarFilial(
            String nombre,
            String pais,
            String ciudad,
            String direccion) {

        this.nombre = nombre;
        this.pais = pais;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public void consultarFilial() {
        System.out.println("Filial: " + nombre + " - " + pais);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
