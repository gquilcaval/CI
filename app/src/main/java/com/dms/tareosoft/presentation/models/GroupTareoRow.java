package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.TypeView;

public class GroupTareoRow {
    @TypeView
    private int typeView;
    private String nivel1;
    private TareoRow data;

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public String getNivel1() {
        return nivel1;
    }

    public void setNivel1(String nivel1) {
        this.nivel1 = nivel1;
    }

    public TareoRow getData() {
        return data;
    }

    public void setData(TareoRow data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GroupTareoRow{" +
                "typeView=" + typeView +
                ", nivel1='" + nivel1 + '\'' +
                ", data=" + data +
                '}';
    }
}
