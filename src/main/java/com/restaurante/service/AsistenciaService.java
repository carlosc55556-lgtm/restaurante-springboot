package com.restaurante.service;

import com.restaurante.entity.Asistencia;
import com.restaurante.repository.AsistenciaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {
    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public List<Asistencia> listar() {
        return asistenciaRepository.findAll();
    }

    public Asistencia guardar(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    public Optional<Asistencia> buscarPorId(Long id) {
        return asistenciaRepository.findById(id);
    }

    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }
}
