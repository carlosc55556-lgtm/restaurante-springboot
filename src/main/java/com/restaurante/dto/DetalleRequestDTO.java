package com.restaurante.dto;

public class DetalleRequestDTO {
    private Long platoId;
    private Integer cantidad;
    private String notas;

    // Getters y Setters
    public Long getPlatoId() { return platoId; }
    public void setPlatoId(Long platoId) { this.platoId = platoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}