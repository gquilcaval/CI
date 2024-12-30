package com.dms.tareosoft.data.pojos;

public class CodigoNumerico {

    private int codigo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "CodigoNumerico{" +
                "codigo=" + codigo +
                '}';
    }
}
