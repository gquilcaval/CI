package com.dms.tareosoft.presentation.models;

import com.google.gson.annotations.SerializedName;

public class EmpleadoRow {
    @SerializedName("vch_CodigoEmpleado")
    private String codigoEmpleado;
    @SerializedName("NombreCompleto")
    private String empleado;
    @SerializedName("FechaHoraIngreso")
    private String fechaHoraIngreso;
    private String supervisor;
    private String tipoIngreso;

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

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(String tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    @Override
    public String toString() {
        return "EmpleadoRow{" +
                "codigoEmpleado='" + codigoEmpleado + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHoraIngreso='" + fechaHoraIngreso + '\'' +
                ", supervisor='" + supervisor + '\'' +
                ", tipoIngreso='" + tipoIngreso + '\'' +
                '}';
    }
}
