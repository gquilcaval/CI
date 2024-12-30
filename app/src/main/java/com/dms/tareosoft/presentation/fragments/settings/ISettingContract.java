package com.dms.tareosoft.presentation.fragments.settings;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.data.models.ContenidoGeneral;
import com.dms.tareosoft.data.models.ContenidoTareo;

import java.util.List;

public interface ISettingContract {

    interface View extends IBaseContract.View {

        void reiniciar();

        void closed();

        void actualizarFechaHora(String mensaje);

        void mostrarFechaHora(String fechaCarga, String fechaHoraServidor);

        void mostrarCampos(ContenidoGeneral contenidoGeneral, ContenidoTareo contenidoTareo, ContenidoAjustes contenidoAjustes);

        void limpiarFlags();

        void listarUnidadMedida(List<UnidadMedida> lista, int posicion);

        void listarTurno(List<String> turnos, int posicion);

        void listarClaseTarea(List<String> clasesTareos, int posicion);

        void setTextButton(String message);

        void fechaDesfasada();
    }

    interface Presenter extends IBaseContract.Presenter<ISettingContract.View> {

        void validarCambios(boolean flag_tab_general, boolean flag_tab_ajustes, boolean flag_tab_tareo);

        void validarConexion();

        void sincronizarMaestros();

        void cargarVista();

        void guardarCampos(boolean flag_url, boolean flag_tab_general, boolean flag_tab_ajustes, boolean flag_tab_tareo);

        void descargarMaestros(boolean flag_tab_general, boolean flag_tab_ajustes, boolean flag_tab_tareo);

    }
}
