
package com.financorp.serf.controller;

import com.financorp.serf.entity.Producto;
import com.financorp.serf.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(
                productoService.listarProductos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarProductosActivos() {
        return ResponseEntity.ok(
                productoService.listarProductosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                productoService.obtenerProducto(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(
            @PathVariable String codigo) {
        return ResponseEntity.ok(
                productoService.buscarPorCodigo(codigo));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @PathVariable String categoria) {
        return ResponseEntity.ok(
                productoService.buscarPorCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<Producto> registrarProducto(
            @RequestBody Producto producto) {

        Producto nuevoProducto =
                productoService.registrarProducto(producto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return ResponseEntity.ok(
                productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id) {

        productoService.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }
}
