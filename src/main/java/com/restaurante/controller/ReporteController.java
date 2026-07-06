package com.restaurante.controller;

import com.restaurante.entity.Venta;
import com.restaurante.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping("/diario")
    public ResponseEntity<Map<String, Object>> reporteDiario() {
        LocalDateTime inicio = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);
        return construirReporte(inicio, fin, "diario");
    }

    @GetMapping("/semanal")
    public ResponseEntity<Map<String, Object>> reporteSemanal() {
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusDays(7);
        return construirReporte(inicio, fin, "semanal");
    }

    @GetMapping("/mensual")
    public ResponseEntity<Map<String, Object>> reporteMensual() {
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime inicio = fin.minusDays(30);
        return construirReporte(inicio, fin, "mensual");
    }

    private ResponseEntity<Map<String, Object>> construirReporte(LocalDateTime inicio, LocalDateTime fin, String tipo) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);
        
        double totalVentas = ventas.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0)
                .sum();
        
        double totalIGV = ventas.stream()
                .mapToDouble(v -> v.getIgv() != null ? v.getIgv() : 0)
                .sum();

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("tipo", tipo);
        reporte.put("periodoInicio", inicio.toString());
        reporte.put("periodoFin", fin.toString());
        reporte.put("cantidadVentas", ventas.size());
        reporte.put("totalVentas", String.format("S/ %.2f", totalVentas));
        reporte.put("totalIGV", String.format("S/ %.2f", totalIGV));
        reporte.put("promedioVenta", ventas.isEmpty() ? 0 : String.format("S/ %.2f", totalVentas / ventas.size()));
        reporte.put("ventas", ventas);

        return ResponseEntity.ok(reporte);
    }
}