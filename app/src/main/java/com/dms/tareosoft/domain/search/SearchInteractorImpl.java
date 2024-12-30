package com.dms.tareosoft.domain.search;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class SearchInteractorImpl implements ISearchInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public SearchInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                @NonNull DataSourceRemote dataSourceRemote,
                                @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    /*@Override
    public Single<List<ConceptoTareo>> listarConceptosTareo(int idNivel) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerConceptosTareo(idNivel); //remote.obtenerClaseTareo();
            case ModoTrabajo.BATCH:
                return local.obtenerConceptosTareo(idNivel);
            default:
                return null;
        }
    }*/

    @Override
    public Single<List<ConceptoTareo>> listarConceptosTareo(int idNivel, int idPadre) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerConceptosTareo(idNivel, idPadre); //remote.obtenerClaseTareo();
            case ModoTrabajo.BATCH:
                return local.obtenerConceptosTareo(idNivel, idPadre);
            default:
                return null;
        }
    }

    /*@Override
    public Single<List<ConceptoTareo>> listarConceptosTareoLike(int idNivel, String search) {
        if (preferenceManager.getModoTrabajo() == ModoTrabajo.LINEA) {
            return local.obtenerConceptosTareoLike(idNivel, search); //remote.obtenerClaseTareo();
        } else {
            return local.obtenerConceptosTareoLike(idNivel, search);
        }
    }*/

    @Override
    public Single<List<ConceptoTareo>> listarConceptosTareoLike(int idNivel, int idPadre, String search) {
        if (preferenceManager.getModoTrabajo() == ModoTrabajo.LINEA) {
            return local.obtenerConceptosTareoLike(idNivel, idPadre, search); //remote.obtenerClaseTareo();
        } else {
            return local.obtenerConceptosTareoLike(idNivel, idPadre, search);
        }
    }

    /*@Override
    public Single<List<ConceptoTareo>> listarConceptosTareoLikeCod(int idNivel, String search) {
        if (preferenceManager.getModoTrabajo() == ModoTrabajo.LINEA) {
            return local.obtenerConceptosTareoLikeCod(idNivel, search); //remote.obtenerClaseTareo();
        } else {
            return local.obtenerConceptosTareoLikeCod(idNivel, search);
        }
    }*/

    @Override
    public Single<List<ConceptoTareo>> listarConceptosTareoLikeCod(int idNivel, int idPadre, String search) {
        if (preferenceManager.getModoTrabajo() == ModoTrabajo.LINEA) {
            return local.obtenerConceptosTareoLikeCod(idNivel, idPadre, search); //remote.obtenerClaseTareo();
        } else {
            return local.obtenerConceptosTareoLikeCod(idNivel, idPadre, search);
        }
    }

}
