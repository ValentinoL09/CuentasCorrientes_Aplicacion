package com.alvear.multigraficaalvear.models;

import java.time.LocalDate;

public class Venta {

    private int id;
    private int clienteId;
    private String descripcion;
    private double montoTotal;
    private double montoRecibido;
    private LocalDate fecha;
    private String detalle;
    private String nombreCliente;

    public Venta() {
    }

    public Venta(int clienteId, String descripcion, double montoTotal, double montoRecibido, LocalDate fecha, String detalle) {
        this.clienteId = clienteId;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.montoRecibido = montoRecibido;
        this.fecha = fecha;
        this.detalle = detalle;
    }

    public Venta(int id, int clienteId, String descripcion, double montoTotal, double montoRecibido, LocalDate fecha, String detalle) {
        this.id = id;
        this.clienteId = clienteId;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.montoRecibido = montoRecibido;
        this.fecha = fecha;
        this.detalle = detalle;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(double montoRecibido) { this.montoRecibido = montoRecibido; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}