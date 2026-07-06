package com.restaurante.controller;

import com.restaurante.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register-trabajador")
    public String registrarTrabajador(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String rol,
            @RequestParam String cargo,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String telefono,
            RedirectAttributes redirectAttrs) {
        
        System.out.println(">>> POST /auth/register-trabajador recibido");
        System.out.println(">>> nombre=" + nombre + ", apellido=" + apellido + ", username=" + username);
        
        try {
            usuarioService.registrarTrabajador(username, password, email, nombre, apellido, cargo, dni, telefono, rol);
            System.out.println(">>> Trabajador registrado OK: " + username);
            return "redirect:/registro-trabajador.html?exito=1";
            
        } catch (Exception e) {
            System.out.println(">>> ERROR al registrar trabajador: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/registro-trabajador.html?error=" + e.getMessage();
        }
    }

    @PostMapping("/register")
    public String registrarCliente(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam String username,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam(required = false) String direccion,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(required = false) String dni,
                                   RedirectAttributes redirectAttrs) {
        try {
            usuarioService.registrarCliente(username, password, email, nombre, apellido, telefono, dni, direccion);
            return "redirect:/login.html?registro=exitoso";
        } catch (Exception e) {
            System.out.println("Error registro cliente: " + e.getMessage());
            return "redirect:/registro.html?error=" + e.getMessage();
        }
    }
}