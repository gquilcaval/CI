package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.annotacion.TypeTareo;

import java.io.Serializable;

public class AllEmpleadoRow implements Serializable {
    private String codigoDetalleTareo;
    private String codigoTareo;
    private int codigoClase;
    private String codigoEmpleado;
    private int idEmpleado;
    private String tareo;
    private String empleado;
    private String fechaHora;
    @TypeTareo
    private int tipoTareo;
    @TypeResultado
    private int tipoResultado;
    private int cantProducida;
    private int cantTrabajadores;
    private boolean checked;

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

    public int getCodigoClase() {
        return codigoClase;
    }

    public void setCodigoClase(int codigoClase) {
        this.codigoClase = codigoClase;
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

    public int getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(int tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public int getCantProducida() {
        return cantProducida;
    }

    public void setCantProducida(int cantProducida) {
        this.cantProducida = cantProducida;
    }

    public int getCantTrabajadores() {
        return cantTrabajadores;
    }

    public void setCantTrabajadores(int cantTrabajadores) {
        this.cantTrabajadores = cantTrabajadores;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "AllEmpleadoRow{" +
                "codigoDetalleTareo='" + codigoDetalleTareo + '\'' +
                ", codigoTareo='" + codigoTareo + '\'' +
                ", codigoClase=" + codigoClase +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", tareo='" + tareo + '\'' +
                ", empleado='" + empleado + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", tipoTareo=" + tipoTareo +
                ", tipoResultado=" + tipoResultado +
                ", cantProducida=" + cantProducida +
                ", cantTrabajadores=" + cantTrabajadores +
                ", checked=" + checked +
                '}';
    }
}