package com.dms.tareosoft.presentation.models;

public class EmpleadoControlRow {
    private String codigoEmpleado;
    private int idEmpleado;
    private int controles;

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getControles() {
        return controles;
    }

    public void setControles(int controles) {
        this.controles = controles;
    }

    @Override
    public String toString() {
        return "EmpleadoControlRow{" +
                "idEmpleado=" + idEmpleado +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", controles=" + controles +
                '}';
    }
}
