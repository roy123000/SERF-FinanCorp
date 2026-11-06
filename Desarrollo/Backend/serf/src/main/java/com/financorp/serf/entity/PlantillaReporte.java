
package com.financorp.serf.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plantillas_reporte")
public class PlantillaReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 80)
    private String pais;

    @Column(length = 80)
    private String tipoReporte;

    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Boolean estado;

    @ElementCollection
    @CollectionTable(
        name = "plantilla_secciones",
        joinColumns = @JoinColumn(name = "plantilla_id")
    )
    @Column(name = "seccion", length = 200)
    private List<String> secciones = new ArrayList<>();

    public PlantillaReporte() {
        this.fechaCreacion = LocalDateTime.now();
        this.version = 1;
        this.estado = true;
    }

    public void agregarSeccion(String seccion) {
        if (seccion != null && !seccion.isBlank()) {
            secciones.add(seccion);
        }
    }

    public void eliminarSeccion(String seccion) {
        secciones.remove(seccion);
    }

    public PlantillaReporte clonarPlantilla() {

        PlantillaReporte copia = new PlantillaReporte();

        copia.setId(this.id);
        copia.setNombre(this.nombre);
        copia.setDescripcion(this.descripcion);
        copia.setPais(this.pais);
        copia.setTipoReporte(this.tipoReporte);
        copia.setVersion(this.version);
        copia.setEstado(this.estado);
        copia.setSecciones(new ArrayList<>(this.secciones));

        return copia;
    }

    public void aplicarPlantilla(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<String> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<String> secciones) {
        this.secciones = new ArrayList<>(secciones);
    }
}
