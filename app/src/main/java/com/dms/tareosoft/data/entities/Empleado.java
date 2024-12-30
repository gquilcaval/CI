package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id", indices = {@Index(value = "id", unique = true)})
public class Empleado {

    @SerializedName("int_IdentificadorEmpleado")
    private int id;
    @SerializedName("vch_CodigoEmpleado")
    private String codigoEmpleado;
    @SerializedName("vch_ApellidoPaterno")
    private String apellidoPaterno;
    @SerializedName("vch_ApellidoMaterno")
    private String apellidoMaterno;
    @SerializedName("vch_NombresEmpleado")
    private String nombres;
    @SerializedName("int_FlgEstadoEmpleado")
    private int estadoEmpleado;
    @SerializedName("vch_IdentificadorTareo")
    private String identificadorTareo;
    @SerializedName("int_CodigoEmpresa")
    private int codigoEmpresa;
    @SerializedName("int_CodigoArea")
    private int codigoArea;
    @SerializedName("vch_TipoPlanilla")
    private String tipoPlanilla;
    @SerializedName("vch_codclase")
    private String codclase;
    @SerializedName("vch_codcategoria")
    private String codcategoria;
    @SerializedName("int_NumDoc")
    private int numDoc;
    @SerializedName("vch_Supervisor")
    private String supervisor;
    @SerializedName("vch_Descripcion")
    private String descripcionEmp;
    @SerializedName("vch_Mensaje")
    private String mensaje;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getEstadoEmpleado() {
        return estadoEmpleado;
    }

    public void setEstadoEmpleado(int estadoEmpleado) {
        this.estadoEmpleado = estadoEmpleado;
    }

    public String getIdentificadorTareo() {
        return identificadorTareo;
    }

    public void setIdentificadorTareo(String identificadorTareo) {
        this.identificadorTareo = identificadorTareo;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public String getTipoPlanilla() {
        return tipoPlanilla;
    }

    public void setTipoPlanilla(String tipoPlanilla) {
        this.tipoPlanilla = tipoPlanilla;
    }

    public String getCodclase() {
        return codclase;
    }

    public void setCodclase(String codclase) {
        this.codclase = codclase;
    }

    public String getCodcategoria() {
        return codcategoria;
    }

    public void setCodcategoria(String codcategoria) {
        this.codcategoria = codcategoria;
    }

    public int getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(int numDoc) {
        this.numDoc = numDoc;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getDescripcionEmp() {
        return descripcionEmp;
    }

    public void setDescripcionEmp(String descripcionEmp) {
        this.descripcionEmp = descripcionEmp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", nombres='" + nombres + '\'' +
                ", estadoEmpleado=" + estadoEmpleado +
                ", identificadorTareo='" + identificadorTareo + '\'' +
                ", codigoEmpresa=" + codigoEmpresa +
                ", codigoArea=" + codigoArea +
                ", tipoPlanilla='" + tipoPlanilla + '\'' +
                ", codclase='" + codclase + '\'' +
                ", codcategoria='" + codcategoria + '\'' +
                ", numDoc=" + numDoc +
                ", supervisor='" + supervisor + '\'' +
                ", descripcion='" + descripcionEmp + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
