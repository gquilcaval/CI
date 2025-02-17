package com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DefinicionInteractorImpl implements IDefinicionInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public DefinicionInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Observable<List<ClaseTareo>> listarClaseTareo() {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarClasesTareo();
            case ModoTrabajo.BATCH:
                return local.listarClasesTareo().toObservable();
            default:
                return null;
        }
    }

    @Override
    public Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.nivelesTareo(idClaseTareo);
            case ModoTrabajo.BATCH:
                return local.nivelesTareo(idClaseTareo);
            default:
                return null;
        }
    }
}
