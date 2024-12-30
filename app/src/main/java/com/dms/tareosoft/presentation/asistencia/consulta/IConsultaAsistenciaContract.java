package com.dms.tareosoft.presentation.asistencia.consulta;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Marcacion;

import java.util.List;

public interface IConsultaAsistenciaContract {

    interface View extends IBaseContract.View {
        void displayMarcaciones(List<Marcacion> lista);

        void onSearchViewListener();

        void limpiarRecyclerview();
    }

    interface Presenter extends IBaseContract.Presenter<IConsultaAsistenciaContract.View>{
        void searchMarcacionesEmpleado(String query);
    }
}
