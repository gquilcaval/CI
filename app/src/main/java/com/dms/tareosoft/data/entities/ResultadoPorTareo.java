package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;

@Entity(primaryKeys = "codResultado", indices = {@Index(value = "codResultado", unique = true)})
public class ResultadoPorTareo {

    @NonNull
    private String codResultado;
    private String fkTareo;
    private String fechaRegistro;
    private String horaRegistro;
    private double cantidad;
    private int fkUsuarioInsert;
    private int fkUsuarioUpdate;
    private String fechaModificacion;
    @StatusDescargaServidor
    private String flgEnvio;

    @NonNull
    public String getCodResultado() {
        return codResultado;
    }

    public void setCodResultado(@NonNull String codResultado) {
        this.codResultado = codResultado;
    }

    public String getFkTareo() {
        return fkTareo;
    }

    public void setFkTareo(String fkTareo) {
        this.fkTareo = fkTareo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getFkUsuarioInsert() {
        return fkUsuarioInsert;
    }

    public void setFkUsuarioInsert(int fkUsuarioInsert) {
        this.fkUsuarioInsert = fkUsuarioInsert;
    }

    public int getFkUsuarioUpdate() {
        return fkUsuarioUpdate;
    }

    public void setFkUsuarioUpdate(int fkUsuarioUpdate) {
        this.fkUsuarioUpdate = fkUsuarioUpdate;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getFlgEnvio() {
        return flgEnvio;
    }

    public void setFlgEnvio(String flgEnvio) {
        this.flgEnvio = flgEnvio;
    }

    @Override
    public String toString() {
        return "ResultadoPorTareo{" +
                "codResultado='" + codResultado + '\'' +
                ", fkTareo='" + fkTareo + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", horaRegistro='" + horaRegistro + '\'' +
                ", cantidad=" + cantidad +
                ", fkUsuarioInsert=" + fkUsuarioInsert +
                ", fkUsuarioUpdate=" + fkUsuarioUpdate +
                ", fechaModificacion='" + fechaModificacion + '\'' +
                ", flgEnvio='" + flgEnvio + '\'' +
                '}';
    }
}
