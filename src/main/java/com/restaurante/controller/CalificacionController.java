package com.restaurante.controller;

import com.restaurante.entity.Calificacion;
import com.restaurante.service.CalificacionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {
    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @GetMapping
    public List<Calificacion> listar() {
        return calificacionService.listar();
    }

    @PostMapping
    public Calificacion crear(@RequestBody Calificacion calificacion) {
        return calificacionService.guardar(calificacion);
    }

    @GetMapping("/{id}")
    public Calificacion obtener(@PathVariable Long id) {
        return calificacionService.buscarPorId(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        calificacionService.eliminar(id);
    }
}
