package com.dms.tareosoft.data.models;

import com.dms.tareosoft.annotacion.ModoTrabajo;

public class ContenidoGeneral {

    private String urlServicioWeb;
    private String fechaHora;
    private boolean erroresDetallados;
    private int margenDiferencia;
    private int timeOut;
    private boolean sound;
    private boolean validaDescarga;
    private boolean activeModuloAsistencia;
    private boolean activeModuloIncidencia;
    private boolean activeModuloAccceso;
    private String ultimaFechaHora;
    @ModoTrabajo
    private int modoTrabajo;
    private int DuracionRefrigerio;

    public String getUrlServicioWeb() {
        return urlServicioWeb;
    }

    public void setUrlServicioWeb(String urlServicioWeb) {
        this.urlServicioWeb = urlServicioWeb;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isErroresDetallados() {
        return erroresDetallados;
    }

    public void setErroresDetallados(boolean erroresDetallados) {
        this.erroresDetallados = erroresDetallados;
    }

    public int getMargenDiferencia() {
        return margenDiferencia;
    }

    public void setMargenDiferencia(int margenDiferencia) {
        this.margenDiferencia = margenDiferencia;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isValidaDescarga() {
        return validaDescarga;
    }

    public void setValidaDescarga(boolean validaDescarga) {
        this.validaDescarga = validaDescarga;
    }

    public String getUltimaFechaHora() {
        return ultimaFechaHora;
    }

    public void setUltimaFechaHora(String ultimaFechaHora) {
        this.ultimaFechaHora = ultimaFechaHora;
    }

    public int getModoTrabajo() {
        return modoTrabajo;
    }

    public void setModoTrabajo(@ModoTrabajo int modoTrabajo) {
        this.modoTrabajo = modoTrabajo;
    }

    public int getDuracionRefrigerio() {
        return DuracionRefrigerio;
    }

    public void setDuracionRefrigerio(int duracionRefrigerio) {
        DuracionRefrigerio = duracionRefrigerio;
    }

    public boolean isActiveModuloAsistencia() {
        return activeModuloAsistencia;
    }

    public void setActiveModuloAsistencia(boolean activeModuloAsistencia) {
        this.activeModuloAsistencia = activeModuloAsistencia;
    }

    public boolean isActiveModuloIncidencia() {
        return activeModuloIncidencia;
    }

    public void setActiveModuloIncidencia(boolean activeModuloIncidencia) {
        this.activeModuloIncidencia = activeModuloIncidencia;
    }

    public boolean isActiveModuloAccceso() {
        return activeModuloAccceso;
    }

    public void setActiveModuloAccceso(boolean activeModuloAccceso) {
        this.activeModuloAccceso = activeModuloAccceso;
    }


    @Override
    public String toString() {
        return "ContenidoGeneral{" +
                "urlServicioWeb='" + urlServicioWeb + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", erroresDetallados=" + erroresDetallados +
                ", margenDiferencia=" + margenDiferencia +
                ", timeOut=" + timeOut +
                ", sound=" + sound +
                ", validaDescarga=" + validaDescarga +
                ", activeModuloAsistencia=" + activeModuloAsistencia +
                ", activeModuloIncidencia=" + activeModuloIncidencia +
                ", activeModuloAccceso=" + activeModuloAccceso +
                ", ultimaFechaHora='" + ultimaFechaHora + '\'' +
                ", modoTrabajo=" + modoTrabajo +
                ", DuracionRefrigerio=" + DuracionRefrigerio +
                '}';
    }
}
