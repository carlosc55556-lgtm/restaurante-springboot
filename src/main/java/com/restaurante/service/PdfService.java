package com.restaurante.service;

import com.restaurante.dto.ReciboRequest;
import com.restaurante.entity.Venta;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.restaurante.entity.DetallePedido;
import com.restaurante.entity.Pedido;

import com.lowagie.text.pdf.PdfPTable;

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

            // Fuentes
            Font empresaFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);
            Font negritaFont = new Font(Font.HELVETICA, 11, Font.BOLD);

            // Empresa
            Paragraph empresa = new Paragraph(
                    "RESTAURANTE ECLIPSE",
                    empresaFont
            );
            empresa.setAlignment(Element.ALIGN_CENTER);
            document.add(empresa);

            Paragraph direccion = new Paragraph(
                    "Comprobante de pago",
                    tituloFont
            );
            direccion.setAlignment(Element.ALIGN_CENTER);
            document.add(direccion);

            document.add(Chunk.NEWLINE);


            // Datos comprobante
            document.add(new Paragraph(
                    "Tipo: " + (venta.getTipoComprobante() != null 
                    ? venta.getTipoComprobante() : "BOLETA"),
                    normalFont));

            document.add(new Paragraph(
                    "Serie: " + (venta.getSerie() != null 
                    ? venta.getSerie() : "B001"),
                    normalFont));

            document.add(new Paragraph(
                    "Número: " + (venta.getNumero() != null 
                    ? venta.getNumero() : "00000001"),
                    normalFont));

            document.add(new Paragraph(
                    "Fecha: " + venta.getFecha(),
                    normalFont));

            document.add(Chunk.NEWLINE);


            // Cliente
            document.add(new Paragraph(
                    "DATOS DEL CLIENTE",
                    negritaFont
            ));

            String nombreCliente = "Cliente general";

            if (venta.getPedido() != null) {

                if (venta.getPedido().getCliente() != null) {
                    nombreCliente = venta.getPedido()
                            .getCliente()
                            .getNombre();
                } else if (venta.getPedido().getNombreCliente() != null) {
                    nombreCliente = venta.getPedido()
                            .getNombreCliente();
                }
            }

            document.add(new Paragraph(
                    "Cliente: " + nombreCliente,
                    normalFont
            ));

            document.add(Chunk.NEWLINE);


            // Tabla detalle
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);

            Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD);

            Color pastel = new Color(210, 235, 230);

            PdfPCell productoHeader = new PdfPCell(new Phrase("Producto", headerFont));
            PdfPCell cantidadHeader = new PdfPCell(new Phrase("Cantidad", headerFont));
            PdfPCell precioHeader = new PdfPCell(new Phrase("Precio", headerFont));
            PdfPCell subtotalHeader = new PdfPCell(new Phrase("Subtotal", headerFont));

            productoHeader.setBackgroundColor(pastel);
            cantidadHeader.setBackgroundColor(pastel);
            precioHeader.setBackgroundColor(pastel);
            subtotalHeader.setBackgroundColor(pastel);

            productoHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            cantidadHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            precioHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            subtotalHeader.setHorizontalAlignment(Element.ALIGN_CENTER);

            tabla.addCell(productoHeader);
            tabla.addCell(cantidadHeader);
            tabla.addCell(precioHeader);
            tabla.addCell(subtotalHeader);


            if (venta.getPedido() != null) {

                for (DetallePedido detalle :
                        venta.getPedido().getDetalles()) {

                    double subtotalDetalle =
                            detalle.getPrecioUnitario()
                            * detalle.getCantidad();

                    tabla.addCell(
                            detalle.getPlato().getNombre()
                    );

                    tabla.addCell(
                            detalle.getCantidad().toString()
                    );

                    tabla.addCell(
                            "S/ " + detalle.getPrecioUnitario()
                    );

                    tabla.addCell(
                            "S/ " + subtotalDetalle
                    );
                }
            }

            document.add(tabla);

            document.add(Chunk.NEWLINE);


            // Totales
            document.add(new Paragraph(
                    "Subtotal: S/ " + venta.getSubtotal(),
                    normalFont
            ));

            document.add(new Paragraph(
                    "IGV: S/ " + venta.getIgv(),
                    normalFont
            ));

            Paragraph total = new Paragraph(
                    "TOTAL: S/ " + venta.getTotal(),
                    new Font(Font.HELVETICA, 14, Font.BOLD)
            );

            document.add(total);


            document.add(Chunk.NEWLINE);

            Paragraph gracias = new Paragraph(
                    "Gracias por su preferencia - RESTAURANTE ECLIPSE",
                    normalFont
            );

            gracias.setAlignment(Element.ALIGN_CENTER);
            document.add(gracias);


            document.close();

            return baos.toByteArray();


        } catch (Exception e) {
            throw new RuntimeException(
                    "Error generando PDF: " + e.getMessage(),
                    e
            );
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