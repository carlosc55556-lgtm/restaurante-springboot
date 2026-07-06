package com.restaurante.controller;

import com.restaurante.entity.Reserva;
import com.restaurante.service.ReservaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> listar() {
        return reservaService.listar();
    }

    @PostMapping
    public Reserva crear(@RequestBody Reserva reserva) {
        return reservaService.guardar(reserva);
    }

    @GetMapping("/{id}")
    public Reserva obtener(@PathVariable Long id) {
        return reservaService.buscarPorId(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Reserva actualizar(@PathVariable Long id, @RequestBody Reserva reserva) {
        reserva.setId(id);
        return reservaService.guardar(reserva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
    }
}
