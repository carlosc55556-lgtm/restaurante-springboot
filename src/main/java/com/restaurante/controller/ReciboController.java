package com.restaurante.controller;

import com.restaurante.dto.ReciboRequest;
import com.restaurante.service.AuditoriaService;
import com.restaurante.service.EmailService;
import com.restaurante.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recibo")
@CrossOrigin(origins = "*")
public class ReciboController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuditoriaService auditoriaService;

    @PostMapping("/descargar")
    public ResponseEntity<byte[]> descargarRecibo(@RequestBody ReciboRequest request) {
        try {
            byte[] pdf = pdfService.generarReciboPdf(request);
            String nombreArchivo = "recibo_" + request.getNumeroPedido() + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                .filename(nombreArchivo).build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarReciboPorCorreo(@RequestBody ReciboRequest request) {
        try {
            byte[] pdf = pdfService.generarReciboPdf(request);
            String nombreArchivo = "recibo_" + request.getNumeroPedido() + ".pdf";

            emailService.enviarRecibo(
                request.getEmailCliente(),
                request.getNombreCliente(),
                pdf,
                nombreArchivo
            );

            auditoriaService.registrarEnvioEmail(
                request.getEmailCliente(),
                "Recibo de consumo",
                true,
                "SISTEMA"
            );

            return ResponseEntity.ok("Recibo enviado correctamente a " + request.getEmailCliente());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al enviar recibo: " + e.getMessage());
        }
    }
}