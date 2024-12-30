package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Incidencia")
public class Incidencia {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("int_MarcaInsidencia")
    private int int_MarcaInsidencia;
    @SerializedName("vch_ClaseTareo")
    private String vch_ClaseTareo;
    @SerializedName("vch_Actividad")
    private String vch_Actividad;
    @SerializedName("Vch_Tarea")
    private String Vch_Tarea = "";
    @SerializedName("DTM_FECMARCA")
    private String DTM_FECMARCA;
    @SerializedName("VCH_HORAMARCA")
    private String VCH_HORAMARCA;
    @SerializedName("VCH_MENSAJE")
    private String VCH_MENSAJE = "";
    @SerializedName("VCH_USUREG")
    private String VCH_USUREG;
    @SerializedName("DTM_FECREG")
    private String DTM_FECREG;
    private String flgEnvio;


    public int getInt_MarcaInsidencia() {
        return int_MarcaInsidencia;
    }

    public void setInt_MarcaInsidencia(int int_MarcaInsidencia) {
        this.int_MarcaInsidencia = int_MarcaInsidencia;
    }

    public String getVch_ClaseTareo() {
        return vch_ClaseTareo;
    }

    public void setVch_ClaseTareo(String vch_ClaseTareo) {
        this.vch_ClaseTareo = vch_ClaseTareo;
    }

    public String getVch_Actividad() {
        return vch_Actividad;
    }

    public void setVch_Actividad(String vch_Actividad) {
        this.vch_Actividad = vch_Actividad;
    }

    public String getVch_Tarea() {
        return Vch_Tarea;
    }

    public void setVch_Tarea(String vch_Tarea) {
        Vch_Tarea = vch_Tarea;
    }

    public String getDTM_FECMARCA() {
        return DTM_FECMARCA;
    }

    public void setDTM_FECMARCA(String DTM_FECMARCA) {
        this.DTM_FECMARCA = DTM_FECMARCA;
    }

    public String getVCH_HORAMARCA() {
        return VCH_HORAMARCA;
    }

    public void setVCH_HORAMARCA(String VCH_HORAMARCA) {
        this.VCH_HORAMARCA = VCH_HORAMARCA;
    }

    public String getVCH_MENSAJE() {
        return VCH_MENSAJE;
    }

    public void setVCH_MENSAJE(String VCH_MENSAJE) {
        this.VCH_MENSAJE = VCH_MENSAJE;
    }

    public String getVCH_USUREG() {
        return VCH_USUREG;
    }

    public void setVCH_USUREG(String VCH_USUREG) {
        this.VCH_USUREG = VCH_USUREG;
    }

    public String getDTM_FECREG() {
        return DTM_FECREG;
    }

    public void setDTM_FECREG(String DTM_FECREG) {
        this.DTM_FECREG = DTM_FECREG;
    }

    public String getFlgEnvio() {
        return flgEnvio;
    }

    public void setFlgEnvio(String flgEnvio) {
        this.flgEnvio = flgEnvio;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "int_MarcaInsidencia=" + int_MarcaInsidencia +
                ", vch_ClaseTareo='" + vch_ClaseTareo + '\'' +
                ", vch_Actividad='" + vch_Actividad + '\'' +
                ", Vch_Tarea='" + Vch_Tarea + '\'' +
                ", DTM_FECMARCA='" + DTM_FECMARCA + '\'' +
                ", VCH_HORAMARCA='" + VCH_HORAMARCA + '\'' +
                ", VCH_MENSAJE='" + VCH_MENSAJE + '\'' +
                ", VCH_USUREG='" + VCH_USUREG + '\'' +
                ", DTM_FECREG='" + DTM_FECREG + '\'' +
                ", flgEnvio='" + flgEnvio + '\'' +
                '}';
    }
}
