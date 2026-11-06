
package com.financorp.serf.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio")
    @Column(nullable = false, length = 50)
    private String rol;

    @Column(nullable = false)
    private Boolean estado = true;

    public Usuario() {
    }

    public Usuario(String nombre, String correo,
                   String contrasena, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = true;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void iniciarSesion() {
        // La autenticación se gestiona mediante AuthService.
    }

    public void cerrarSesion() {
        // La gestión de sesión se realiza mediante JWT.
    }

    public void actualizarDatos(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }
}
