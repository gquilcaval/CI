package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.dms.tareosoft.annotacion.TypeUser;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Usuario")
public class Usuario {

    @PrimaryKey()
    @SerializedName("int_IdentificadorUsuario")
    private int idUsuario;
    @SerializedName("int_IdentificadorEmpleado")
    private int idEmpleado;
    @ForeignKey (entity = Perfil.class, parentColumns = "id", childColumns = "idPerfil")
    @TypeUser
    @SerializedName("int_IdentificadorPerfil")
    private int fkPerfil;
    @SerializedName("vch_NombreUsuario")
    private String usuario;
    @SerializedName("vch_ClaveUsuario")
    private String clave;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getFkPerfil() {
        return fkPerfil;
    }

    public void setFkPerfil(int fkPerfil) {
        this.fkPerfil = fkPerfil;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", idEmpleado=" + idEmpleado +
                ", fkPerfil=" + fkPerfil +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                '}';
    }
}
