
package com.financorp.serf.config;

import com.financorp.serf.entity.Usuario;
import com.financorp.serf.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdministradorInicializador {

    @Value("${SERF_ADMIN_PASSWORD:}")
    private String contrasenaAdministrador;

    @Bean
    public CommandLineRunner crearAdministradorInicial(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String correo = "admin@financorp.com";

            // Evitar registrar nuevamente al administrador.
            if (usuarioRepository.existsByCorreo(correo)) {
                System.out.println(
                    "El administrador inicial ya se encuentra registrado."
                );
                return;
            }

            // Verificar que exista una contraseña configurada.
            if (contrasenaAdministrador == null
                    || contrasenaAdministrador.isBlank()) {
                throw new IllegalStateException(
                    "No se ha configurado SERF_ADMIN_PASSWORD."
                );
            }

            Usuario administrador = new Usuario();

            administrador.setNombre("Administrador SERF");
            administrador.setCorreo(correo);
            administrador.setContrasena(
                passwordEncoder.encode(contrasenaAdministrador)
            );
            administrador.setRol("ADMINISTRADOR");
            administrador.setEstado(true);

            usuarioRepository.save(administrador);

            System.out.println(
                "Administrador inicial creado correctamente: " + correo
            );
        };
    }
}
