package com.dms.tareosoft.presentation.models;

public class AllResultadoPorTareoRow {
    private String fecha;
    private double cantidad;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "AllResultadoPorTareoRow{" +
                "fecha='" + fecha + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
