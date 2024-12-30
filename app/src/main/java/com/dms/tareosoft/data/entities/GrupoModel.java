package com.dms.tareosoft.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "vchCodigoGrupo",indices = {@Index(value = "vchCodigoGrupo", unique = true)})
public class GrupoModel {
    @NonNull
    @SerializedName("vchCodigoGrupo")
    private String vchCodigoGrupo;
    @SerializedName("vchDescripcionGrupo")
    private String vchDescripcionGrupo;

    public String getVchCodigoGrupo() {
        return vchCodigoGrupo;
    }

    public void setVchCodigoGrupo(String vchCodigoGrupo) {
        this.vchCodigoGrupo = vchCodigoGrupo;
    }

    public String getVchDescripcionGrupo() {
        return vchDescripcionGrupo;
    }

    public void setVchDescripcionGrupo(String vchDescripcionGrupo) {
        this.vchDescripcionGrupo = vchDescripcionGrupo;
    }
}
