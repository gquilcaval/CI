package com.dms.tareosoft.data.pojos;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.presentation.models.EmpleadoConsultaRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public class AllTareosWithResult {
    @Embedded
    public TareoRow tareo;
    @Relation(parentColumn = "codigo", entityColumn = "fkTareo", entity = ResultadoPorTareo.class)
    public List<ResultadoPorTareo> resultado;

    public TareoRow getTareo() {
        return tareo;
    }

    public void setTareo(TareoRow tareo) {
        this.tareo = tareo;
    }

    public List<ResultadoPorTareo> getResultado() {
        return resultado;
    }

    public void setResultado(List<ResultadoPorTareo> resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "AllTareosWithResult{" +
                "tareo=" + tareo +
                ", resultado=" + resultado +
                '}';
    }
}
