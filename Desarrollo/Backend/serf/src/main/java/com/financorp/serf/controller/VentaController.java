
package com.financorp.serf.controller;

import com.financorp.serf.entity.Venta;
import com.financorp.serf.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(
                ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ventaService.obtenerVenta(id));
    }

    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<Venta>> buscarPorFilial(
            @PathVariable Long filialId) {
        return ResponseEntity.ok(
                ventaService.buscarPorFilial(filialId));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Venta>> buscarPorPeriodo(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        return ResponseEntity.ok(
                ventaService.buscarPorPeriodo(
                        fechaInicio, fechaFin));
    }

    @PostMapping
    public ResponseEntity<Venta> registrarVenta(
            @RequestBody Venta venta) {

        Venta nuevaVenta =
                ventaService.registrarVenta(venta);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaVenta);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anularVenta(
            @PathVariable Long id) {

        ventaService.anularVenta(id);

        return ResponseEntity.noContent().build();
    }
}
