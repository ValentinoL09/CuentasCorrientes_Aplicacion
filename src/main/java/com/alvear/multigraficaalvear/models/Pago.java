package com.alvear.multigraficaalvear.models;

import java.time.LocalDate;

public class Pago {

    private int id;
    private int ventaId;
    private double montoPagado;
    private LocalDate fecha;

    public Pago() {
    }

    public Pago(int ventaId, double montoPagado, LocalDate fecha) {
        this.ventaId = ventaId;
        this.montoPagado = montoPagado;
        this.fecha = fecha;
    }

    public Pago(int id, int ventaId, double montoPagado, LocalDate fecha) {
        this.id = id;
        this.ventaId = ventaId;
        this.montoPagado = montoPagado;
        this.fecha = fecha;
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

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
