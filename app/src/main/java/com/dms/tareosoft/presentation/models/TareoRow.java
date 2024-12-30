package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TareoRow implements Serializable {

    private String codigo;
    private int codigoClase;
    private String concepto1;
    private String concepto2;
    private String concepto3;
    private String concepto4;
    private String concepto5;
    private int cantTrabajadores;
    @TypeTareo
    private int tipoTareo;
    private String fechaInicio;
    private String fechaFin;
    private boolean isChecked;
    private int cantProducida;
    @TypeResultado
    private int tipoResultado;
    @StatusTareo
    private int statusTareo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCodigoClase() {
        return codigoClase;
    }

    public void setCodigoClase(int codigoClase) {
        this.codigoClase = codigoClase;
    }

    public String getConcepto1() {
        return concepto1;
    }

    public void setConcepto1(String concepto1) {
        this.concepto1 = concepto1;
    }

    public String getConcepto2() {
        return concepto2;
    }

    public void setConcepto2(String concepto2) {
        this.concepto2 = concepto2;
    }

    public String getConcepto3() {
        return concepto3;
    }

    public void setConcepto3(String concepto3) {
        this.concepto3 = concepto3;
    }

    public String getConcepto4() {
        return concepto4;
    }

    public void setConcepto4(String concepto4) {
        this.concepto4 = concepto4;
    }

    public String getConcepto5() {
        return concepto5;
    }

    public void setConcepto5(String concepto5) {
        this.concepto5 = concepto5;
    }

    public int getCantTrabajadores() {
        return cantTrabajadores;
    }

    public void setCantTrabajadores(int cantTrabajadores) {
        this.cantTrabajadores = cantTrabajadores;
    }

    public int getTipoTareo() {
        return tipoTareo;
    }

    public void setTipoTareo(int tipoTareo) {
        this.tipoTareo = tipoTareo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getCantProducida() {
        return cantProducida;
    }

    public void setCantProducida(int cantProducida) {
        this.cantProducida = cantProducida;
    }

    public int getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(int tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public int getStatusTareo() {
        return statusTareo;
    }

    public void setStatusTareo(int statusTareo) {
        this.statusTareo = statusTareo;
    }

    @Override
    public String toString() {
        return "TareoRow{" +
                "codigo='" + codigo + '\'' +
                ", codigoClase=" + codigoClase +
                ", concepto1='" + concepto1 + '\'' +
                ", concepto2='" + concepto2 + '\'' +
                ", concepto3='" + concepto3 + '\'' +
                ", concepto4='" + concepto4 + '\'' +
                ", concepto5='" + concepto5 + '\'' +
                ", cantTrabajadores=" + cantTrabajadores +
                ", tipoTareo=" + tipoTareo +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                ", isChecked=" + isChecked +
                ", cantProducida=" + cantProducida +
                ", tipoResultado=" + tipoResultado +
                ", statusTareo=" + statusTareo +
                '}';
    }
}
