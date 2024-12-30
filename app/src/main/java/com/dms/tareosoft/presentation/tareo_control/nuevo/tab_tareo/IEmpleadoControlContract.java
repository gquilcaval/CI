package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_tareo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

public interface IEmpleadoControlContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<EmpleadoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IEmpleadoControlContract.View>{

    }
}
