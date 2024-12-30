package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

public interface IFinalizarMasivoContract {

    interface View extends IBaseContract.View {
        void closeWindow();

        void showRefrig(boolean show);

        void showCheckAuto(boolean show);

        void enableFehaHoraFin(boolean enable);

        void moveToReubicarAllEmpleados(ArrayList<AllEmpleadoRow> listAllEmpleados);

        void moveToTareoReubicarMasivoPorDestajoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                                         List<TareoRow> listaAllTareos,
                                                         List<AllEmpleadoRow> listPorJornal,
                                                         List<String> listaFechaTareos);
    }

    interface Presenter extends IBaseContract.Presenter<IFinalizarMasivoContract.View> {

        void obtenerAllEmpleados(ArrayList<String> listaCodTareos);

        void finalizarTareo(boolean refrigerio);
    }
}
