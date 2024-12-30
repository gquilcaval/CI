package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.util.TextUtil;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id", indices = {@Index(value = "descripcion", unique = true)})
public class Turno {

    @SerializedName("int_IdentificadorTurno")
    private int id;
    @SerializedName("vch_Descripcion")
    private String descripcion;

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
        return "Turno{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
