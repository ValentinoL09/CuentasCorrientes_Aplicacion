package com.alvear.multigraficaalvear.models;

public class Cliente {

    private int id;
    private String nombreCliente;
    private String telefono;
    private String cuit;
    private String correo;

    public Cliente() {
    }

    public Cliente(String nombreCliente, String telefono, String cuit, String correo) {
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.cuit = cuit;
        this.correo = correo;
    }

    public Cliente(int id, String nombreCliente, String telefono, String cuit, String correo) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.cuit = cuit;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombreCliente;
    }
}
