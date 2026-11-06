
package com.financorp.serf.controller;

import com.financorp.serf.entity.Filial;
import com.financorp.serf.service.FilialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filiales")
@CrossOrigin(origins = "http://localhost:4200")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping
    public ResponseEntity<List<Filial>> listarFiliales() {
        return ResponseEntity.ok(
                filialService.listarFiliales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filial> obtenerFilial(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                filialService.obtenerFilial(id));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Filial>> buscarPorPais(
            @PathVariable String pais) {
        return ResponseEntity.ok(
                filialService.buscarPorPais(pais));
    }

    @PostMapping
    public ResponseEntity<Filial> registrarFilial(
            @RequestBody Filial filial) {
        Filial nuevaFilial =
                filialService.registrarFilial(filial);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaFilial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filial> actualizarFilial(
            @PathVariable Long id,
            @RequestBody Filial filial) {
        return ResponseEntity.ok(
                filialService.actualizarFilial(id, filial));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFilial(
            @PathVariable Long id) {
        filialService.eliminarFilial(id);

        return ResponseEntity.noContent().build();
    }
}
