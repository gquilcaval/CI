package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "codTareo", indices = {@Index(value = "codTareo", unique = true)})
public class Tareo {

    @NonNull
    @SerializedName("codTareo")
    private String codTareo;
    @SerializedName("fkConcepto1")
    private int fkConcepto1;
    @SerializedName("fkConcepto2")
    private int fkConcepto2;
    @SerializedName("fkConcepto3")
    private int fkConcepto3;
    @SerializedName("fkConcepto4")
    private int fkConcepto4;
    @SerializedName("fkConcepto5")
    private int fkConcepto5;
    @SerializedName("fkUnidadMedida")
    private int fkUnidadMedida;
    @SerializedName("fechaInicio")
    private String fechaInicio;
    @SerializedName("horaInicio")
    private String horaInicio;
    @SerializedName("flgFechaInicio")
    private int flgFechaInicio;
    @SerializedName("flgHoraInicio")
    private String flgHoraInicio;
    @SerializedName("FechaFin")
    private String fechaFin;
    @SerializedName("HoraFin")
    private String horaFin;
    @SerializedName("flgFechaFin")
    private int flgFechaFin;
    @SerializedName("flgHoraFin")
    private String flgHoraFin;
    @SerializedName("cantTrabajadores")
    private int cantTrabajadores;
    @SerializedName("cantProducida")
    private double cantProducida;
    @SerializedName("flgResultados")
    private int flgResultados;
    @SerializedName("tipoTareo")
    private int tipoTareo;
    @SerializedName("estado")
    private int estado;
    @SerializedName("fkTurno")
    private int fkTurno;
    @SerializedName("vchCodigoParcela")
    private String codParcela;
    @SerializedName("vchCodigoCultivo")
    private String codCultivo;
    @SerializedName("vchCodigoGrupo")
    private String codGrupo;
    @SerializedName("vchCodigoMaterial")
    private String codMaterial;
    private String tipoPlanilla;
    private int codigoClase;
    @TypeResultado
    private int tipoResultado;
    private String fechaRegistroInicio;
    private String horaRegistroInicio;
    private String fechaRegistroFin;
    private String horaRegistroFin;
    private int usuarioInsert;
    private int usuarioUpdate;
    private String fechaModificacion;
    @StatusDescargaServidor
    private String flgEnvio;
    private int flgEstadoRegistro;

    @NonNull
    public String getCodTareo() {
        return codTareo;
    }

    public void setCodTareo(@NonNull String codTareo) {
        this.codTareo = codTareo;
    }

    public int getFkConcepto1() {
        return fkConcepto1;
    }

    public void setFkConcepto1(int fkConcepto1) {
        this.fkConcepto1 = fkConcepto1;
    }

    public int getFkConcepto2() {
        return fkConcepto2;
    }

    public void setFkConcepto2(int fkConcepto2) {
        this.fkConcepto2 = fkConcepto2;
    }

    public int getFkConcepto3() {
        return fkConcepto3;
    }

    public void setFkConcepto3(int fkConcepto3) {
        this.fkConcepto3 = fkConcepto3;
    }

    public int getFkConcepto4() {
        return fkConcepto4;
    }

    public void setFkConcepto4(int fkConcepto4) {
        this.fkConcepto4 = fkConcepto4;
    }

    public int getFkConcepto5() {
        return fkConcepto5;
    }

    public void setFkConcepto5(int fkConcepto5) {
        this.fkConcepto5 = fkConcepto5;
    }

    public int getFkUnidadMedida() {
        return fkUnidadMedida;
    }

    public void setFkUnidadMedida(int fkUnidadMedida) {
        this.fkUnidadMedida = fkUnidadMedida;
    }

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

    public int getFlgFechaInicio() {
        return flgFechaInicio;
    }

    public void setFlgFechaInicio(int flgFechaInicio) {
        this.flgFechaInicio = flgFechaInicio;
    }

    public String getFlgHoraInicio() {
        return flgHoraInicio;
    }

