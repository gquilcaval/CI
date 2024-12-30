package com.dms.tareosoft.presentation.models;

public class ResultadoRow {
    private String codigoEmpleado;
    private String empleado;
    private String fechaHora;
    private int cantidadProducida;

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(int cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    @Override
    public String toString() {
        return "ResultadoRow{" +
                "codigoEmpleado='" + codigoEmpleado + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", cantidadProducida=" + cantidadProducida +
                '}';
    }
}
