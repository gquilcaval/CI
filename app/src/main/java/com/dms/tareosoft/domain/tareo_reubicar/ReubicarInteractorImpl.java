package com.dms.tareosoft.domain.tareo_reubicar;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class ReubicarInteractorImpl implements IReubicarInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ReubicarInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                  @NonNull DataSourceRemote dataSourceRemote,
                                  @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }


    @Override
    public Maybe<List<AllEmpleadoRow>> obtenerAllEmpleadosWithTareo(@StatusTareo int statusTareo,
                                                                    @StatusFinDetalleTareo int statusFinDetalleTareo,
                                                                    int idUsuario) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
            default:
                return local.obtenerAllEmpleadosWithTareo(statusTareo, statusFinDetalleTareo, idUsuario);
        }
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int statusTareo, int usuario,
                                                        @StatusDescargaServidor String statusServidor) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
            default:
                return local.obtenerListaTareo(statusTareo, usuario, statusServidor);
        }
    }
}
