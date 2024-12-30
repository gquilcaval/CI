package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorEmpleado;

import com.dms.tareosoft.annotacion.ResultadoPorEmpleadoModificado;
import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.ResultadoRow;

import java.util.List;

public interface IResultadoPorEmpleadoContract {


    interface View extends IBaseContract.View {

        void mostrarEmpleados(List<ResultadoRow> lista);

        void mostrarDialogoResultado(int cantProducida,
                                     @ResultadoPorEmpleadoModificado int result);

        void mostrarMensajeEmpleadoConResultado(int cantProducida,
                                                @ResultadoPorEmpleadoModificado int result);

        void salir();
    }

    interface Presenter extends IBaseContract.Presenter<IResultadoPorEmpleadoContract.View> {

        void guardarResultado(double cantidad, String codTareo,
                              @ResultadoPorEmpleadoModificado int result);

    }
}
