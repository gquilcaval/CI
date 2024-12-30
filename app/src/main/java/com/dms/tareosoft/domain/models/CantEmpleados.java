package com.dms.tareosoft.domain.models;

public class CantEmpleados {
    private String codigo;
    private int cantEmpleados;

    public CantEmpleados(String codigo, int cantEmpleados) {
        this.codigo = codigo;
        this.cantEmpleados = cantEmpleados;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCantEmpleados() {
        return cantEmpleados;
    }

    public void setCantEmpleados(int cantEmpleados) {
        this.cantEmpleados = cantEmpleados;
    }

    @Override
    public String toString() {
        return "CantEmpleados{" +
                "codigo='" + codigo + '\'' +
                ", cantEmpleados=" + cantEmpleados +
                '}';
    }
}
