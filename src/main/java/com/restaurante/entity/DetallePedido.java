package com.restaurante.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido_platos")  // o "detalle_pedido", según tu BD
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "plato_id", nullable = false)
    private Plato plato;

    private Integer cantidad;
    
    @Column(name = "precio_unitario")
    private Double precioUnitario;  // <-- IMPORTANTE: no debe ser null

    private String notas;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Plato getPlato() { return plato; }
    public void setPlato(Plato plato) { this.plato = plato; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}