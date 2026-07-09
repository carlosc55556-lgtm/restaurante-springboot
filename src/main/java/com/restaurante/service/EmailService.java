package com.restaurante.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    // Método para comprobante de venta (ya lo tenías)
    public void enviarComprobanteVenta(String emailCliente, String nombreCliente,
                                        String numeroComprobante, byte[] pdfBytes) {
        String asunto = "Comprobante de Pago - " + numeroComprobante;

        String mensajeHtml = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2196f3;'>¡Gracias por tu compra!</h2>" +
                "<p>Hola <strong>" + nombreCliente + "</strong>,</p>" +
                "<p>Adjunto encontrarás tu comprobante de pago <strong>" + numeroComprobante + "</strong>.</p>" +
                "<p>Esperamos verte pronto nuevamente.</p><br>" +
                "<p style='color: #888;'>Restaurante Eclipse - Sistema Web</p>" +
                "</body></html>";

        enviarConAdjunto(emailCliente, asunto, mensajeHtml, pdfBytes,
                "Comprobante_" + numeroComprobante + ".pdf");
    }

    // Método genérico para enviar recibo
    public void enviarRecibo(String emailCliente, String asunto, byte[] pdfBytes, String nombreArchivo) {
        String mensajeHtml = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2196f3;'>¡Gracias por tu visita!</h2>" +
                "<p>Adjunto encontrarás tu recibo.</p>" +
                "<p>Esperamos verte pronto nuevamente.</p><br>" +
                "<p style='color: #888;'>Restaurante Eclipse - Sistema Web</p>" +
                "</body></html>";

        enviarConAdjunto(emailCliente, asunto, mensajeHtml, pdfBytes, nombreArchivo);
    }

    // Lógica común: arma el JSON y llama a la API de Brevo
    private void enviarConAdjunto(String emailDestino, String asunto, String htmlContent,
                                   byte[] pdfBytes, String nombreArchivo) {
        try {
            String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("sender", Map.of("name", senderName, "email", senderEmail));
            body.put("to", List.of(Map.of("email", emailDestino)));
            body.put("subject", asunto);
            body.put("htmlContent", htmlContent);
            body.put("attachment", List.of(Map.of("content", pdfBase64, "name", nombreArchivo)));

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", brevoApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_API_URL, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Brevo respondió con error: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email vía Brevo: " + e.getMessage(), e);
        }
    }
}