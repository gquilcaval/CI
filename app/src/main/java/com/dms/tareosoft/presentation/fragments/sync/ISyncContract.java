package com.dms.tareosoft.presentation.fragments.sync;

import com.dms.tareosoft.base.IBaseContract;

public interface ISyncContract {

    interface View extends IBaseContract.View {

        void mostrarFechaHora(String fechaServidor, String fechaSincronizada);

        void actualizarFechaHora(String s);

        void mostrarMensajeTareosActivos();

        void mostrarMensajeSyncExitosa();

        void mostrarMensajeSyncFallida(String message);

        void mostrarMensajeSinDatos();

        void mostrarMensajeSinMarcaciones();

        void mostrarMensajeSinIncidencias();
    }

    interface Presenter extends IBaseContract.Presenter<ISyncContract.View> {
        void validarFechaHora();

        void sincronizarFechaHora();

        void obtenerMarcacionesPendientesEnvio();
    }
}
