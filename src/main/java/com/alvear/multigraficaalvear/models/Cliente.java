package com.alvear.multigraficaalvear.models;

public class Cliente {

    private int id;
    private String nombreCliente;
    private String telefono;
    private String cuit;
    private String correo;
    private String detalle; // Nueva variable

    public Cliente() {
    }

    public Cliente(String nombreCliente, String telefono, String cuit, String correo, String detalle) {
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.cuit = cuit;
        this.correo = correo;
        this.detalle = detalle;
    }

    public Cliente(int id, String nombreCliente, String telefono, String cuit, String correo, String detalle) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.cuit = cuit;
        this.correo = correo;
        this.detalle = detalle;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    @Override
    public String toString() {
        return nombreCliente;
    }
}