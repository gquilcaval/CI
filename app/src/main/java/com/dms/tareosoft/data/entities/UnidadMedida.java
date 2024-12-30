package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.util.TextUtil;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id", indices = {@Index(value = "descripcion", unique = true)})
public class UnidadMedida {

    @SerializedName("INT_IDENTIFICADORUNIDADMEDIDA")
    private int id;
    @SerializedName("VCH_DESCRIPCIONUNIDADMEDIDA")
    private String descripcion;

    public UnidadMedida(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "UnidadMedida{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
