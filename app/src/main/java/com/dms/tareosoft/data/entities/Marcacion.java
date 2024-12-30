package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dms.tareosoft.annotacion.TypeUser;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Marcacion")
public class Marcacion {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("int_MarcaAsistencia")
    private int idMarcacion;
    @SerializedName("vch_CodigoEmpleado")
    private String codigoEmpleado;
    @SerializedName("vch_ClaseTareo")
    private String claseTareo;
    @SerializedName("vch_Actividad")
    private String actividad;
    @SerializedName("Vch_Tarea")
    private String tarea;
    @SerializedName("DTM_FECMARCA")
    private String fechaMarca;
    @SerializedName("VCH_HORAMARCA")
    private String horaMarca;
    @SerializedName("FLGOPE")
    private String flGope;
    @SerializedName("VCH_MENSAJE")
    private String mensaje;
    @SerializedName("VCH_TIPO")
    private String tipo;
    @SerializedName("VCH_USUREG")
    private String usuarioRegistro;
    @SerializedName("DTM_FECREG")
    private String fechRegistro;
    private String flgEnvio;
    private String nombreEmpleado;
    private String supervisor;

    public int getIdMarcacion() {
        return idMarcacion;
    }

    public void setIdMarcacion(int idMarcacion) {
        this.idMarcacion = idMarcacion;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getClaseTareo() {
        return claseTareo;
    }

    public void setClaseTareo(String claseTareo) {
        this.claseTareo = claseTareo;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getFechaMarca() {
        return fechaMarca;
    }

    public void setFechaMarca(String fechaMarca) {
        this.fechaMarca = fechaMarca;
    }

    public String getHoraMarca() {
        return horaMarca;
    }

    public void setHoraMarca(String horaMarca) {
        this.horaMarca = horaMarca;
    }

    public String getFlGope() {
        return flGope;
    }

    public void setFlGope(String flGope) {
        this.flGope = flGope;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public String getFechRegistro() {
        return fechRegistro;
    }

    public void setFechRegistro(String fechRegistro) {
        this.fechRegistro = fechRegistro;
    }

    public String getFlgEnvio() {
        return flgEnvio;
    }

    public void setFlgEnvio(String flgEnvio) {
        this.flgEnvio = flgEnvio;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public String toString() {
        return "Marcacion{" +
                "idMarcacion=" + idMarcacion +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", claseTareo='" + claseTareo + '\'' +
                ", actividad='" + actividad + '\'' +
                ", tarea='" + tarea + '\'' +
                ", fechaMarca='" + fechaMarca + '\'' +
                ", horaMarca='" + horaMarca + '\'' +
                ", flGope='" + flGope + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tipo='" + tipo + '\'' +
                ", usuarioRegistro='" + usuarioRegistro + '\'' +
                ", fechRegistro='" + fechRegistro + '\'' +
                ", flgEnvio='" + flgEnvio + '\'' +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", supervisor='" + supervisor + '\'' +
                '}';
    }
}
