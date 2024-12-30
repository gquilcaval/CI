package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface ITareoReubicarMasivoListContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);

        void closeWindows();
    }

    interface Presenter extends IBaseContract.Presenter<ITareoReubicarMasivoListContract.View> {
        void obtenerTareosIniciados();

        void createNewListDetalleTareo(String codTareo);

        void crearNuevoDetalleTareo();
    }
}
