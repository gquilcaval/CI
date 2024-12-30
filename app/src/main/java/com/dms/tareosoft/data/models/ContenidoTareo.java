package com.dms.tareosoft.data.models;

public class ContenidoTareo {

    private int unidadMedida;
    private int claseTareo;
    private int turno;

    public int getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(int unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getClaseTareo() {
        return claseTareo;
    }

    public void setClaseTareo(int claseTareo) {
        this.claseTareo = claseTareo;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "ContenidoTareo{" +
                "unidadMedida=" + unidadMedida +
                ", claseTareo=" + claseTareo +
                ", turno=" + turno +
                '}';
    }
}
