package com.alvear.multigraficaalvear.models;

public class Servicio {

    private int id;
    private String nombre;
    private String categoria;
    private double precioSugerido;

    public Servicio() {
    }

    public Servicio(String nombre, String categoria, double precioSugerido) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioSugerido = precioSugerido;
    }

    public Servicio(int id, String nombre, String categoria, double precioSugerido) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioSugerido = precioSugerido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecioSugerido() {
        return precioSugerido;
    }

    public void setPrecioSugerido(double precioSugerido) {
        this.precioSugerido = precioSugerido;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
