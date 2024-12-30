package com.dms.tareosoft.domain.tareo_resultado.tareo_resultado_por_tareo;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class ResultadoPorTareoInteractorImpl implements IResultadoPorTareoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ResultadoPorTareoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                           @NonNull DataSourceRemote dataSourceRemote,
                                           @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<AllResultadoPorTareoRow>> obtenerListResultforTareo(String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerListResultforTareo(codTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<Long> guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.guardarResultadoPorTareo(resultadoPorTareo);
            default:
                return null;
        }
    }

    @Override
    public Completable liquidarTareo(String codigoTareo, String fechaModificacion, int usuario) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.liquidarTareo(codigoTareo, fechaModificacion, usuario);
            default:
                return null;
        }
    }
}
