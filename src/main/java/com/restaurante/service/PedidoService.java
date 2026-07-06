package com.restaurante.service;

import com.restaurante.dto.PedidoRequestDTO;
import com.restaurante.entity.DetallePedido;
import com.restaurante.entity.Pedido;
import com.restaurante.entity.Plato;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Transactional
    public Pedido crearPedido(PedidoRequestDTO dto) {
        // 1. Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setNumeroMesa(dto.getNumeroMesa());
        pedido.setEstado("PENDIENTE");
        pedido.setFecha(LocalDateTime.now());

        // 2. Guardar primero para obtener el ID (necesario para la FK en detalles)
        pedido = pedidoRepository.save(pedido);

        // 3. Crear los detalles
        if (dto.getItems() != null) {
            for (var item : dto.getItems()) {
                Plato plato = platoRepository.findById(item.getPlatoId())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + item.getPlatoId()));

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);              // FK al pedido
                detalle.setPlato(plato);                // FK al plato
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(plato.getPrecio());  // <-- CLAVE: precio del plato
                detalle.setNotas(item.getNotas());

                // Agregar a la lista del pedido
                pedido.getDetalles().add(detalle);
            }
        }

        // 4. Guardar de nuevo (cascade ALL guardará los detalles automáticamente)
        return pedidoRepository.save(pedido);
    }
}