    public void setFlgHoraInicio(String flgHoraInicio) {
        this.flgHoraInicio = flgHoraInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getFlgFechaFin() {
        return flgFechaFin;
    }

    public void setFlgFechaFin(int flgFechaFin) {
        this.flgFechaFin = flgFechaFin;
    }

    public String getFlgHoraFin() {
        return flgHoraFin;
    }

    public void setFlgHoraFin(String flgHoraFin) {
        this.flgHoraFin = flgHoraFin;
    }

    public int getCantTrabajadores() {
        return cantTrabajadores;
    }

    public void setCantTrabajadores(int cantTrabajadores) {
        this.cantTrabajadores = cantTrabajadores;
    }

    public double getCantProducida() {
        return cantProducida;
    }

    public void setCantProducida(double cantProducida) {
        this.cantProducida = cantProducida;
    }

    public int getFlgResultados() {
        return flgResultados;
    }

    public void setFlgResultados(int flgResultados) {
        this.flgResultados = flgResultados;
    }

    public int getTipoTareo() {
        return tipoTareo;
    }

    public void setTipoTareo(int tipoTareo) {
        this.tipoTareo = tipoTareo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getFkTurno() {
        return fkTurno;
    }

    public void setFkTurno(int fkTurno) {
        this.fkTurno = fkTurno;
    }

    public String getCodParcela() {
        return codParcela;
    }

    public void setCodParcela(String codParcela) {
        this.codParcela = codParcela;
    }

    public String getCodCultivo() {
        return codCultivo;
    }

    public void setCodCultivo(String codCultivo) {
        this.codCultivo = codCultivo;
    }

    public String getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getCodMaterial() {
        return codMaterial;
    }

    public void setCodMaterial(String codMaterial) {
        this.codMaterial = codMaterial;
    }

    public String getTipoPlanilla() {
        return tipoPlanilla;
    }

    public void setTipoPlanilla(String tipoPlanilla) {
        this.tipoPlanilla = tipoPlanilla;
    }

    public int getCodigoClase() {
        return codigoClase;
    }

    public void setCodigoClase(int codigoClase) {
        this.codigoClase = codigoClase;
    }

    public int getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(int tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String getFechaRegistroInicio() {
        return fechaRegistroInicio;
    }

    public void setFechaRegistroInicio(String fechaRegistroInicio) {
        this.fechaRegistroInicio = fechaRegistroInicio;
    }

    public String getHoraRegistroInicio() {
        return horaRegistroInicio;
    }

    public void setHoraRegistroInicio(String horaRegistroInicio) {
        this.horaRegistroInicio = horaRegistroInicio;
    }

    public String getFechaRegistroFin() {
        return fechaRegistroFin;
    }

    public void setFechaRegistroFin(String fechaRegistroFin) {
        this.fechaRegistroFin = fechaRegistroFin;
    }

    public String getHoraRegistroFin() {
        return horaRegistroFin;
    }

    public void setHoraRegistroFin(String horaRegistroFin) {
        this.horaRegistroFin = horaRegistroFin;
    }

    public int getUsuarioInsert() {
        return usuarioInsert;
    }

    public void setUsuarioInsert(int usuarioInsert) {
        this.usuarioInsert = usuarioInsert;
    }

    public int getUsuarioUpdate() {
        return usuarioUpdate;
    }

    public void setUsuarioUpdate(int usuarioUpdate) {
        this.usuarioUpdate = usuarioUpdate;
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

    public int getFlgEstadoRegistro() {
        return flgEstadoRegistro;
    }

    public void setFlgEstadoRegistro(int flgEstadoRegistro) {
        this.flgEstadoRegistro = flgEstadoRegistro;
    }

    @Override
    public String toString() {
        return "Tareo{" +
                "codTareo='" + codTareo + '\'' +
                ", fkConcepto1=" + fkConcepto1 +
                ", fkConcepto2=" + fkConcepto2 +
                ", fkConcepto3=" + fkConcepto3 +
                ", fkConcepto4=" + fkConcepto4 +
                ", fkConcepto5=" + fkConcepto5 +
                ", fkUnidadMedida=" + fkUnidadMedida +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", flgFechaInicio=" + flgFechaInicio +
                ", flgHoraInicio=" + flgHoraInicio +
                ", fechaFin='" + fechaFin + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", flgFechaFin=" + flgFechaFin +
                ", flgHoraFin=" + flgHoraFin +
                ", cantTrabajadores=" + cantTrabajadores +
                ", cantProducida=" + cantProducida +
                ", flgResultados=" + flgResultados +
                ", tipoTareo=" + tipoTareo +
                ", estado=" + estado +
                ", fkTurno=" + fkTurno +
                ", codParcela='" + codParcela + '\'' +
                ", codCultivo='" + codCultivo + '\'' +
                ", codGrupo='" + codGrupo + '\'' +
                ", codMaterial='" + codMaterial + '\'' +
                ", tipoPlanilla='" + tipoPlanilla + '\'' +
                ", codigoClase=" + codigoClase +
                ", tipoResultado=" + tipoResultado +
                ", fechaRegistroInicio='" + fechaRegistroInicio + '\'' +
                ", horaRegistroInicio='" + horaRegistroInicio + '\'' +
                ", fechaRegistroFin='" + fechaRegistroFin + '\'' +
                ", horaRegistroFin='" + horaRegistroFin + '\'' +
                ", usuarioInsert=" + usuarioInsert +
                ", usuarioUpdate=" + usuarioUpdate +
                ", fechaModificacion='" + fechaModificacion + '\'' +
                ", flgEnvio='" + flgEnvio + '\'' +
                ", flgEstadoRegistro=" + flgEstadoRegistro +
                '}';
    }
}
