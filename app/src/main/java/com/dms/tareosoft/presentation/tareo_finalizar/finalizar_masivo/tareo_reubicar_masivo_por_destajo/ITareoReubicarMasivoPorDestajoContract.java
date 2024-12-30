package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;

public interface ITareoReubicarMasivoPorDestajoContract {

    interface View extends IBaseContract.View {
        void salir();

        void changePage(int page);

        void showDetailedErrorDialog(ArrayList<String> listaErrores);

        void moveToTareoReubicarMasivoListActivity(Boolean correctly);
    }

    interface Presenter extends IBaseContract.Presenter<ITareoReubicarMasivoPorDestajoContract.View> {

        void validarDatosParaFinalizar(boolean refrigerio,
                                       ArrayList<ResultadoTareo> listaResultado,
                                       ArrayList<AllEmpleadoRow> listResultadoXTareo);
    }
}
