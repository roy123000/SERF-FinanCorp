
package com.financorp.serf.controller;

import com.financorp.serf.entity.PlantillaReporte;
import com.financorp.serf.service.PlantillaReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plantillas")
@CrossOrigin(origins = "http://localhost:4200")
public class PlantillaReporteController {

    private final PlantillaReporteService plantillaService;

    public PlantillaReporteController(
            PlantillaReporteService plantillaService) {
        this.plantillaService = plantillaService;
    }

    @GetMapping
    public ResponseEntity<List<PlantillaReporte>> listarPlantillas() {
        return ResponseEntity.ok(
                plantillaService.listarPlantillas());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<PlantillaReporte>> listarActivas() {
        return ResponseEntity.ok(
                plantillaService.listarPlantillasActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantillaReporte> obtenerPlantilla(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                plantillaService.obtenerPlantilla(id));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<PlantillaReporte>> buscarPorPais(
            @PathVariable String pais) {
        return ResponseEntity.ok(
                plantillaService.buscarPorPais(pais));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PlantillaReporte>> buscarPorTipo(
            @PathVariable String tipo) {
        return ResponseEntity.ok(
                plantillaService.buscarPorTipo(tipo));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PlantillaReporte>> buscarPorPaisYTipo(
            @RequestParam String pais,
            @RequestParam String tipo) {
        return ResponseEntity.ok(
                plantillaService.buscarPorPaisYTipo(pais, tipo));
    }

    @PostMapping
    public ResponseEntity<PlantillaReporte> registrarPlantilla(
            @RequestBody PlantillaReporte plantilla) {

        PlantillaReporte nuevaPlantilla =
                plantillaService.registrarPlantilla(plantilla);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaPlantilla);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantillaReporte> actualizarPlantilla(
            @PathVariable Long id,
            @RequestBody PlantillaReporte plantilla) {

        return ResponseEntity.ok(
                plantillaService.actualizarPlantilla(id, plantilla));
    }

    @PostMapping("/{id}/clonar")
    public ResponseEntity<PlantillaReporte> clonarPlantilla(
            @PathVariable Long id) {

        PlantillaReporte copia =
                plantillaService.clonarPlantilla(id);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(copia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlantilla(
            @PathVariable Long id) {

        plantillaService.eliminarPlantilla(id);

        return ResponseEntity.noContent().build();
    }
}
