package com.restaurante.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // Método para comprobante de venta (ya lo tenías)
    public void enviarComprobanteVenta(String emailCliente, String nombreCliente,
                                        String numeroComprobante, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(emailCliente);
            helper.setSubject("Comprobante de Pago - " + numeroComprobante);
            
            String mensajeHtml = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2196f3;'>¡Gracias por tu compra!</h2>" +
                "<p>Hola <strong>" + nombreCliente + "</strong>,</p>" +
                "<p>Adjunto encontrarás tu comprobante de pago <strong>" + numeroComprobante + "</strong>.</p>" +
                "<p>Esperamos verte pronto nuevamente.</p><br>" +
                "<p style='color: #888;'>Restaurante Eclipse - Sistema Web</p>" +
                "</body></html>";
            
            helper.setText(mensajeHtml, true);
            helper.addAttachment("Comprobante_" + numeroComprobante + ".pdf", 
                new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }

    // ← NUEVO: Método genérico para enviar recibo
    public void enviarRecibo(String emailCliente, String asunto, byte[] pdfBytes, String nombreArchivo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(emailCliente);
            helper.setSubject(asunto);
            
            String mensajeHtml = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2196f3;'>¡Gracias por tu visita!</h2>" +
                "<p>Adjunto encontrarás tu recibo.</p>" +
                "<p>Esperamos verte pronto nuevamente.</p><br>" +
                "<p style='color: #888;'>Restaurante Eclipse - Sistema Web</p>" +
                "</body></html>";
            
            helper.setText(mensajeHtml, true);
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar recibo: " + e.getMessage(), e);
        }
    }
}