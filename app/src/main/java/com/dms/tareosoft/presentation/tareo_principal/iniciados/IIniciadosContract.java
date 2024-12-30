package com.dms.tareosoft.presentation.tareo_principal.iniciados;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IIniciadosContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IIniciadosContract.View>{
        void obtenerTareosIniciados();

        void deleteTareo(String codigoTareo);
    }
}
