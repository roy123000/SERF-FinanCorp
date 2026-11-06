
package com.financorp.serf.controller;

import com.financorp.serf.entity.Reporte;
import com.financorp.serf.service.ReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> listarReportes() {
        return ResponseEntity.ok(
                reporteService.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReporte(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                reporteService.obtenerReporte(id));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Reporte>> buscarPorPais(
            @PathVariable String pais) {
        return ResponseEntity.ok(
                reporteService.buscarPorPais(pais));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Reporte>> buscarPorTipo(
            @PathVariable String tipo) {
        return ResponseEntity.ok(
                reporteService.buscarPorTipo(tipo));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reporte>> buscarPorEstado(
            @PathVariable String estado) {
        return ResponseEntity.ok(
                reporteService.buscarPorEstado(estado));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Reporte>> buscarPorPaisYTipo(
            @RequestParam String pais,
            @RequestParam String tipo) {
        return ResponseEntity.ok(
                reporteService.buscarPorPaisYTipo(pais, tipo));
    }

    @PostMapping
    public ResponseEntity<Reporte> registrarReporte(
            @RequestBody Reporte reporte) {

        Reporte nuevoReporte =
                reporteService.registrarReporte(reporte);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoReporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizarReporte(
            @PathVariable Long id,
            @RequestBody Reporte reporte) {

        return ResponseEntity.ok(
                reporteService.actualizarReporte(id, reporte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(
            @PathVariable Long id) {

        reporteService.eliminarReporte(id);

        return ResponseEntity.noContent().build();
    }
}
