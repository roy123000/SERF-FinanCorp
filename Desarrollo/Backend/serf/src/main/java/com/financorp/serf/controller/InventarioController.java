
package com.financorp.serf.controller;

import com.financorp.serf.entity.Inventario;
import com.financorp.serf.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventarios() {
        return ResponseEntity.ok(
                inventarioService.listarInventarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerInventario(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                inventarioService.obtenerInventario(id));
    }

    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<Inventario>> buscarPorFilial(
            @PathVariable Long filialId) {
        return ResponseEntity.ok(
                inventarioService.buscarPorFilial(filialId));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Inventario>> buscarPorProducto(
            @PathVariable Long productoId) {
        return ResponseEntity.ok(
                inventarioService.buscarPorProducto(productoId));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Inventario>> consultarStockBajo(
            @RequestParam(defaultValue = "10") Integer cantidad) {
        return ResponseEntity.ok(
                inventarioService.consultarStockBajo(cantidad));
    }

    @PostMapping
    public ResponseEntity<Inventario> registrarInventario(
            @RequestParam Long productoId,
            @RequestParam Long filialId,
            @RequestParam Integer cantidadInicial,
            @RequestParam Integer stockMinimo) {

        Inventario nuevoInventario =
                inventarioService.registrarInventario(
                        productoId,
                        filialId,
                        cantidadInicial,
                        stockMinimo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoInventario);
    }

    @PatchMapping("/{id}/entrada")
    public ResponseEntity<Inventario> registrarEntrada(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(
                inventarioService.registrarEntrada(id, cantidad));
    }

    @PatchMapping("/{id}/salida")
    public ResponseEntity<Inventario> registrarSalida(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(
                inventarioService.registrarSalida(id, cantidad));
    }
}
