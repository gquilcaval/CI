package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorTareo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;

import java.util.List;

public interface IResultadoPorTareoContract {


    interface View extends IBaseContract.View {

        void mostrarListResult(List<AllResultadoPorTareoRow> lista);

        void salir();
    }

    interface Presenter extends IBaseContract.Presenter<IResultadoPorTareoContract.View> {

        void setCodTareo(String codTareo);

        void obtenerListResult();

        void setCantProd(double cantidad);

        void guardarResultadoPorTareo();
    }
}
