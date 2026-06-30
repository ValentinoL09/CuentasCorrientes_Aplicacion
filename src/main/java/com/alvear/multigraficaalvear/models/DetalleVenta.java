package com.alvear.multigraficaalvear.models;

public class DetalleVenta {

    private int id;
    private int ventaId;
    private int servicioId;
    private int cantidad;
    private double precioUnitario;
    private String nombreServicio;

    public DetalleVenta() {
    }

    public DetalleVenta(int ventaId, int servicioId, int cantidad, double precioUnitario) {
        this.ventaId = ventaId;
        this.servicioId = servicioId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public DetalleVenta(int id, int ventaId, int servicioId, int cantidad, double precioUnitario) {
        this.id = id;
        this.ventaId = ventaId;
        this.servicioId = servicioId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = servicioId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public double getSubtotal() {
        return this.cantidad * this.precioUnitario;
    }
}
