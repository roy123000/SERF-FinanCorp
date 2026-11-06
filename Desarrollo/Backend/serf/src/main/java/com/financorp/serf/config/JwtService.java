
package com.financorp.serf.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${serf.jwt.secret}")
    private String secret;

    @Value("${serf.jwt.expiration}")
    private long expiration;

    private SecretKey obtenerClave() {
        byte[] clave = secret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(clave);
    }

    public String generarToken(String correo, String rol) {

        Date fechaActual = new Date();

        Date fechaExpiracion = new Date(
                fechaActual.getTime() + expiration);

        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)
                .issuedAt(fechaActual)
                .expiration(fechaExpiracion)
                .signWith(obtenerClave())
                .compact();
    }

    public String obtenerCorreo(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    public String obtenerRol(String token) {
        return obtenerClaim(token,
                claims -> claims.get("rol", String.class));
    }

    public Date obtenerFechaExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    public boolean validarToken(String token, String correo) {

        String correoToken = obtenerCorreo(token);

        return correoToken.equals(correo)
                && obtenerFechaExpiracion(token).after(new Date());
    }

    private <T> T obtenerClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }
}
