package com.restaurante.controller;

import com.restaurante.entity.Plato;
import com.restaurante.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/platos")
public class PlatoWebController {

    @Autowired
    private PlatoService platoService;

    @GetMapping
    public String gestionarPlatos() {
        return "forward:/platos.html";
    }

    @PostMapping("/agregar")
    public String agregarPlato(@RequestParam String nombre,
                               @RequestParam String descripcion,
                               @RequestParam Double precio,
                               @RequestParam(required = false) String categoria,
                               RedirectAttributes redirectAttrs) {
        try {
            Plato plato = new Plato();
            plato.setNombre(nombre);
            plato.setDescripcion(descripcion);
            plato.setPrecio(precio);
            plato.setCategoria(categoria != null ? categoria : "Plato Principal");
            plato.setStock(0);
            plato.setDisponible(true);  // ← Ahora funciona con boolean
            
            platoService.guardarPlato(plato);
            
            redirectAttrs.addFlashAttribute("mensaje", "Plato agregado!");
            redirectAttrs.addFlashAttribute("tipo", "success");
            
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttrs.addFlashAttribute("tipo", "error");
        }
        
        return "redirect:/platos.html?exito=1";
    }

    @GetMapping("/api/listar")
    @ResponseBody
    public List<Plato> listarPlatosApi() {
        return platoService.listarPlatos();
    }
}