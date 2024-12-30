package com.dms.tareosoft.domain.models;

import com.dms.tareosoft.data.pojos.CodigoCadena;

import java.util.List;

public class RespuestaTareo {
    private Boolean status;
    private String msg;
    private List<CodigoCadena> datos;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CodigoCadena> getDatos() {
        return datos;
    }

    public void setDatos(List<CodigoCadena> datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "RespuestaTareo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", datos=" + datos +
                '}';
    }
}
