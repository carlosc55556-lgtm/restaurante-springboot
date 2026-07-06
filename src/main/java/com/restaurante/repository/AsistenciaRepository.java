package com.restaurante.repository;

import com.restaurante.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByTrabajadorId(Long trabajadorId);
}
