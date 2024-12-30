package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porTareo;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

public interface IListaReubicarPorTareoContract {

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

    interface Presenter extends IBaseContract.Presenter<IListaReubicarPorTareoContract.View> {
        void obtenerAllEmpleadosWithTareo();

        void obtenerTareosIniciados();

        int getTimeValidezTareo();

        String getcurrentDateTime();

        List<TareoRow> getListaAllTareos();

        List<String> getListaFechaTareos();

        void addListaFechaTareos(String fechaTareo);

        void removeListaFechaTareos(String fechaTareo);

        void clearListaFechaTareos();

        List<AllEmpleadoRow> getListaAllEmpleados();

        void addListaAllEmpleados(AllEmpleadoRow allEmpleadoRow);

        void removeListaAllEmpleados(AllEmpleadoRow allEmpleadoRow);

        void clearListaAllEmpleados();

        List<TareoRow> getListaTmpTareos();

        void addListaTmpTareos(TareoRow tareoRow);

        void removeListTmpTareos(TareoRow tareoRow);

        void clearListTmpTareos();

        int claseTareoEmpleado(List<AllEmpleadoRow> listaAllEmpleados);

        boolean crearNewTareo(int clase);

        boolean isMismaClase(TareoRow item);

        boolean isMismoResultado(TareoRow item);

        void moveToOneToOthers();

        void actionReubicar();
    }
}
