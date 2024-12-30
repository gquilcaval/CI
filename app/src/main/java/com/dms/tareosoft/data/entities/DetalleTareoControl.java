package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity()
public class DetalleTareoControl {

    @PrimaryKey(autoGenerate = true)
    private int idControl;
    @NonNull
    @SerializedName("vch_IdentificadorTareo")
    private String fkTareo;
    @SerializedName("vch_FechaControl")
    private String fechaControl;
    @SerializedName("vch_HoraControl")
    private String horaControl;
    @SerializedName("vch_CodigoEmpleado")
    private String codEmpleado;
    @SerializedName("int_IdentificadorEmpleado")
    private int fkEmpleado;
    @SerializedName("int_IdentificadorUsuario_Insert")
    private int fkUsuarioInsert;
    @SerializedName("int_IdentificadorUsuario_Update")
    private int fkUsuarioUpdate;
    private String flgEnvio;

    public int getIdControl() {
        return idControl;
    }

    public void setIdControl(int idControl) {
        this.idControl = idControl;
    }

    @NonNull
    public String getFkTareo() {
        return fkTareo;
    }

    public void setFkTareo(@NonNull String fkTareo) {
        this.fkTareo = fkTareo;
    }

    public String getFechaControl() {
        return fechaControl;
    }

    public void setFechaControl(String fechaControl) {
        this.fechaControl = fechaControl;
    }

    public String getHoraControl() {
        return horaControl;
    }

    public void setHoraControl(String horaControl) {
        this.horaControl = horaControl;
    }

    public String getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(String codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public int getFkEmpleado() {
        return fkEmpleado;
    }

    public void setFkEmpleado(int fkEmpleado) {
        this.fkEmpleado = fkEmpleado;
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

    public String getFlgEnvio() {
        return flgEnvio;
    }

    public void setFlgEnvio(String flgEnvio) {
        this.flgEnvio = flgEnvio;
    }

    @Override
    public String toString() {
        return "DetalleTareoControl{" +
                "idControl=" + idControl +
                ", fkTareo='" + fkTareo + '\'' +
                ", fechaControl='" + fechaControl + '\'' +
                ", horaControl='" + horaControl + '\'' +
                ", codEmpleado='" + codEmpleado + '\'' +
                ", fkEmpleado=" + fkEmpleado +
                ", fkUsuarioInsert=" + fkUsuarioInsert +
                ", fkUsuarioUpdate=" + fkUsuarioUpdate +
                ", flgEnvio='" + flgEnvio + '\'' +
                '}';
    }
}
