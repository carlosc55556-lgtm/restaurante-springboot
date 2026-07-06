package com.restaurante.service;

import com.restaurante.entity.Plato;
import com.restaurante.repository.PlatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    // Crear o actualizar un plato
    public Plato guardarPlato(Plato plato) {
        return platoRepository.save(plato);
    }

    // Listar todos los platos
    public List<Plato> listarPlatos() {
        return platoRepository.findAll();
    }

    // Buscar plato por ID
    public Optional<Plato> obtenerPlatoPorId(Long id) {
        return platoRepository.findById(id);
    }

    // Eliminar plato por ID
    public void eliminarPlato(Long id) {
        platoRepository.deleteById(id);
    }

    // Buscar platos por nombre (ejemplo de método adicional)
    public List<Plato> buscarPorNombre(String nombre) {
        return platoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
