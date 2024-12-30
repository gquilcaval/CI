package com.dms.tareosoft.domain.tareo_reubicar.tareo_reubicar_list;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TareoReubicarListInteractorImpl implements ITareoReubicarListInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoReubicarListInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                           @NonNull DataSourceRemote dataSourceRemote,
                                           @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int estado, int usuario,
                                                        @StatusDescargaServidor String estadoEnvio,
                                                        List<String> listCodTareos,
                                                        List<Integer> listCodClase) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerTareosIniciadosOnly(estado, usuario, estadoEnvio,
                        listCodTareos, listCodClase);
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
