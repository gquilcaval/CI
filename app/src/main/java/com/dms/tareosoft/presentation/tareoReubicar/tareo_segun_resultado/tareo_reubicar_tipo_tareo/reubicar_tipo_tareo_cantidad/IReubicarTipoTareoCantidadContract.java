package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

public interface IReubicarTipoTareoCantidadContract {

    interface View extends IBaseContract.View {

        boolean hasCantidad();

        void mostrarListAllEmpleadosPorEmpleado(ArrayList<AllEmpleadoRow> listAllEmpleados);

        void mostrarListAllTareoEnd(List<AllTareosWithResult> listAllTareoEnd);

        ArrayList<ResultadoTareo> listAllTareos();

        ArrayList<AllEmpleadoRow> listAllEmpleados();

        void mostrarDialogCantidadTipoEmpleado(AllEmpleadoRow listAllEmpleados, int position);

        void mostrarDialogCantidadTipoTareo(TareoRow listAllEmpleados, int position);

        void actualizarResultado();
    }

    interface Presenter extends IBaseContract.Presenter<View> {

        void obtenerTareoConResultados(List<String> allTareoEnd);

        void validarEmpleados(AllEmpleadoRow listAllEmpleados, int position);

        void validarTareo(TareoRow tareoRow, int position);

        void setListAllEmpleados(ArrayList<AllEmpleadoRow> listAllEmpleados);

        void guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo);
    }
}
