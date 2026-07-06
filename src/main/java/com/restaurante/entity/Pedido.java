package com.restaurante.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private String estado;
    
    // NUEVO: Nombre del cliente o número de mesa
    private String nombreCliente;
    private String numeroMesa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Relación con detalles (líneas de pedido)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(String numeroMesa) { this.numeroMesa = numeroMesa; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
    
    // Método helper para calcular total
    public double getTotal() {
        return detalles.stream()
            .mapToDouble(d -> (d.getPrecioUnitario() != null ? d.getPrecioUnitario() : 0) 
                            * (d.getCantidad() != null ? d.getCantidad() : 1))
            .sum();
    }
}