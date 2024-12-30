package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

public interface IReubicarTipoTareoContract {

    interface View extends IBaseContract.View {

        void salir();

        void changePage(int page);

        void showDetailedErrorDialog(ArrayList<String> listaErrores);

        void moveToTareoReubicarListActivity(Boolean correctly);
    }

    interface Presenter extends IBaseContract.Presenter<IReubicarTipoTareoContract.View> {

        void getListAllTareos();

        List<TareoRow> getListAllTareo();

        void setListEmpleadoPorFinalizar(List<AllEmpleadoRow> listEmpleadoPorFinalizar);

        List<AllEmpleadoRow> getListEmpleadoPorFinalizar();

        void fechaFinTareo(String fechaFinTareo);

        void horaFinTareo(String horaFinTareo);

        void fechaIniRefrigerio(String fechaIniRefrigerio);

        void fechaFinRefrigerio(String fechaFinRefrigerio);

        void setListaFechaTareos(List<String> listaFechaTareos);

        void validarDatosParaFinalizar(boolean refrigerio,
                                       List<ResultadoTareo> listaResultado,
                                       List<AllEmpleadoRow> listResultadoXTareo);

        boolean validarRefrigerio();

        boolean validarFechaFin();

        void crearResultadosEmpleado(List<ResultadoTareo> listaResultado,
                                     List<AllEmpleadoRow> listResultadoXTareo);

        void finalizarTareos(List<String> listFinTareos);

        void finalizarEmpleados(List<String> listFinEmpleados);
    }
}
