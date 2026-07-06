package com.restaurante.controller;

import com.restaurante.entity.Asistencia;
import com.restaurante.service.AsistenciaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {
    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @GetMapping
    public List<Asistencia> listar() {
        return asistenciaService.listar();
    }

    @PostMapping
    public Asistencia crear(@RequestBody Asistencia asistencia) {
        return asistenciaService.guardar(asistencia);
    }

    @GetMapping("/{id}")
    public Asistencia obtener(@PathVariable Long id) {
        return asistenciaService.buscarPorId(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
    }
}
