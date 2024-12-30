package com.dms.tareosoft.presentation.models;

public class CodigoEmpleadoRow {
    private String codigoEmpleado;
    private String numeroDocumento;

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }


    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    @Override
    public String toString() {
        return "CodigoEmpleadoRow{" +
                "codigoEmpleado='" + codigoEmpleado + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                '}';
    }
}
