package com.dms.tareosoft.domain.tareo_editar_emp;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TareoEditarEmpInteractorImpl implements ITareoEditarEmpInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoEditarEmpInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                        @NonNull DataSourceRemote dataSourceRemote,
                                        @NonNull PreferenceManager preferenceManager){
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<List<DetalleTareo>> obtenerEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerDetalleTareo(codigoTareo, statusFinTareo);
            case ModoTrabajo.BATCH:
                return local.obtenerDetalleTareo(codigoTareo, statusFinTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaEmpleados(codigoTareo, statusFinTareo);
            case ModoTrabajo.BATCH:
                return local.obtenerListaEmpleados(codigoTareo, statusFinTareo);
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
    public Single<Tareo> obtenerTareo(String codigoTareo) {
        return local.consultarTareo(codigoTareo);
    }

    @Override
    public Single<Long> actualizarTareo(Tareo tareo) {
        return local.actualizarTareo(tareo);
    }

    @Override
    public Single<Long[]> registrarEmpleadoxsTareo(List<DetalleTareo> detalleTareos) {
        return local.agregarEmpleadosTareo(detalleTareos).doOnSuccess(longs ->
                local.actualizarEmpleados(detalleTareos));
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
}
