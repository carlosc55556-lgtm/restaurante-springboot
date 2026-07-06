package com.restaurante.service;

import com.restaurante.entity.DetallePedido;
import com.restaurante.entity.Pedido;
import com.restaurante.entity.Venta;
import com.restaurante.repository.DetallePedidoRepository;
import com.restaurante.repository.PedidoRepository;
import com.restaurante.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }

    @Transactional
    public Venta crearVentaDesdePedido(Long pedidoId, String tipoComprobante, String serie, String numero) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));
        
        // COMENTADO: Validación de venta duplicada (para pruebas)
        /*
        if (ventaRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new RuntimeException("Ya existe una venta para este pedido");
        }
        */
        
        // ========== CALCULAR TOTAL DESDE DETALLES ==========
        double subtotal = 0;
        
        // Opción 1: Usar detalles del pedido (si están cargados)
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            subtotal = pedido.getDetalles().stream()
                .mapToDouble(d -> {
                    double precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0;
                    int cantidad = d.getCantidad() != null ? d.getCantidad() : 1;
                    return precio * cantidad;
                })
                .sum();
            System.out.println(">>> Total calculado desde pedido.getDetalles(): " + subtotal);
        } 
        // Opción 2: Buscar detalles por repositorio
        else {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);
            subtotal = detalles.stream()
                .mapToDouble(d -> {
                    double precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0;
                    int cantidad = d.getCantidad() != null ? d.getCantidad() : 1;
                    return precio * cantidad;
                })
                .sum();
            System.out.println(">>> Total calculado desde DetallePedidoRepository: " + subtotal);
        }
        
        // Validar que el pedido tenga items
        if (subtotal == 0) {
            throw new RuntimeException("El pedido no tiene platos/bebidas o el total es S/ 0.00");
        }
        
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        System.out.println(">>> VentaService - Subtotal: " + subtotal + ", IGV: " + igv + ", Total: " + total);

        Venta venta = new Venta();
        venta.setPedido(pedido);
        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(subtotal);
        venta.setIgv(igv);
        venta.setTotal(total);
        venta.setTipoComprobante(tipoComprobante);
        venta.setSerie(serie);
        venta.setNumero(numero);
        venta.setComprobante(tipoComprobante + " " + serie + "-" + numero);
        venta.setEstado("COMPLETADA");
        venta.setEmailEnviado(false);
        venta.setCreatedAt(LocalDateTime.now());

        return ventaRepository.save(venta);
    }
}