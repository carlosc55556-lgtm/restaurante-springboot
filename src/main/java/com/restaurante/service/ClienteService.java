package com.restaurante.service;

import com.restaurante.entity.Cliente;
import com.restaurante.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }
}
