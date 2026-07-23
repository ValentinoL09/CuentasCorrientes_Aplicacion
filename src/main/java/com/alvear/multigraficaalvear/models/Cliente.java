package com.alvear.multigraficaalvear.models;

public class Cliente {

    private int id;
    private String nombreEmpresa; // Reemplaza a nombreCliente
    private String encargado;     // Nuevo campo opcional
    private String telefono;
    private String telefonoAlternativo; // Nuevo campo
    private String cuit;
    private String correo;
    // Eliminamos la variable "detalle"

    public Cliente() {
    }

    public Cliente(String nombreEmpresa, String encargado, String telefono, String telefonoAlternativo, String cuit, String correo) {
        this.nombreEmpresa = nombreEmpresa;
        this.encargado = encargado;
        this.telefono = telefono;
        this.telefonoAlternativo = telefonoAlternativo;
        this.cuit = cuit;
        this.correo = correo;
    }

    public Cliente(int id, String nombreEmpresa, String encargado, String telefono, String telefonoAlternativo, String cuit, String correo) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.encargado = encargado;
        this.telefono = telefono;
        this.telefonoAlternativo = telefonoAlternativo;
        this.cuit = cuit;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getEncargado() { return encargado; }
    public void setEncargado(String encargado) { this.encargado = encargado; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTelefonoAlternativo() { return telefonoAlternativo; }
    public void setTelefonoAlternativo(String telefonoAlternativo) { this.telefonoAlternativo = telefonoAlternativo; }

    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public String toString() {
        // En los ComboBox se va a ver "Nombre de la Empresa"
        return nombreEmpresa; 
    }
}