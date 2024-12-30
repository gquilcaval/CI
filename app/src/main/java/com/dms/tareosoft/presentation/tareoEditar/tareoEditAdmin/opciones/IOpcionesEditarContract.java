package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.opciones;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.presentation.models.OpcionesTareo;

import java.util.Date;

public interface IOpcionesEditarContract {

    interface View extends IBaseContract.View {
        OpcionesTareo obtenerCampos();

        void mostrarUnidadMedida(String descripcion);

        void mostrarTurno(String descripcion);

        void mostrarCampos(String tipo, String fechaInicio, String horaInicio);
    }

    interface Presenter extends IBaseContract.Presenter<IOpcionesEditarContract.View> {
        void obtenerCamposTareo(String tareo);

        boolean validarFechaInicio(Date date);
    }
}
