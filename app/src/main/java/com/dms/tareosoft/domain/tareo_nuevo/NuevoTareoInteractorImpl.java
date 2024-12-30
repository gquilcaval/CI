package com.dms.tareosoft.domain.tareo_nuevo;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class NuevoTareoInteractorImpl implements INuevoTareoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public NuevoTareoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                    @NonNull DataSourceRemote dataSourceRemote,
                                    @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }


    /*@Override
    public Maybe<Long> registrarEmpleado(Empleado nuevo) {
        return local.registrarEmpleado(nuevo);
    }*/

    @Override
    public Maybe<Long> registrarTareo(Tareo nuevo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.registrarTareo(nuevo);
            case ModoTrabajo.BATCH:
                return local.registrarTareo(nuevo);
            default:
                return null;
        }
    }

    @Override
    public Single<Long[]> registrarEmpleadosTareo(List<DetalleTareo> empleados) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.agregarEmpleadosTareo(empleados);
            case ModoTrabajo.BATCH:
                return local.agregarEmpleadosTareo(empleados).doOnSuccess(longs -> local.actualizarEmpleados(empleados));
            default:
                return null;
        }
    }

}
