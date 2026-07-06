package com.restaurante.repository;

import com.restaurante.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    
    // Método que falta:
    List<Plato> findByNombreContainingIgnoreCase(String nombre);
    
    // Otros métodos útiles que puedes necesitar:
    List<Plato> findByCategoria(String categoria);
    List<Plato> findByDisponibleTrue();
    List<Plato> findByDisponibleFalse();
}