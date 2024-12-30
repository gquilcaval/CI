package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.util.TextUtil;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(primaryKeys = "idConceptoTareo", indices = {@Index(value = "idConceptoTareo", unique = true)})
public class ConceptoTareo implements Serializable {

    @SerializedName("int_IdentificadorConcepto")
    private int idConceptoTareo;

    @SerializedName("int_IdentificadorNivel")
    private int fkNivel;

    @SerializedName("int_OrdenNivel")
    private int ordenNivel;

    @SerializedName("vch_CodigoConcepto")
    private String codConcepto;

    @SerializedName("vch_DescripcionConcepto")
    private String descripcion;

    @SerializedName("int_IdentificadorConceptoPadre")
    private String fkConceptoPadre;

    public int getIdConceptoTareo() {
        return idConceptoTareo;
    }

    public void setIdConceptoTareo(int idConceptoTareo) {
        this.idConceptoTareo = idConceptoTareo;
    }

    public int getFkNivel() {
        return fkNivel;
    }

    public void setFkNivel(int fkNivel) {
        this.fkNivel = fkNivel;
    }

    public int getOrdenNivel() {
        return ordenNivel;
    }

    public void setOrdenNivel(int ordenNivel) {
        this.ordenNivel = ordenNivel;
    }

    public String getCodConcepto() {
        return codConcepto;
    }

    public void setCodConcepto(String codConcepto) {
        this.codConcepto = codConcepto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFkConceptoPadre() {
        return fkConceptoPadre;
    }

    public void setFkConceptoPadre(String fkConceptoPadre) {
        this.fkConceptoPadre = fkConceptoPadre;
    }

    @Override
    public String toString() {
        return "ConceptoTareo{" +
                "idConceptoTareo=" + idConceptoTareo +
                ", fkNivel=" + fkNivel +
                ", ordenNivel=" + ordenNivel +
                ", codConcepto='" + codConcepto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fkConceptoPadre='" + fkConceptoPadre + '\'' +
                '}';
    }
}
