
package com.financorp.serf.patterns;

import java.math.BigDecimal;

public class ConfiguracionSistema {

    // Instancia única del patrón Singleton
    private static ConfiguracionSistema instancia;

    // Parámetros generales del sistema SERF
    private String monedaBase;
    private String idioma;
    private String formatoFecha;
    private BigDecimal tasaImpuesto;

    // Constructor privado
    private ConfiguracionSistema() {
        this.monedaBase = "PEN";
        this.idioma = "Español";
        this.formatoFecha = "dd/MM/yyyy";
        this.tasaImpuesto = new BigDecimal("18.00");
    }

    // Obtiene la única instancia de configuración
    public static synchronized ConfiguracionSistema getInstancia() {

        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }

        return instancia;
    }

    // Actualiza la configuración general
    public void actualizarConfiguracion(
            String monedaBase,
            String idioma,
            String formatoFecha,
            BigDecimal tasaImpuesto) {

        this.monedaBase = monedaBase;
        this.idioma = idioma;
        this.formatoFecha = formatoFecha;
        this.tasaImpuesto = tasaImpuesto;
    }

    // Devuelve la configuración actual
    public String obtenerConfiguracion() {

        return "Moneda: " + monedaBase
                + ", Idioma: " + idioma
                + ", Formato de fecha: " + formatoFecha
                + ", Tasa de impuesto: " + tasaImpuesto + "%";
    }

    // Métodos getters
    public String getMonedaBase() {
        return monedaBase;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public BigDecimal getTasaImpuesto() {
        return tasaImpuesto;
    }
}
