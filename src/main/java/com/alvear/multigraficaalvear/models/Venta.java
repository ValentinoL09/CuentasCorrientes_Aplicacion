package com.alvear.multigraficaalvear.models;

import java.time.LocalDate;

public class Venta {

    private int id;
    private int clienteId;
    private int tipoVentaId;
    private String descripcion;
    private double montoTotal;
    private double montoRecibido;
    private LocalDate fecha;

    public Venta() {
    }

    public Venta(int clienteId, int tipoVentaId, String descripcion, double montoTotal, double montoRecibido, LocalDate fecha) {
        this.clienteId = clienteId;
        this.tipoVentaId = tipoVentaId;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.montoRecibido = montoRecibido;
        this.fecha = fecha;
    }

    public Venta(int id, int clienteId, int tipoVentaId, String descripcion, double montoTotal, double montoRecibido, LocalDate fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.tipoVentaId = tipoVentaId;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.montoRecibido = montoRecibido;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getTipoVentaId() {
        return tipoVentaId;
    }

    public void setTipoVentaId(int tipoVentaId) {
        this.tipoVentaId = tipoVentaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoRecibido() {
        return montoRecibido;
    }

    public void setMontoRecibido(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
