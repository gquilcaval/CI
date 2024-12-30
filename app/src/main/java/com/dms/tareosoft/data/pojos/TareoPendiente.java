package com.dms.tareosoft.data.pojos;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.List;

public class TareoPendiente {
    @Embedded
    public Tareo tareo;
    @Relation(parentColumn = "codTareo", entityColumn = "fkTareo", entity = DetalleTareo.class)
    public List<DetalleTareo> detalle;

    public Tareo getTareo() {
        return tareo;
    }

    public void setTareo(Tareo tareo) {
        this.tareo = tareo;
    }

    public List<DetalleTareo> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleTareo> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "TareoPendiente{" +
                "tareo=" + tareo +
                ", detalle=" + detalle +
                '}';
    }
}
