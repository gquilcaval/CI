package com.dms.tareosoft.domain.tareo_editar.editar_opciones;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import javax.inject.Inject;

import io.reactivex.Single;

public class EditarOpcionesInteractorImpl implements IEditarOpcionesInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public EditarOpcionesInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<UnidadMedida> obtenerUnidadMedida(int idUnidad) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.consultarUnidadMedida(idUnidad);
            case ModoTrabajo.BATCH:
                return local.consultarUnidadMedida(idUnidad);
            default:
                return null;
        }
    }

    @Override
    public Single<Turno> obtenerTurno(int idTurno) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.consultarTurno(idTurno);
            case ModoTrabajo.BATCH:
                return local.consultarTurno(idTurno);
            default:
                return null;
        }
    }

    @Override
    public Single<Tareo> obtenerTareo(String codigoTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.consultarTareo(codigoTareo);
            case ModoTrabajo.BATCH:
                return local.consultarTareo(codigoTareo);
            default:
                return null;
        }
    }
}