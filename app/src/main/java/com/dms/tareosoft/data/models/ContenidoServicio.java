package com.dms.tareosoft.data.models;

public class ContenidoServicio {

    private String urlServicioWeb;
    private String fechaHora;
    private int modoTrabajo;
    private boolean erroresDetallados;
    private int margenDiferencia;

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

    public int getModoTrabajo() {
        return modoTrabajo;
    }

    public void setModoTrabajo(int modoTrabajo) {
        this.modoTrabajo = modoTrabajo;
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
}
