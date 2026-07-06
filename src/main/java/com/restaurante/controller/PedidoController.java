package com.restaurante.controller;

import com.restaurante.entity.DetallePedido;
import com.restaurante.entity.Pedido;
import com.restaurante.entity.Plato;
import com.restaurante.entity.Venta;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.repository.PlatoRepository;
import com.restaurante.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private VentaRepository ventaRepository;  // <-- AGREGADO para poder eliminar ventas asociadas

    // ========== LISTAR TODOS ==========
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Pedido p : pedidos) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", p.getId());
            dto.put("nombreCliente", p.getNombreCliente());
            dto.put("numeroMesa", p.getNumeroMesa());
            dto.put("estado", p.getEstado());
            dto.put("fecha", p.getFecha());

            double total = detallePedidoRepository.findByPedidoId(p.getId()).stream()
                .mapToDouble(d -> (d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0)
                                * (d.getCantidad() != null ? d.getCantidad() : 1))
                .sum();
            dto.put("total", total);

            resultado.add(dto);
        }

        return ResponseEntity.ok(resultado);
    }

    // ========== OBTENER POR ID ==========
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            Map<String, Object> dto = new HashMap<>();
            dto.put("id", pedido.getId());
            dto.put("nombreCliente", pedido.getNombreCliente());
            dto.put("numeroMesa", pedido.getNumeroMesa());
            dto.put("estado", pedido.getEstado());
            dto.put("fecha", pedido.getFecha());

            List<Map<String, Object>> detallesDto = new ArrayList<>();
            for (DetallePedido d : pedido.getDetalles()) {
                Map<String, Object> det = new HashMap<>();
                det.put("id", d.getId());
                det.put("cantidad", d.getCantidad());
                det.put("precioUnitario", d.getPrecioUnitario());
                det.put("notas", d.getNotas());
                if (d.getPlato() != null) {
                    Map<String, Object> platoDto = new HashMap<>();
                    platoDto.put("id", d.getPlato().getId());
                    platoDto.put("nombre", d.getPlato().getNombre());
                    platoDto.put("precio", d.getPlato().getPrecio());
                    det.put("plato", platoDto);
                }
                detallesDto.add(det);
            }
            dto.put("detalles", detallesDto);

            double total = detallesDto.stream()
                .mapToDouble(d -> {
                    double precio = d.get("precioUnitario") != null ? ((Number) d.get("precioUnitario")).doubleValue() : 0;
                    int cant = d.get("cantidad") != null ? ((Number) d.get("cantidad")).intValue() : 1;
                    return precio * cant;
                })
                .sum();
            dto.put("total", total);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ========== CAMBIAR ESTADO (POST para evitar 405) ==========
    @PostMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            String nuevoEstado = payload.get("estado");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("Estado requerido");
            }

            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);

            Map<String, Object> dto = new HashMap<>();
            dto.put("id", pedido.getId());
            dto.put("estado", pedido.getEstado());
            dto.put("mensaje", "Estado actualizado a " + nuevoEstado);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ========== CREAR PEDIDO ==========
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> payload) {
        try {
            Pedido pedido = new Pedido();
            pedido.setFecha(LocalDateTime.now());

            Object estadoObj = payload.get("estado");
            pedido.setEstado(estadoObj != null ? estadoObj.toString() : "PENDIENTE");

            Object nombreClienteObj = payload.get("nombreCliente");
            if (nombreClienteObj != null) {
                pedido.setNombreCliente(nombreClienteObj.toString());
            }

            Object numeroMesaObj = payload.get("numeroMesa");
            if (numeroMesaObj != null) {
                pedido.setNumeroMesa(numeroMesaObj.toString());
            }

            Pedido guardado = pedidoRepository.save(pedido);

            if (payload.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

                for (Map<String, Object> item : items) {
                    Object platoIdObj = item.get("platoId");
                    if (platoIdObj == null) continue;
                    Long platoId = Long.valueOf(platoIdObj.toString());

                    Object cantidadObj = item.get("cantidad");
                    Integer cantidad = cantidadObj != null ? Integer.valueOf(cantidadObj.toString()) : 1;

                    Object notasObj = item.get("notas");
                    String notas = notasObj != null ? notasObj.toString() : null;

                    Plato plato = platoRepository.findById(platoId).orElse(null);

                    if (plato != null) {
                        DetallePedido detalle = new DetallePedido();
                        detalle.setPedido(guardado);
                        detalle.setPlato(plato);
                        detalle.setCantidad(cantidad);
                        detalle.setPrecioUnitario(plato.getPrecio());
                        detalle.setNotas(notas);
                        detallePedidoRepository.save(detalle);
                    }
                }
            }

            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ========== AGREGAR ITEM ==========
    @PostMapping(value = "/{pedidoId}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> agregarItem(@PathVariable Long pedidoId, @RequestBody Map<String, Object> item) {
        try {
            Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            Long platoId = Long.valueOf(item.get("platoId").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());
            String notas = item.containsKey("notas") ? item.get("notas").toString() : null;

            Plato plato = platoRepository.findById(platoId)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));

            List<DetallePedido> existentes = detallePedidoRepository.findByPedidoId(pedidoId);
            DetallePedido existente = existentes.stream()
                .filter(d -> d.getPlato().getId().equals(platoId))
                .findFirst()
                .orElse(null);

            if (existente != null) {
                existente.setCantidad(existente.getCantidad() + cantidad);
                detallePedidoRepository.save(existente);
            } else {
                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setPlato(plato);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(plato.getPrecio());
                detalle.setNotas(notas);
                detallePedidoRepository.save(detalle);
            }

            return ResponseEntity.ok(pedidoRepository.findById(pedidoId).orElse(pedido));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ========== ELIMINAR (con eliminación de venta asociada) ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        try {
            if (!pedidoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // OPCION 1: Eliminar primero la venta asociada (si existe)
            Optional<Venta> venta = ventaRepository.findByPedidoId(id);
            if (venta.isPresent()) {
                ventaRepository.delete(venta.get());
                System.out.println(">>> Venta asociada eliminada: " + venta.get().getId());
            }

            pedidoRepository.deleteById(id);
            System.out.println(">>> Pedido " + id + " eliminado");
            return ResponseEntity.ok(Map.of("mensaje", "Pedido y venta asociada eliminados"));

        } catch (Exception e) {
            System.out.println(">>> ERROR eliminando: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}