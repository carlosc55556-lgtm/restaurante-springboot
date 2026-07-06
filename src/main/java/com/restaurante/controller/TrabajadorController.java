package com.restaurante.controller;

import com.restaurante.entity.Trabajador;
import com.restaurante.service.TrabajadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @PostMapping
    public ResponseEntity<Trabajador> crearTrabajador(@RequestBody Trabajador trabajador) {
        return ResponseEntity.ok(trabajadorService.guardarTrabajador(trabajador));
    }

    @GetMapping
    public ResponseEntity<List<Trabajador>> listarTrabajadores() {
        return ResponseEntity.ok(trabajadorService.listarTrabajadores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trabajador> obtenerTrabajador(@PathVariable Long id) {
        return trabajadorService.obtenerTrabajadorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizarTrabajador(@PathVariable Long id, @RequestBody Trabajador trabajador) {
        return trabajadorService.obtenerTrabajadorPorId(id)
                .map(trabajadorExistente -> {
                    trabajadorExistente.setNombre(trabajador.getNombre());
                    trabajadorExistente.setApellido(trabajador.getApellido());
                    trabajadorExistente.setDni(trabajador.getDni());
                    trabajadorExistente.setTelefono(trabajador.getTelefono());
                    trabajadorExistente.setPuesto(trabajador.getPuesto());
                    return ResponseEntity.ok(trabajadorService.guardarTrabajador(trabajadorExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTrabajador(@PathVariable Long id) {
        if (trabajadorService.obtenerTrabajadorPorId(id).isPresent()) {
            trabajadorService.eliminarTrabajador(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
