package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_cantidad_por_destajo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;

public interface ITareoReubicarMasivoPorDestajoCantidadContract {

    interface View extends IBaseContract.View {
        boolean hasCantidad();

        void mostrarListParaJornal(ArrayList<AllEmpleadoRow> listaPorJornal);

        ArrayList<ResultadoTareo> listaResultado();

        ArrayList<AllEmpleadoRow> listResultadoXTareo();

        void mostrarDialogCantidadParaDestajo(AllEmpleadoRow porDestajo, int position,int cantidad);
    }

    interface Presenter extends IBaseContract.Presenter<ITareoReubicarMasivoPorDestajoCantidadContract.View> {
        void validarEmpleados(AllEmpleadoRow porJornal, int position);

        void setListAllJornal(ArrayList<AllEmpleadoRow> allPorJornal);
    }
}
