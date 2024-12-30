package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_empleado;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

public interface IControlTareoContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<EmpleadoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IControlTareoContract.View>{
        void guardarControl(EmpleadoControlRow empleado, String codigoTareo);
        void validarTrabajador(String codTareo, String codEmpleado);
        void obtenerControles(String codTareo);
    }
}
