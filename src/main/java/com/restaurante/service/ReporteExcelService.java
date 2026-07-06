package com.restaurante.service;

import com.restaurante.entity.Venta;
import com.restaurante.repository.VentaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReporteExcelService {

    @Autowired
    private VentaRepository ventaRepository;

    public byte[] generarReporteVentasExcel(LocalDate fechaInicio, LocalDate fechaFin) throws Exception {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        
        java.util.List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle moneyStyle = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat();
        moneyStyle.setDataFormat(df.getFormat("S/ #,##0.00"));
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE VENTAS - RESTAURANTE ECLIPSE");
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
        
        Row periodRow = sheet.createRow(1);
        periodRow.createCell(0).setCellValue("Período: " + 
            fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " +
            fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));
        
        String[] headers = {"ID Venta", "Fecha", "Cliente", "Subtotal", "IGV (18%)", "Total"};
        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 4;
        double totalGeneral = 0;
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(venta.getId());
            
            row.createCell(1).setCellValue(
                venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
            
            // ← CORREGIDO: venta.getCliente() → venta.getPedido().getCliente()
            String nombreCliente = "N/A";
            if (venta.getPedido() != null && venta.getPedido().getCliente() != null) {
                nombreCliente = venta.getPedido().getCliente().getNombre() + " " + 
                    (venta.getPedido().getCliente().getApellido() != null ? venta.getPedido().getCliente().getApellido() : "");
            }
            row.createCell(2).setCellValue(nombreCliente);
            
            Cell subtotalCell = row.createCell(3);
            subtotalCell.setCellValue(venta.getSubtotal());
            subtotalCell.setCellStyle(moneyStyle);
            
            Cell igvCell = row.createCell(4);
            igvCell.setCellValue(venta.getIgv());
            igvCell.setCellStyle(moneyStyle);
            
            Cell totalCell = row.createCell(5);
            totalCell.setCellValue(venta.getTotal());
            totalCell.setCellStyle(moneyStyle);
            
            totalGeneral += venta.getTotal();
        }
        
        Row totalRow = sheet.createRow(rowNum + 1);
        Cell totalLabelCell = totalRow.createCell(4);
        totalLabelCell.setCellValue("TOTAL GENERAL:");
        totalLabelCell.setCellStyle(headerStyle);
        
        Cell totalValueCell = totalRow.createCell(5);
        totalValueCell.setCellValue(totalGeneral);
        totalValueCell.setCellStyle(moneyStyle);
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        return baos.toByteArray();
    }
}