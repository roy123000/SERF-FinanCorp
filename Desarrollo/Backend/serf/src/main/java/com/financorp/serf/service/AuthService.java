
package com.financorp.serf.service;

import com.financorp.serf.config.JwtService;
import com.financorp.serf.dto.LoginRequest;
import com.financorp.serf.dto.LoginResponse;
import com.financorp.serf.entity.Usuario;
import com.financorp.serf.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse iniciarSesion(LoginRequest solicitud) {

        Usuario usuario = usuarioRepository
                .findByCorreo(solicitud.getCorreo())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Correo o contraseña incorrectos."));

        if (usuario.getEstado() == null || !usuario.getEstado()) {
            throw new BadCredentialsException(
                    "El usuario se encuentra inactivo.");
        }

        if (usuario.getContrasena() == null
                || !passwordEncoder.matches(
                        solicitud.getContrasena(),
                        usuario.getContrasena())) {

            throw new BadCredentialsException(
                    "Correo o contraseña incorrectos.");
        }

        String token = jwtService.generarToken(
                usuario.getCorreo(),
                usuario.getRol()
        );

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol()
        );
    }
}
