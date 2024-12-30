package com.dms.tareosoft.presentation.models;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = "codTareo", indices = {@Index(value = "codTareo", unique = true)})
public class EmpleadoConsultaRow {
    private String codTareo;
    private String codigoEmpleado;
    private String empleado;
    private String fechaHoraIngreso;
    private String fechaHoraSalida;
    private int cantProducida;
    private int estado;

    public String getCodTareo() {
        return codTareo;
    }

    public void setCodTareo(String codTareo) {
        this.codTareo = codTareo;
    }

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

    public int getCantProducida() {
        return cantProducida;
    }

    public void setCantProducida(int cantProducida) {
        this.cantProducida = cantProducida;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EmpleadoConsultaRow{" +
                "codTareo='" + codTareo + '\'' +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHoraIngreso='" + fechaHoraIngreso + '\'' +
                ", fechaHoraSalida='" + fechaHoraSalida + '\'' +
                ", cantProducida=" + cantProducida +
                ", estado=" + estado +
                '}';
    }
}
