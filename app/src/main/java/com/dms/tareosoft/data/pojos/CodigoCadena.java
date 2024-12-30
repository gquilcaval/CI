package com.dms.tareosoft.data.pojos;

public class CodigoCadena {

    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "CodigoCadena{" +
                "codigo='" + codigo + '\'' +
                '}';
    }
}
