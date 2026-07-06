package com.restaurante.service;

import com.restaurante.entity.Calificacion;
import com.restaurante.repository.CalificacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;

    public CalificacionService(CalificacionRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }

    public List<Calificacion> listar() {
        return calificacionRepository.findAll();
    }

    public Calificacion guardar(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }

    public Optional<Calificacion> buscarPorId(Long id) {
        return calificacionRepository.findById(id);
    }

    public void eliminar(Long id) {
        calificacionRepository.deleteById(id);
    }
}
