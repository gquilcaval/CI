package com.dms.tareosoft.data.models;

import com.dms.tareosoft.data.pojos.TareoPendiente;

import java.util.List;

public class BodyTareo {
    private List<TareoPendiente> tareos;

    public List<TareoPendiente> getTareos() {
        return tareos;
    }

    public void setTareosPendientes(List<TareoPendiente> tareosPendientes) {
        this.tareos = tareosPendientes;
    }
}
