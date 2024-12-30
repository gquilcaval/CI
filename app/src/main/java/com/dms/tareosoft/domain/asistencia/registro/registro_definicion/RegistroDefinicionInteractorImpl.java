package com.dms.tareosoft.domain.asistencia.registro.registro_definicion;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion.IDefinicionInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RegistroDefinicionInteractorImpl implements IRegistroDefinicionInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public RegistroDefinicionInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
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

    @Override
    public Observable<List<Marcacion>> listarAsistencias() {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return Observable.empty();
            case ModoTrabajo.BATCH:
                return local.obtenerAsistencias().toObservable();
            default:
                return null;
        }
    }
}
