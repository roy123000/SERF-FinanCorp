
package com.financorp.serf.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String encabezado = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(7);

        try {
            String correo = jwtService.obtenerCorreo(token);
            String rol = jwtService.obtenerRol(token);

            if (correo != null
                    && rol != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtService.validarToken(token, correo)) {

                    String autoridad = rol.startsWith("ROLE_")
                            ? rol
                            : "ROLE_" + rol;

                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    correo,
                                    null,
                                    List.of(new SimpleGrantedAuthority(autoridad))
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }

        } catch (JwtException | IllegalArgumentException excepcion) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
