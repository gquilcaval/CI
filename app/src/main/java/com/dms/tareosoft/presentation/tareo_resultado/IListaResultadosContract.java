package com.dms.tareosoft.presentation.tareo_resultado;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IListaResultadosContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IListaResultadosContract.View>{
        void obtenerTareosIniciados();

        void deleteTareo(String codigoTareo);
    }
}
