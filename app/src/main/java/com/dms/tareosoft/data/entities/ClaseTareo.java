package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.util.TextUtil;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id",indices = {@Index(value = "descripcion", unique = true)})
public class ClaseTareo {

    private int id;
    private String descripcion;
    private String cod_planilla;

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

    public String getCod_planilla() {
        return cod_planilla;
    }

    public void setCod_planilla(String cod_planilla) {
        this.cod_planilla = cod_planilla;
    }

    @Override
    public String toString() {
        return "ClaseTareo{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", cod_planilla='" + cod_planilla + '\'' +
                '}';
    }
}
