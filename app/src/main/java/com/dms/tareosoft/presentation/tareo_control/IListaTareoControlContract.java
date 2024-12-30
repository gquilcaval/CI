package com.dms.tareosoft.presentation.tareo_control;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IListaTareoControlContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IListaTareoControlContract.View>{
        void obtenerTareosIniciados();

        void deleteTareo(String codigoTareo);
    }
}
