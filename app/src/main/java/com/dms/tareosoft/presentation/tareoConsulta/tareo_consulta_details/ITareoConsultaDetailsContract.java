package com.dms.tareosoft.presentation.tareoConsulta.tareo_consulta_details;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface ITareoConsultaDetailsContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<AllEmpleadosConsulta> lista);

        void mostrarDetalleTareo(TareoRow tareoRow);

        void closed();
    }

    interface Presenter extends IBaseContract.Presenter<ITareoConsultaDetailsContract.View> {

        void setCodTareo(String codTareo);

        void obtenerDetailTareo();

        void obtenerAllEmpleados();

        void deleteTareo(String codTareo);
    }
}
