package com.dms.tareosoft.presentation.asistencia.registro;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.OpcionesTareo;

import java.util.ArrayList;
import java.util.List;

public interface IRegistroAsistenciaContract {

    interface View extends IBaseContract.View {
        void salir();

        void changePage(int page);

        void showDetailedErrorDialog(ArrayList<String> listaErrores);

    }

    interface Presenter extends IBaseContract.Presenter<IRegistroAsistenciaContract.View> {
        void guardarTareo(Tareo nuevoTareo, int niveles, List<DetalleTareo> empleados);
    }
}
