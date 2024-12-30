package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_tareo;

import com.dms.tareosoft.base.IBaseContract;

public interface ITabTareoContract {

    interface View extends IBaseContract.View {
        void cerrar();

        void showRefrig(boolean show);
    }

    interface Presenter extends IBaseContract.Presenter<ITabTareoContract.View> {
        void finalizarEmpleado(String codigoTareo, String codigoEmpleado, boolean refrigerio);
    }
}
