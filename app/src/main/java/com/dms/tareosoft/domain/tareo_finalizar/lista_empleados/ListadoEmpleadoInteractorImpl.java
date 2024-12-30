package com.dms.tareosoft.domain.tareo_finalizar.lista_empleados;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class ListadoEmpleadoInteractorImpl implements IListadoEmpleadoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ListadoEmpleadoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Flowable<List<EstadoEmpleadoRow>> obtenerEmpleados(String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerEstadoEmpleados(codTareo);
            default:
                return null;
        }
    }
}
