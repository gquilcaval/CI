package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;

public interface IReubicarTipoEmpleadoCantidadContract {

    interface View extends IBaseContract.View {

        boolean hasCantidad();

        void mostrarListAllEmpleadosPorEmpleado(ArrayList<AllEmpleadoRow> listAllEmpleados);

        ArrayList<ResultadoTareo> listAllTareos();

        ArrayList<AllEmpleadoRow> listAllEmpleados();

        void mostrarDialogCantidadTipoEmpleado(AllEmpleadoRow listAllEmpleados, int position);
    }

    interface Presenter extends IBaseContract.Presenter<View> {

        void validarEmpleados(AllEmpleadoRow listAllEmpleados, int position);

        void setListAllEmpleados(ArrayList<AllEmpleadoRow> listAllEmpleados);
    }
}
