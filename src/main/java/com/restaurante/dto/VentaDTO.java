package com.restaurante.dto;

import java.time.LocalDateTime;

public class VentaDTO {
    
    private Long id;
    private String tipoComprobante;
    private String serie;
    private String numero;
    private Double total;
    private LocalDateTime fecha;
    private String estado;
    private boolean emailEnviado;
    private String comprobante;  // ← AGREGAR ESTE CAMPO
    private String clienteNombre;
    private String clienteEmail;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isEmailEnviado() { return emailEnviado; }
    public void setEmailEnviado(boolean emailEnviado) { this.emailEnviado = emailEnviado; }

    public String getComprobante() { return comprobante; }  // ← AGREGAR
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }  // ← AGREGAR

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
}