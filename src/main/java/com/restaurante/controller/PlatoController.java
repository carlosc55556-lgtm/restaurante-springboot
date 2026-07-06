package com.restaurante.controller;

import com.restaurante.entity.Plato;
import com.restaurante.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    @PostMapping
    public ResponseEntity<?> crearPlato(@RequestBody Plato plato) {
        try {
            Plato nuevo = platoService.guardarPlato(plato);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Plato> listarPlatos() {
        return platoService.listarPlatos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plato> obtenerPlato(@PathVariable Long id) {
        return platoService.obtenerPlatoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPlato(@PathVariable Long id, @RequestBody Plato plato) {
        return platoService.obtenerPlatoPorId(id)
                .map(existente -> {
                    existente.setNombre(plato.getNombre());
                    existente.setDescripcion(plato.getDescripcion());
                    existente.setPrecio(plato.getPrecio());
                    existente.setCategoria(plato.getCategoria());
                    return ResponseEntity.ok(platoService.guardarPlato(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long id) {
        if (platoService.obtenerPlatoPorId(id).isPresent()) {
            platoService.eliminarPlato(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}