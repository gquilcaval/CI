package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(primaryKeys = "codResultado", indices = {@Index(value = "codResultado", unique = true)})
public class ResultadoTareo {

    @NonNull
    @SerializedName("vch_IdentificadorResultado")
    private String codResultado;
    @SerializedName("vch_IdentificadorDetalle")
    private String fkDetalleTareo;
    @SerializedName("vch_FechaRegistro")
    private String fechaRegistro;
    @SerializedName("vch_HoraRegistro")
    private String horaRegistro;
    @SerializedName("dec_Cantidad")
    private double cantidad;
    @SerializedName("int_IdentificadorUsuario_Insert")
    private int fkUsuarioInsert;
    @SerializedName("int_IdentificadorUsuario_Update")
    private int fkUsuarioUpdate;
    @SerializedName("vch_FechaModificacion")
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

    public String getFkDetalleTareo() {
        return fkDetalleTareo;
    }

    public void setFkDetalleTareo(String fkDetalleTareo) {
        this.fkDetalleTareo = fkDetalleTareo;
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
        return "ResultadoTareo{" +
                "codResultado='" + codResultado + '\'' +
                ", fkDetalleTareo='" + fkDetalleTareo + '\'' +
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
