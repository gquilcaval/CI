package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_empleados;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;

import java.util.List;

public interface ITabEmpleadoContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<EstadoEmpleadoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<ITabEmpleadoContract.View>{

    }
}
