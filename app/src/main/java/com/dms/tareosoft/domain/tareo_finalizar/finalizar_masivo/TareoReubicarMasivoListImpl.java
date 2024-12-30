package com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TareoReubicarMasivoListImpl implements ITareoReubicarMasivoListInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoReubicarMasivoListImpl(@NonNull DataSourceLocal dataSourceLocal,
                                       @NonNull DataSourceRemote dataSourceRemote,
                                       @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int statusTareo,
                                                        int usuario,
                                                        @StatusDescargaServidor String statusServer) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaTareo(statusTareo, usuario,statusServer);
            case ModoTrabajo.BATCH:
                return local.obtenerListaTareo(statusTareo, usuario,statusServer);
            default:
                return null;
        }
    }

    @Override
    public Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.createNewDetalleTareo(detalleTareos);
            case ModoTrabajo.BATCH:
                return local.createNewDetalleTareo(detalleTareos);
            default:
                return null;
        }
    }
}
