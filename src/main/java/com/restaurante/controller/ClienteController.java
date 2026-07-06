package com.restaurante.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.restaurante.entity.Cliente;
import com.restaurante.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteService.guardar(cliente);
    }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable Long id) {
        return clienteService.buscarPorId(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        return clienteService.guardar(cliente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    @GetMapping("/buscar/{dni}")
    public Cliente buscarPorDni(@PathVariable String dni) {
        return clienteService.buscarPorDni(dni);
    }
}
