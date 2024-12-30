package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Acceso")
public class Acceso {


    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("Codigo_de_solicitud")
    private String cod_solicitud;
    @SerializedName("Agencia_u_oficina")
    private String agencia_oficina;
    @SerializedName("Fecha_inicial")
    private String fech_inicial;
    @SerializedName("Hora_inicio")
    private String hora_inicio;
    @SerializedName("Fecha_final")
    private String fecha_final;
    @SerializedName("Hora_final")
    private String hora_final;
    @SerializedName("Detalle_del_motivo")
    private String detalle_motivo;
    @SerializedName("Zonas")
    private String zonas;
    @SerializedName("Area_solicitante")
    private String area_solicitante;
    @SerializedName("Motivo")
    private String motivo;
    @SerializedName("Empresa")
    private String empresa;
    @SerializedName("Visitantes_o_personal")
    private String visitante_personal;
    @SerializedName("Equipo_portatil")
    private String equipo_portatil;
    @SerializedName("Serie_y_Marca")
    private String serie_marca;
    private String fech_registro;
    private String flg_envio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCod_solicitud() {
        return cod_solicitud;
    }

    public void setCod_solicitud(String cod_solicitud) {
        this.cod_solicitud = cod_solicitud;
    }

    public String getAgencia_oficina() {
        return agencia_oficina;
    }

    public void setAgencia_oficina(String agencia_oficina) {
        this.agencia_oficina = agencia_oficina;
    }

    public String getFech_inicial() {
        return fech_inicial;
    }

    public void setFech_inicial(String fech_inicial) {
        this.fech_inicial = fech_inicial;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getDetalle_motivo() {
        return detalle_motivo;
    }

    public void setDetalle_motivo(String detalle_motivo) {
        this.detalle_motivo = detalle_motivo;
    }

    public String getZonas() {
        return zonas;
    }

    public void setZonas(String zonas) {
        this.zonas = zonas;
    }

    public String getArea_solicitante() {
        return area_solicitante;
    }

    public void setArea_solicitante(String area_solicitante) {
        this.area_solicitante = area_solicitante;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getVisitante_personal() {
        return visitante_personal;
    }

    public void setVisitante_personal(String visitante_personal) {
        this.visitante_personal = visitante_personal;
    }

    public String getEquipo_portatil() {
        return equipo_portatil;
    }

    public void setEquipo_portatil(String equipo_portatil) {
        this.equipo_portatil = equipo_portatil;
    }

    public String getSerie_marca() {
        return serie_marca;
    }

    public void setSerie_marca(String serie_marca) {
        this.serie_marca = serie_marca;
    }

    public String getFlg_envio() {
        return flg_envio;
    }

    public void setFlg_envio(String flg_envio) {
        this.flg_envio = flg_envio;
    }

    public String getFech_registro() {
        return fech_registro;
    }

    public void setFech_registro(String fech_registro) {
        this.fech_registro = fech_registro;
    }

    @Override
    public String toString() {
        return "Acceso{" +
                "id=" + id +
                ", cod_solicitud='" + cod_solicitud + '\'' +
                ", agencia_oficina='" + agencia_oficina + '\'' +
                ", fech_inicial='" + fech_inicial + '\'' +
                ", hora_inicio='" + hora_inicio + '\'' +
                ", fecha_final='" + fecha_final + '\'' +
                ", hora_final='" + hora_final + '\'' +
                ", detalle_motivo='" + detalle_motivo + '\'' +
                ", zonas='" + zonas + '\'' +
                ", area_solicitante='" + area_solicitante + '\'' +
                ", motivo='" + motivo + '\'' +
                ", empresa='" + empresa + '\'' +
                ", visitante_personal='" + visitante_personal + '\'' +
                ", equipo_portatil='" + equipo_portatil + '\'' +
                ", serie_marca='" + serie_marca + '\'' +
                ", fech_registro='" + fech_registro + '\'' +
                ", flg_envio=" + flg_envio +
                '}';
    }
}
