package com.dms.tareosoft.data.models;

public class ContenidoAjustes {
    private boolean unidadMedida;
    private boolean fechaHoraInicio;
    private boolean fechaHoraFin;
    private boolean vigenciaDescarga;
    private boolean registrarEmpleado;
    private boolean registrarTareoNotEmpleado;
    private int timeWorker;

    public boolean isUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(boolean unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public boolean isFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(boolean fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public boolean isFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(boolean fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public boolean isVigenciaDescarga() {
        return vigenciaDescarga;
    }

    public void setVigenciaDescarga(boolean vigenciaDescarga) {
        this.vigenciaDescarga = vigenciaDescarga;
    }

    public boolean isRegistrarEmpleado() {
        return registrarEmpleado;
    }

    public void setRegistrarEmpleado(boolean registrarEmpleado) {
        this.registrarEmpleado = registrarEmpleado;
    }

    public boolean isRegistrarTareoNotEmpleado() {
        return registrarTareoNotEmpleado;
    }

    public void setRegistrarTareoNotEmpleado(boolean registrarTareoNotEmpleado) {
        this.registrarTareoNotEmpleado = registrarTareoNotEmpleado;
    }

    public int getTimeWorker() {
        return timeWorker;
    }

    public void setTimeWorker(int timeWorker) {
        this.timeWorker = timeWorker;
    }

    @Override
    public String toString() {
        return "ContenidoAjustes{" +
                "unidadMedida=" + unidadMedida +
                ", fechaHoraInicio=" + fechaHoraInicio +
                ", fechaHoraFin=" + fechaHoraFin +
                ", vigenciaDescarga=" + vigenciaDescarga +
                ", registrarEmpleado=" + registrarEmpleado +
                ", registrarTareoNotEmpleado=" + registrarTareoNotEmpleado +
                ", timeWorker=" + timeWorker +
                '}';
    }
}
