package com.dms.tareosoft.presentation.models;

import com.dms.tareosoft.annotacion.TypeView;

public class AllEmpleadoAdapter {
    @TypeView
    private int typeView;
    private String concepto1;
    private AllEmpleadoRow data;

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public String getConcepto1() {
        return concepto1;
    }

    public void setConcepto1(String concepto1) {
        this.concepto1 = concepto1;
    }

    public AllEmpleadoRow getData() {
        return data;
    }

    public void setData(AllEmpleadoRow data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AllEmpleadoAdapter{" +
                "typeView=" + typeView +
                ", concepto1='" + concepto1 + '\'' +
                ", data=" + data +
                '}';
    }
}