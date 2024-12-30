package com.dms.tareosoft.domain.tareo_nuevo.nuevo_opciones;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class OpcionesInteractorImpl implements IOpcionesInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public OpcionesInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                  @NonNull DataSourceRemote dataSourceRemote,
                                  @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<List<UnidadMedida>> listarUnidadMedidas() {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarUnidadesMedida();
            case ModoTrabajo.BATCH:
                return local.listarUnidadesMedida();
            default:
                return null;
        }
    }

    @Override
    public Single<List<Turno>> listarTurnos() {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarTurnos();
            case ModoTrabajo.BATCH:
                return local.listarTurnos();
            default:
                return null;
        }
    }
}
