package com.dms.tareosoft.presentation.models;

public class EstadoEmpleadoRow {
    private String codigoEmpleado;
    private String empleado;
    private String fechaHoraIngreso;
    private String fechaHoraSalida;
    private int estado;

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

    public String getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(String fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EstadoEmpleadoRow{" +
                "codigoEmpleado='" + codigoEmpleado + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHoraIngreso='" + fechaHoraIngreso + '\'' +
                ", fechaHoraSalida='" + fechaHoraSalida + '\'' +
                ", estado=" + estado +
                '}';
    }
}
