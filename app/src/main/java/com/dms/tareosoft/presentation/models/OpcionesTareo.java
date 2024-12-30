package com.dms.tareosoft.presentation.models;

public class OpcionesTareo {
    private String fechaInicio;
    private String horaInicio;
    private int codigoTurno;
    private int tipoTareo;
    private int tipoResultado;
    private int codigoUnidadMedida;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
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

    public int getCodigoUnidadMedida() {
        return codigoUnidadMedida;
    }

    public void setCodigoUnidadMedida(int codigoUnidadMedida) {
        this.codigoUnidadMedida = codigoUnidadMedida;
    }

    @Override
    public String toString() {
        return "OpcionesTareo{" +
                "fechaInicio='" + fechaInicio + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", codigoTurno=" + codigoTurno +
                ", tipoTareo=" + tipoTareo +
                ", tipoResultado=" + tipoResultado +
                ", codigoUnidadMedida=" + codigoUnidadMedida +
                '}';
    }
}
