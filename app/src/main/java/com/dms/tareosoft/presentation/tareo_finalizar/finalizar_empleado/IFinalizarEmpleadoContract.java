package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado;

import com.dms.tareosoft.base.IBaseContract;

public interface IFinalizarEmpleadoContract {

    interface View extends IBaseContract.View {
        void salir();

    }

    interface Presenter extends IBaseContract.Presenter<IFinalizarEmpleadoContract.View>{

    }
}
