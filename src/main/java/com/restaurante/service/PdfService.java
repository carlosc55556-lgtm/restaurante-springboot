package com.restaurante.service;

import com.restaurante.dto.ReciboRequest;
import com.restaurante.entity.Venta;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    // Método para Venta
    public byte[] generarComprobante(Venta venta) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("COMPROBANTE DE PAGO", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);
            
            document.add(new Paragraph("Tipo: " + (venta.getTipoComprobante() != null ? venta.getTipoComprobante() : "BOLETA")));
            document.add(new Paragraph("Serie: " + (venta.getSerie() != null ? venta.getSerie() : "B001")));
            document.add(new Paragraph("Número: " + (venta.getNumero() != null ? venta.getNumero() : "00000001")));
            document.add(new Paragraph("Fecha: " + venta.getFecha()));
            document.add(Chunk.NEWLINE);
            
            if (venta.getPedido() != null && venta.getPedido().getCliente() != null) {
                document.add(new Paragraph("Cliente: " + venta.getPedido().getCliente().getNombre()));
                document.add(new Paragraph("Email: " + venta.getPedido().getCliente().getEmail()));
                document.add(Chunk.NEWLINE);
            }
            
            document.add(new Paragraph("Subtotal: S/ " + venta.getSubtotal()));
            document.add(new Paragraph("IGV: S/ " + venta.getIgv()));
            document.add(new Paragraph("TOTAL: S/ " + venta.getTotal()));
            
            document.close();
            
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    // Método para ReciboRequest
    public byte[] generarReciboPdf(ReciboRequest recibo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("RECIBO DE PAGO", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);
            
            document.add(new Paragraph("Cliente: " + recibo.getClienteNombre()));
            document.add(new Paragraph("Email: " + recibo.getClienteEmail()));
            document.add(new Paragraph("Fecha: " + recibo.getFecha()));
            document.add(Chunk.NEWLINE);
            
            // Items
            document.add(new Paragraph("DETALLE:", new Font(Font.HELVETICA, 14, Font.BOLD)));
            for (var item : recibo.getItems()) {
                document.add(new Paragraph("- " + item.getDescripcion() + " x" + item.getCantidad() 
                    + " = S/ " + item.getSubtotal()));
            }
            document.add(Chunk.NEWLINE);
            
            document.add(new Paragraph("TOTAL: S/ " + recibo.getTotal(), 
                new Font(Font.HELVETICA, 16, Font.BOLD)));
            
            document.close();
            
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error generando recibo PDF: " + e.getMessage(), e);
        }
    }
}