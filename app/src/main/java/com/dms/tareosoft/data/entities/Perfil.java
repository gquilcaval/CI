package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dms.tareosoft.annotacion.TypeUser;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Perfil")
public class Perfil {

    @TypeUser
    @PrimaryKey()
    @SerializedName("int_IdentificadorPerfil")
    private int idPerfil;
    @SerializedName("vch_DescripcionPerfil")
    private String descripcion;

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "idPerfil=" + idPerfil +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
