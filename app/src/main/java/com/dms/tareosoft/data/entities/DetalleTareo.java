package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Tareo.class, parentColumns = "codTareo", childColumns = "fkTareo", onDelete = CASCADE))
public class DetalleTareo {
    @PrimaryKey
    @NonNull
    @SerializedName("codDetalleTareo")
    private String codDetalleTareo;
    @ColumnInfo(name = "fkTareo", index = true)
    @SerializedName("fkTareo")
    private String fkTareo;
    @SerializedName("fkEmpleado")
    private int fkEmpleado;
    @SerializedName("codigoEmpleado")
    private String codigoEmpleado;
    @SerializedName("fechaRegistro")
    private String fechaRegistro;
    @SerializedName("horaRegistroInicio")
    private String horaRegistroInicio;
    @SerializedName("vch_HoraRegistroSalida")
    private String vch_HoraRegistroSalida;
    @SerializedName("horaIngreso")
    private String horaIngreso;
    @SerializedName("fechaIngreso")
    private String fechaIngreso;
    @SerializedName("vch_FechaSalida")
    private String vch_FechaSalida;
    @SerializedName("vch_HoraSalida")
    private String vch_HoraSalida;
    @SerializedName("flgEstadoIniTareo")
    private int flgEstadoIniTareo;
    @SerializedName("flgEstadoFinTareo")
    private int flgEstadoFinTareo ;
    @SerializedName("int_FlgHoraRefrigerio")
    private int int_FlgHoraRefrigerio;
    @SerializedName("horaIniRefrigerio")
    private String horaIniRefrigerio;
    @SerializedName("horaFinRefrigerio")
    private String horaFinRefrigerio;
    private int insUsuarioDetalle;

    @NonNull
    public String getCodDetalleTareo() {
        return codDetalleTareo;
    }

    public void setCodDetalleTareo(@NonNull String codDetalleTareo) {
        this.codDetalleTareo = codDetalleTareo;
    }

    public String getFkTareo() {
        return fkTareo;
    }

    public void setFkTareo(String fkTareo) {
        this.fkTareo = fkTareo;
    }

    public int getFkEmpleado() {
        return fkEmpleado;
    }

    public void setFkEmpleado(int fkEmpleado) {
        this.fkEmpleado = fkEmpleado;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getHoraRegistroInicio() {
        return horaRegistroInicio;
    }

    public void setHoraRegistroInicio(String horaRegistroInicio) {
        this.horaRegistroInicio = horaRegistroInicio;
    }

    public String getVch_HoraRegistroSalida() {
        return vch_HoraRegistroSalida;
    }

    public void setVch_HoraRegistroSalida(String vch_HoraRegistroSalida) {
        this.vch_HoraRegistroSalida = vch_HoraRegistroSalida;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getVch_FechaSalida() {
        return vch_FechaSalida;
    }

    public void setVch_FechaSalida(String vch_FechaSalida) {
        this.vch_FechaSalida = vch_FechaSalida;
    }

    public String getVch_HoraSalida() {
        return vch_HoraSalida;
    }

    public void setVch_HoraSalida(String vch_HoraSalida) {
        this.vch_HoraSalida = vch_HoraSalida;
    }

    public int getFlgEstadoIniTareo() {
        return flgEstadoIniTareo;
    }

    public void setFlgEstadoIniTareo(int flgEstadoIniTareo) {
        this.flgEstadoIniTareo = flgEstadoIniTareo;
    }

    public int getFlgEstadoFinTareo() {
        return flgEstadoFinTareo;
    }

    public void setFlgEstadoFinTareo(int flgEstadoFinTareo) {
        this.flgEstadoFinTareo = flgEstadoFinTareo;
    }

    public int getInt_FlgHoraRefrigerio() {
        return int_FlgHoraRefrigerio;
    }

    public void setInt_FlgHoraRefrigerio(int int_FlgHoraRefrigerio) {
        this.int_FlgHoraRefrigerio = int_FlgHoraRefrigerio;
    }

    public String getHoraIniRefrigerio() {
        return horaIniRefrigerio;
    }

    public void setHoraIniRefrigerio(String horaIniRefrigerio) {
        this.horaIniRefrigerio = horaIniRefrigerio;
    }

    public String getHoraFinRefrigerio() {
        return horaFinRefrigerio;
    }

    public void setHoraFinRefrigerio(String horaFinRefrigerio) {
        this.horaFinRefrigerio = horaFinRefrigerio;
    }

    public int getInsUsuarioDetalle() {
        return insUsuarioDetalle;
    }

    public void setInsUsuarioDetalle(int insUsuarioDetalle) {
        this.insUsuarioDetalle = insUsuarioDetalle;
    }

    @Override
    public String toString() {
        return "DetalleTareo{" +
                "codDetalleTareo='" + codDetalleTareo + '\'' +
                ", fkTareo='" + fkTareo + '\'' +
                ", fkEmpleado=" + fkEmpleado +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", horaRegistroInicio='" + horaRegistroInicio + '\'' +
                ", vch_HoraRegistroSalida='" + vch_HoraRegistroSalida + '\'' +
                ", horaIngreso='" + horaIngreso + '\'' +
                ", fechaIngreso='" + fechaIngreso + '\'' +
                ", vch_FechaSalida='" + vch_FechaSalida + '\'' +
                ", vch_HoraSalida='" + vch_HoraSalida + '\'' +
                ", flgEstadoIniTareo=" + flgEstadoIniTareo +
                ", flgEstadoFinTareo=" + flgEstadoFinTareo +
                ", int_FlgHoraRefrigerio=" + int_FlgHoraRefrigerio +
                ", horaIniRefrigerio='" + horaIniRefrigerio + '\'' +
                ", horaFinRefrigerio='" + horaFinRefrigerio + '\'' +
                ", insUsuarioDetalle=" + insUsuarioDetalle +
                '}';
    }
}
