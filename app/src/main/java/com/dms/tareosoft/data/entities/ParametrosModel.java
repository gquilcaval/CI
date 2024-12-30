package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;


@Entity(primaryKeys = "INT_IDPARAMETRO",indices = {@Index(value = "INT_IDPARAMETRO",unique = true)})
public class ParametrosModel {
    @SerializedName("INT_IDPARAMETRO")
    private int INT_IDPARAMETRO;
    @SerializedName("VCH_CODIGOPARAMETRO")
    private String VCH_CODIGOPARAMETRO;
    @SerializedName("VCH_NOMBREPARAMETRO")
    private String VCH_NOMBREPARAMETRO;
    @SerializedName("VCH_DESCRIPCIONPARAMETRO")
    private String VCH_DESCRIPCIONPARAMETRO;
    @SerializedName("VCH_VALORPARAMETRO")
    private String VCH_VALORPARAMETRO;

    public int getINT_IDPARAMETRO() {
        return INT_IDPARAMETRO;
    }

    public void setINT_IDPARAMETRO(int INT_IDPARAMETRO) {
        this.INT_IDPARAMETRO = INT_IDPARAMETRO;
    }

    public String getVCH_CODIGOPARAMETRO() {
        return VCH_CODIGOPARAMETRO;
    }

    public void setVCH_CODIGOPARAMETRO(String VCH_CODIGOPARAMETRO) {
        this.VCH_CODIGOPARAMETRO = VCH_CODIGOPARAMETRO;
    }

    public String getVCH_NOMBREPARAMETRO() {
        return VCH_NOMBREPARAMETRO;
    }

    public void setVCH_NOMBREPARAMETRO(String VCH_NOMBREPARAMETRO) {
        this.VCH_NOMBREPARAMETRO = VCH_NOMBREPARAMETRO;
    }

    public String getVCH_DESCRIPCIONPARAMETRO() {
        return VCH_DESCRIPCIONPARAMETRO;
    }

    public void setVCH_DESCRIPCIONPARAMETRO(String VCH_DESCRIPCIONPARAMETRO) {
        this.VCH_DESCRIPCIONPARAMETRO = VCH_DESCRIPCIONPARAMETRO;
    }

    public String getVCH_VALORPARAMETRO() {
        return VCH_VALORPARAMETRO;
    }

    public void setVCH_VALORPARAMETRO(String VCH_VALORPARAMETRO) {
        this.VCH_VALORPARAMETRO = VCH_VALORPARAMETRO;
    }
}
