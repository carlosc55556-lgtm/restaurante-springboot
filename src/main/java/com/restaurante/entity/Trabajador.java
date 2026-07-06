package com.restaurante.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trabajadores")
public class Trabajador {

    @Id
    @Column(name = "id_usuario")
    private Long id;  // <-- SIN @GeneratedValue

    @MapsId
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String nombre;
    private String apellido;
    private String cargo;
    private String dni;
    private String puesto;
    private String telefono;

    // ========== GETTERS Y SETTERS ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}