package com.dms.tareosoft.presentation.models;

import java.io.Serializable;

public class TareoIniciadoDetalle implements Serializable {

    private String codigo;
    private String descripcion1;
    private String descripcion2;
    private String fechaHoraInicio;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion1() {
        return descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getDescripcion2() {
        return descripcion2;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    @Override
    public String toString() {
        return "TareoIniciadoDetalle{" +
                "codigo='" + codigo + '\'' +
                ", descripcion1='" + descripcion1 + '\'' +
                ", descripcion2='" + descripcion2 + '\'' +
                ", fechaHoraInicio='" + fechaHoraInicio + '\'' +
                '}';
    }
}
