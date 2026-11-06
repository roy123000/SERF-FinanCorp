
package com.financorp.serf.service;

import com.financorp.serf.entity.Usuario;
import com.financorp.serf.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado: " + id));
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado con el correo: " + correo));
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException(
                    "El correo electrónico ya está registrado.");
        }

        if (usuario.getContrasena() == null
                || usuario.getContrasena().isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria.");
        }

        usuario.setContrasena(
                passwordEncoder.encode(usuario.getContrasena()));

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(
            Long id, Usuario datosUsuario) {

        Usuario usuario = obtenerUsuario(id);

        if (!usuario.getCorreo().equals(datosUsuario.getCorreo())
                && usuarioRepository.existsByCorreo(
                        datosUsuario.getCorreo())) {
            throw new RuntimeException(
                    "El correo electrónico ya está registrado.");
        }

        usuario.actualizarDatos(
                datosUsuario.getNombre(),
                datosUsuario.getCorreo()
        );

        usuario.setRol(datosUsuario.getRol());
        usuario.setEstado(datosUsuario.getEstado());

        if (datosUsuario.getContrasena() != null
                && !datosUsuario.getContrasena().isBlank()) {
            usuario.setContrasena(
                    passwordEncoder.encode(
                            datosUsuario.getContrasena()));
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerUsuario(id);
        usuarioRepository.delete(usuario);
    }
}
