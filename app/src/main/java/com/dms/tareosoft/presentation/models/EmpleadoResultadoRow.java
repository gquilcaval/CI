package com.dms.tareosoft.presentation.models;

public class EmpleadoResultadoRow {
    private String detalleTareo;
    private int cantidadProducida;

    public String getDetalleTareo() {
        return detalleTareo;
    }

    public void setDetalleTareo(String detalleTareo) {
        this.detalleTareo = detalleTareo;
    }

    public int getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(int cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    @Override
    public String toString() {
        return "EmpleadoResultadoRow{" +
                "detalleTareo='" + detalleTareo + '\'' +
                ", cantidadProducida=" + cantidadProducida +
                '}';
    }
}
