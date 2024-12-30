package com.dms.tareosoft.domain.tareo_editar.editar_definicion;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class EditarDefinicionInteractorImpl implements IEditarDefinicionInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public EditarDefinicionInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
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
    public Single<List<ConceptoTareo>> listarConceptosTareo(int idNivel) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerConceptosTareo(idNivel);
            case ModoTrabajo.BATCH:
                return local.obtenerConceptosTareo(idNivel);
            default:
                return null;
        }
    }

    @Override
    public Single<ClaseTareo> obtenerClaseTareo(int idClaseTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.consultarClaseTareo(idClaseTareo);
            case ModoTrabajo.BATCH:
                return local.consultarClaseTareo(idClaseTareo);
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
