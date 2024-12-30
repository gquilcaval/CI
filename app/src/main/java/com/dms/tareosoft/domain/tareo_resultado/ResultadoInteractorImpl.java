package com.dms.tareosoft.domain.tareo_resultado;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.ResultadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ResultadoInteractorImpl implements IResultadoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ResultadoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                   @NonNull DataSourceRemote dataSourceRemote,
                                   @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Flowable<List<ResultadoRow>> obtenerResultadoEmpleados(String codigoTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerResultadoEmpleados(codigoTareo);
            default:
                return null;
        }
    }

    @Override
    public Completable guardarResultadoEmpleado(ResultadoTareo nuevo, String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.guardarResultadoEmpleado(nuevo, codTareo);
            default:
                return null;
        }
    }

    @Override
    public Completable modificarResultadoEmpleado(ResultadoTareo nuevo, String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.modificarResultadoEmpleado(nuevo, codTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<EmpleadoResultadoRow> validarEmpleado(String codigoTareo, String codEmpleado) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.validarEmpleadoTareo(codigoTareo, codEmpleado);
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
