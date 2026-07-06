package com.restaurante.dto;

import java.time.LocalDate;
import java.util.List;

public class ReciboRequest {
    
    private String clienteNombre;
    private String clienteEmail;
    private String emailCliente;      // ← ALIAS para compatibilidad
    private String nombreCliente;     // ← ALIAS para compatibilidad
    private String numeroPedido;      // ← NUEVO
    private LocalDate fecha;
    private List<ItemRecibo> items;
    private Double total;

    // Getters y Setters
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    // Alias para ReciboController
    public String getEmailCliente() { 
        return emailCliente != null ? emailCliente : clienteEmail; 
    }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public String getNombreCliente() { 
        return nombreCliente != null ? nombreCliente : clienteNombre; 
    }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public List<ItemRecibo> getItems() { return items; }
    public void setItems(List<ItemRecibo> items) { this.items = items; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}