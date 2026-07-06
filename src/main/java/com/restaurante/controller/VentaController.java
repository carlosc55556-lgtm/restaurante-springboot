package com.restaurante.controller;

import com.restaurante.dto.VentaDTO;
import com.restaurante.entity.Venta;
import com.restaurante.service.EmailService;
import com.restaurante.service.PdfService;
import com.restaurante.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfService pdfService;

    // ========== LISTAR VENTAS ==========
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listarVentas() {
        try {
            List<Venta> ventas = ventaService.listarTodas();

            List<VentaDTO> dtos = ventas.stream().map(v -> {
                VentaDTO dto = new VentaDTO();
                dto.setId(v.getId());
                dto.setTipoComprobante(v.getTipoComprobante());
                dto.setSerie(v.getSerie());
                dto.setNumero(v.getNumero());
                dto.setTotal(v.getTotal());
                dto.setFecha(v.getFecha());
                dto.setEstado(v.getEstado());
                dto.setEmailEnviado(v.isEmailEnviado());
                dto.setComprobante(v.getComprobante());

                if (v.getPedido() != null && v.getPedido().getCliente() != null) {
                    dto.setClienteNombre(v.getPedido().getCliente().getNombre());
                    dto.setClienteEmail(v.getPedido().getCliente().getEmail());
                } else if (v.getPedido() != null && v.getPedido().getNombreCliente() != null) {
                    dto.setClienteNombre(v.getPedido().getNombreCliente());
                } else {
                    dto.setClienteNombre("Cliente Genérico");
                }

                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== CREAR VENTA DESDE PEDIDO ==========
    @PostMapping("/crear")
    public ResponseEntity<?> crearVenta(@RequestParam Long pedidoId,
                                        @RequestParam String tipoComprobante,
                                        @RequestParam String serie,
                                        @RequestParam String numero) {
        try {
            System.out.println(">>> POST /api/ventas/crear - pedidoId: " + pedidoId);
            Venta venta = ventaService.crearVentaDesdePedido(pedidoId, tipoComprobante, serie, numero);
            System.out.println(">>> Venta creada: " + venta.getComprobante() + ", Total: " + venta.getTotal());

            Map<String, Object> dto = new HashMap<>();
            dto.put("id", venta.getId());
            dto.put("comprobante", venta.getComprobante());
            dto.put("tipoComprobante", venta.getTipoComprobante());
            dto.put("serie", venta.getSerie());
            dto.put("numero", venta.getNumero());
            dto.put("subtotal", venta.getSubtotal());
            dto.put("igv", venta.getIgv());
            dto.put("total", venta.getTotal());
            dto.put("fecha", venta.getFecha());
            dto.put("estado", venta.getEstado());
            dto.put("pedidoId", venta.getPedido() != null ? venta.getPedido().getId() : null);

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            System.out.println(">>> ERROR creando venta: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ========== DESCARGAR PDF ==========
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        try {
            Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            byte[] pdf = pdfService.generarComprobante(venta);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "comprobante_" + venta.getNumero() + ".pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ========== ENVIAR EMAIL (acepta email manual) ==========
    @PostMapping("/{id}/enviar-email")
    public ResponseEntity<?> enviarEmail(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        try {
            Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            String emailCliente = null;
            String nombreCliente = "Cliente";

            // 1. Si el body trae un email, usarlo
            if (body != null && body.containsKey("email") && body.get("email") != null && !body.get("email").isEmpty()) {
                emailCliente = body.get("email");
            }

            // 2. Si no, buscar en el cliente registrado
            if (emailCliente == null || emailCliente.isEmpty()) {
                if (venta.getPedido() != null && venta.getPedido().getCliente() != null) {
                    emailCliente = venta.getPedido().getCliente().getEmail();
                    nombreCliente = venta.getPedido().getCliente().getNombre();
                }
            }

            // 3. Si no hay cliente registrado, usar nombreCliente del pedido
            if (venta.getPedido() != null && venta.getPedido().getNombreCliente() != null) {
                nombreCliente = venta.getPedido().getNombreCliente();
            }

            // 4. Validar que tengamos email
            if (emailCliente == null || emailCliente.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "No hay email disponible. Ingresa un email manualmente.");
                return ResponseEntity.badRequest().body(error);
            }

            byte[] pdf = pdfService.generarComprobante(venta);
            emailService.enviarComprobanteVenta(emailCliente, nombreCliente, venta.getNumero(), pdf);

            venta.setEmailEnviado(true);
            venta.setFechaEnvio(java.time.LocalDateTime.now());
            ventaService.guardar(venta);

            Map<String, String> success = new HashMap<>();
            success.put("mensaje", "Comprobante enviado exitosamente a " + emailCliente);
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al enviar: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}