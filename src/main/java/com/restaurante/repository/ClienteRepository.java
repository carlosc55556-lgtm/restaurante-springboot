package com.restaurante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurante.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByDni(String dni);
}
