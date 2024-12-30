package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.OpcionesTareo;

import java.util.ArrayList;
import java.util.List;

public interface NewTareoNotEmployerContract {

    interface View extends IBaseContract.View {
        void salir();

        void changePage(int page);

        void showDetailedErrorDialog(ArrayList<String> listaErrores);

        String getFechaInicio();

        String getHoraInicio();

        void setResult(boolean isValid);
    }

    interface Presenter extends IBaseContract.Presenter<NewTareoNotEmployerContract.View> {
        void guardarTareo(Tareo nuevoTareo, int niveles, OpcionesTareo obtenerCampos);
    }
}
