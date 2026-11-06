
package com.financorp.serf.controller;

import com.financorp.serf.dto.LoginRequest;
import com.financorp.serf.dto.LoginResponse;
import com.financorp.serf.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciarSesion(
            @Valid @RequestBody LoginRequest solicitud) {

        LoginResponse respuesta =
                authService.iniciarSesion(solicitud);

        return ResponseEntity.ok(respuesta);
    }
}
