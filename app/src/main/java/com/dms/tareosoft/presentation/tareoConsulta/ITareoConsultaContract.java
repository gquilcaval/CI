package com.dms.tareosoft.presentation.tareoConsulta;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.AllTareoRow;

import java.util.List;

public interface ITareoConsultaContract {

    interface View extends IBaseContract.View {
        void mostrarAllTareos(List<AllTareoRow> allTareos);
    }

    interface Presenter extends IBaseContract.Presenter<ITareoConsultaContract.View> {
        void obtenerAllTareos();
    }
}
