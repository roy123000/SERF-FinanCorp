
package com.financorp.serf.controller;

import com.financorp.serf.entity.Usuario;
import com.financorp.serf.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(
                usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                usuarioService.obtenerUsuario(id));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> buscarPorCorreo(
            @PathVariable String correo) {
        return ResponseEntity.ok(
                usuarioService.buscarPorCorreo(correo));
    }

    @PostMapping
    public ResponseEntity<Usuario> registrarUsuario(
            @RequestBody Usuario usuario) {
        Usuario nuevoUsuario =
                usuarioService.registrarUsuario(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
