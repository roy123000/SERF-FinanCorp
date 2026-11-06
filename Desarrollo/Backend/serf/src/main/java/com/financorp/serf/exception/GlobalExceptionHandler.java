
package com.financorp.serf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarArgumentoInvalido(
            IllegalArgumentException excepcion) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                excepcion.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(
            RuntimeException excepcion) {

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                excepcion.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorInterno(
            Exception excepcion) {

        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor.");
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus estado,
            String mensaje) {

        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("fecha", LocalDateTime.now());
        respuesta.put("estado", estado.value());
        respuesta.put("error", estado.getReasonPhrase());
        respuesta.put("mensaje", mensaje);

        return ResponseEntity
                .status(estado)
                .body(respuesta);
    }
}
