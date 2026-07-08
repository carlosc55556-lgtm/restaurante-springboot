package com.restaurante.controller;

import com.restaurante.entity.Trabajador;
import com.restaurante.service.TrabajadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @GetMapping("/exportar")
    public void exportarExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=trabajadores.xlsx");

        List<Trabajador> trabajadores = trabajadorService.listarTrabajadores();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Trabajadores");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nombres");
        header.createCell(2).setCellValue("Apellidos");
        header.createCell(3).setCellValue("Cargo");
        header.createCell(4).setCellValue("DNI");
        header.createCell(5).setCellValue("Teléfono");

        int rowNum = 1;

        for (Trabajador t : trabajadores) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(t.getId() != null ? t.getId() : 0);
            row.createCell(1).setCellValue(t.getNombre() != null ? t.getNombre() : "");
            row.createCell(2).setCellValue(t.getApellido() != null ? t.getApellido() : "");
            row.createCell(3).setCellValue(t.getCargo() != null ? t.getCargo() : "");
            row.createCell(4).setCellValue(t.getDni() != null ? t.getDni() : "");
            row.createCell(5).setCellValue(t.getTelefono() != null ? t.getTelefono() : "");
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
