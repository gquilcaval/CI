package com.dms.tareosoft.domain.tareo_editar.editar_empleados;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class EditarEmpleadosInteractorImpl implements IEditarEmpleadosInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public EditarEmpleadosInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                         @NonNull DataSourceRemote dataSourceRemote,
                                         @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarEmpleadoPlanilla(codEmpleado, codPlanilla);
            case ModoTrabajo.BATCH:
                return local.validarEmpleadoPlanilla(codEmpleado, codPlanilla);
            default:
                return null;
        }
    }

    @Override
    public Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarEmpleado(codEmpleado);
            case ModoTrabajo.BATCH:
                return local.validarEmpleado(codEmpleado);
            default:
                return null;
        }
    }

    @Override
    public Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                               @StatusFinDetalleTareo int statusDetalleTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarExistenciaEmpleadoporDni(codEmpleado,statusDetalleTareo);
            case ModoTrabajo.BATCH:
                return local.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<List<DetalleTareo>> obtenerListaDetalleTareo(String codTareo,
                                                               @StatusFinDetalleTareo int statusFinTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerDetalleTareo(codTareo, statusFinTareo);
            case ModoTrabajo.BATCH:
                return local.obtenerDetalleTareo(codTareo, statusFinTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo,
                                                           @StatusFinDetalleTareo int statusFinTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaEmpleados(codigoTareo, statusFinTareo);
            case ModoTrabajo.BATCH:
                return local.obtenerListaEmpleados(codigoTareo, statusFinTareo);
            default:
                return null;
        }
    }
}
