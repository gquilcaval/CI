package com.dms.tareosoft.domain.tareo_control;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class ControlTareoInteractorImpl implements IControlTareoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ControlTareoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Completable guardarControl(DetalleTareoControl nuevo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return local.guardarControl(nuevo);
            case ModoTrabajo.BATCH:
                return local.guardarControl(nuevo);
            default:
                return null;
        }
    }

    @Override
    public Flowable<List<EmpleadoRow>> obtenerControlEmpleados(String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerControlEmpleados(codTareo);
            default:
                return null;
        }
    }

    @Override
    public Maybe<EmpleadoControlRow> validarEmpleadoTareo(String codEmpleado, String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.verificarEmpleadoTareo(codEmpleado, codTareo);
            default:
                return null;
        }
    }


}
