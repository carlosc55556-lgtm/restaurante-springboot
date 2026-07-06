package com.restaurante.dto;

import java.util.List;

public class PedidoRequestDTO {
    private String nombreCliente;
    private String numeroMesa;
    private List<DetalleRequestDTO> items;

    // Getters y Setters
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(String numeroMesa) { this.numeroMesa = numeroMesa; }

    public List<DetalleRequestDTO> getItems() { return items; }
    public void setItems(List<DetalleRequestDTO> items) { this.items = items; }
}