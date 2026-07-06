package com.restaurante.service;

import com.restaurante.entity.Trabajador;
import com.restaurante.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorService(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    public Trabajador guardarTrabajador(Trabajador trabajador) {
        return trabajadorRepository.save(trabajador);
    }

    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }

    public Optional<Trabajador> obtenerTrabajadorPorId(Long id) {
        return trabajadorRepository.findById(id);
    }

    public void eliminarTrabajador(Long id) {
        trabajadorRepository.deleteById(id);
    }

    public Trabajador buscarPorDni(String dni) {
        return trabajadorRepository.findByDni(dni);
    }
}
