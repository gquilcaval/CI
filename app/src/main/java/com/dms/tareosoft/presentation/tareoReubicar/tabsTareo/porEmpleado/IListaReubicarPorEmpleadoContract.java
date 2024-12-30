package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porEmpleado;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IListaReubicarPorEmpleadoContract {

    interface View extends IBaseContract.View {
        void mostrarAllEmpleados(List<AllEmpleadoRow> lista);

        void moveToReubicarTipoEmpleadoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                                List<String> listaFechaTareos);

        void moveToReubicarTipoTareoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                             List<String> listaAllTareoEnd,
                                             List<String> listaFechaTareos);

        void mostrarMensajeOptionCrearTareoNew(int clase);

        void mostrarMensajeCrearTareoNew(int clase);
    }

    interface Presenter extends IBaseContract.Presenter<IListaReubicarPorEmpleadoContract.View> {
        void obtenerAllEmpleadosWithTareo();

        void obtenerTareosIniciados();

        int getTimeValidezTareo();

        String getcurrentDateTime();

        void addListaFechaTareos(String fechaTareo);

        void removeListaFechaTareos(String fechaTareo);

        void clearListaFechaTareos();

        List<AllEmpleadoRow> getListEmpleadoPorFinalizar();

        void addListEmpleadoPorFinalizar(AllEmpleadoRow allEmpleadoRow);

        void removeListEmpleadoPorFinalizar(AllEmpleadoRow allEmpleadoRow);

        void clearListEmpleadoPorFinalizar();

        List<String> getListCodTareos();

        void addListCodTareos(String tareoRow);

        void removeListTmpTareos(String tareoRow);

        void clearListTmpTareos();

        int claseTareoEmpleado(List<AllEmpleadoRow> listaAllEmpleados);

        boolean crearNewTareo(int clase);

        boolean isMismaClase(AllEmpleadoRow item);

        boolean isMismoResultado(AllEmpleadoRow item);

        void moveToOneToOthers();

        void actionReubicar();
    }
}
