package com.dms.tareosoft.presentation.tareo_principal.finalizados;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IFinalizadosContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IFinalizadosContract.View>{
        void obtenerTareosIniciados();

        void deleteTareo(String codigoTareo);
    }
}
