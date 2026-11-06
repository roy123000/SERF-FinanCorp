
package com.financorp.serf.controller;

import com.financorp.serf.patterns.ConfiguracionSistema;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/configuracion")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfiguracionController {

    private final ConfiguracionSistema configuracion =
            ConfiguracionSistema.getInstancia();

    // Consultar la configuración actual
    @GetMapping
    public ConfiguracionResponse obtenerConfiguracion() {

        return new ConfiguracionResponse(
                configuracion.getMonedaBase(),
                configuracion.getIdioma(),
                configuracion.getFormatoFecha(),
                configuracion.getTasaImpuesto()
        );
    }

    // Actualizar la configuración general
    @PutMapping
    public ConfiguracionResponse actualizarConfiguracion(
            @RequestBody ConfiguracionRequest solicitud) {

        if (solicitud.monedaBase() == null
                || solicitud.monedaBase().isBlank()
                || solicitud.idioma() == null
                || solicitud.idioma().isBlank()
                || solicitud.formatoFecha() == null
                || solicitud.formatoFecha().isBlank()
                || solicitud.tasaImpuesto() == null) {

            throw new IllegalArgumentException(
                    "Todos los campos de configuración son obligatorios."
            );
        }

        if (solicitud.tasaImpuesto().compareTo(BigDecimal.ZERO) < 0
                || solicitud.tasaImpuesto().compareTo(
                        new BigDecimal("100")) > 0) {

            throw new IllegalArgumentException(
                    "La tasa de impuesto debe estar entre 0 y 100."
            );
        }

        configuracion.actualizarConfiguracion(
                solicitud.monedaBase(),
                solicitud.idioma(),
                solicitud.formatoFecha(),
                solicitud.tasaImpuesto()
        );

        return obtenerConfiguracion();
    }

    public record ConfiguracionRequest(
            String monedaBase,
            String idioma,
            String formatoFecha,
            BigDecimal tasaImpuesto
    ) {}

    public record ConfiguracionResponse(
            String monedaBase,
            String idioma,
            String formatoFecha,
            BigDecimal tasaImpuesto
    ) {}
}
