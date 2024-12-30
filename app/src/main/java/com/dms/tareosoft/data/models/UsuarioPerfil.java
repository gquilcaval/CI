package com.dms.tareosoft.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Perfil;
import com.dms.tareosoft.data.entities.Usuario;

public class UsuarioPerfil {
    @Embedded
    public Usuario usuario;
    @Embedded
    public Perfil perfil;
    @Embedded
    public Empleado empleado;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public String toString() {
        return "UsuarioPerfil{" +
                "usuario=" + usuario +
                ", perfil=" + perfil +
                ", empleado=" + empleado +
                '}';
    }
}
