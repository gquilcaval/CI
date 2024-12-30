package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;

import java.io.Serializable;

public class AllTareoRow implements Serializable {

    private String codigoTareo;
    private String tareo;
    @StatusTareo
    private int status;
    @StatusDescargaServidor
    private String statusServidor;
    private String horaFecha;

    public String getCodigoTareo() {
        return codigoTareo;
    }

    public void setCodigoTareo(String codigoTareo) {
        this.codigoTareo = codigoTareo;
    }

    public String getTareo() {
        return tareo;
    }

    public void setTareo(String tareo) {
        this.tareo = tareo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusServidor() {
        return statusServidor;
    }

    public void setStatusServidor(String statusServidor) {
        this.statusServidor = statusServidor;
    }

    public String getHoraFecha() {
        return horaFecha;
    }

    public void setHoraFecha(String horaFecha) {
        this.horaFecha = horaFecha;
    }

    @Override
    public String toString() {
        return "AllTareoRow{" +
                "codigoTareo='" + codigoTareo + '\'' +
                ", tareo='" + tareo + '\'' +
                ", status=" + status +
                ", statusServidor='" + statusServidor + '\'' +
                ", horaFecha='" + horaFecha + '\'' +
                '}';
    }
}
