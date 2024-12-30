package com.dms.tareosoft.domain.models;

import com.dms.tareosoft.data.pojos.CodigoNumerico;

import java.util.List;

public class RespuestaNumerica {
    private Boolean status;
    private String msg;
    private List<CodigoNumerico> datos;

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

    public List<CodigoNumerico> getDatos() {
        return datos;
    }

    public void setDatos(List<CodigoNumerico> datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "RespuestaNumerica{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", datos=" + datos +
                '}';
    }
}
