package com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class EmpleadosInteractorImpl implements IEmpleadosInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public EmpleadosInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                   @NonNull DataSourceRemote dataSourceRemote,
                                   @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<Long> finalizarTareoEmpleado(String codigoTareo, String codigoEmpleado,
                                               String fechaFinTareo, String horaFinTareo,
                                               Boolean refrigerio, String fechaIniRefrigerio,
                                               String fechaFinRefrigerio, Boolean flgUltimo,
                                               String horaRegistroSalida) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo revisar url
                return null;
            case ModoTrabajo.BATCH:
                return local.finalizarTareoEmpleado(codigoTareo, codigoEmpleado, fechaFinTareo,
                        horaFinTareo, refrigerio, fechaIniRefrigerio, fechaFinRefrigerio,
                        flgUltimo, horaRegistroSalida);
            default:
                return null;
        }
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
                return remote.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
            case ModoTrabajo.BATCH:
                return local.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
            default:
                return null;
        }
    }

    @Override
    public Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleados(String codTareo,
                                                                 @StatusFinDetalleTareo int status) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaEmpleadosFinalizar(codTareo,status);
            case ModoTrabajo.BATCH:
                return local.obtenerCodigoEmpleadosIniciados(codTareo, status);
            default:
                return null;
        }
    }

    @Override
    public Flowable<List<EmpleadoRow>> obtenerEmpleadosSinControl(String codTareo,
                                                                  @StatusIniDetalleTareo int estadoIniciado) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerEmpleadosSinControl(codTareo, estadoIniciado);
            case ModoTrabajo.BATCH:
                return local.obtenerEmpleadosSinControl(codTareo, estadoIniciado);
            default:
                return null;
        }
    }
}
