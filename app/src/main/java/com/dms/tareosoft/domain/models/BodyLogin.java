package com.dms.tareosoft.domain.models;

import com.google.gson.annotations.SerializedName;

public class BodyLogin {

    @SerializedName("nombreUsuario")
    private String nombreUsuario;

    @SerializedName("claveUsuario")
    private String claveUsuario;

    public BodyLogin(String nombreUsuario, String claveUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.claveUsuario = claveUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }


    public String getClaveUsuario() {
        return claveUsuario;
    }

}
