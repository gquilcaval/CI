package com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface ITareoReubicarListContract {

    interface View extends IBaseContract.View {

        void mostrarTareos(List<TareoRow> lista);

        void closeWindows();

        void reubicarExitoso();
    }

    interface Presenter extends IBaseContract.Presenter<ITareoReubicarListContract.View> {

        void obtenerTareosIniciados();

        void createNewListDetalleTareo(String codTareo);

        void crearNuevoDetalleTareo();

        int getTimeValidezTareo();

        String getcurrentDateTime();
    }
}
