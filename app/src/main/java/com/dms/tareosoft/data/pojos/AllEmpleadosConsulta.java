package com.dms.tareosoft.data.pojos;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.presentation.models.EmpleadoConsultaRow;

import java.util.List;

public class AllEmpleadosConsulta {
    @Embedded
    public EmpleadoConsultaRow empleado;
    @Relation(parentColumn = "codigoEmpleado", entityColumn = "codEmpleado", entity = DetalleTareoControl.class)
    public List<DetalleTareoControl> controles;

    public EmpleadoConsultaRow getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoConsultaRow empleado) {
        this.empleado = empleado;
    }

    public List<DetalleTareoControl> getControles() {
        return controles;
    }

    public void setControles(List<DetalleTareoControl> controles) {
        this.controles = controles;
    }

    @Override
    public String toString() {
        return "AllEmpleadosConsulta{" +
                "empleado=" + empleado +
                ", controles=" + controles +
                '}';
    }
}
