package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.TypeTareo;

import java.io.Serializable;

public class AllEmpleadoConsultaRow implements Serializable {
    private String codigoDetalleTareo;
    private String codigoTareo;
    private String codigoEmpleado;
    private int idEmpleado;
    private String tareo;
    private String empleado;
    private String fechaHora;
    @TypeTareo
    private int tipoTareo;
    private int cantProducida;

    public String getCodigoDetalleTareo() {
        return codigoDetalleTareo;
    }

    public void setCodigoDetalleTareo(String codigoDetalleTareo) {
        this.codigoDetalleTareo = codigoDetalleTareo;
    }

    public String getCodigoTareo() {
        return codigoTareo;
    }

    public void setCodigoTareo(String codigoTareo) {
        this.codigoTareo = codigoTareo;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getTareo() {
        return tareo;
    }

    public void setTareo(String tareo) {
        this.tareo = tareo;
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

    public int getTipoTareo() {
        return tipoTareo;
    }

    public void setTipoTareo(int tipoTareo) {
        this.tipoTareo = tipoTareo;
    }

    public int getCantProducida() {
        return cantProducida;
    }

    public void setCantProducida(int cantProducida) {
        this.cantProducida = cantProducida;
    }

    @Override
    public String toString() {
        return "AllEmpleadoConsultaRow{" +
                "codigoDetalleTareo='" + codigoDetalleTareo + '\'' +
                ", codigoTareo='" + codigoTareo + '\'' +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", tareo='" + tareo + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", tipoTareo=" + tipoTareo +
                ", cantProducida=" + cantProducida +
                '}';
    }
}