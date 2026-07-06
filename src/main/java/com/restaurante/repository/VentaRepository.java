package com.restaurante.repository;

import com.restaurante.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByPedido_Cliente_Id(Long clienteId);
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    Optional<Venta> findByPedidoId(Long pedidoId); // <-- NUEVO
}