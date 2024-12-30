package com.dms.tareosoft.presentation.tareo_finalizar;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IPorFinalizarContract {

    interface View extends IBaseContract.View {
        void mostrarTareos(List<TareoRow> lista);
    }

    interface Presenter extends IBaseContract.Presenter<IPorFinalizarContract.View> {
        void obtenerTareosIniciados();

        void deleteTareo(String codigoTareo);

        boolean validarTiempoExcedido(String fechaTareo);

        int horasValidesTareo();
    }
}
