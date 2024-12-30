package com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class ReubicarTipoEmpleadoCantidadInteractorImpl
        implements IReubicarTipoEmpleadoCantidadInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ReubicarTipoEmpleadoCantidadInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                                      @NonNull DataSourceRemote dataSourceRemote,
                                                      @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codTareo, String codEmpleado) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.validarResultadoEmpleadoParaReubicar(codTareo, codEmpleado);
            default:
                return null;
        }
    }

    @Override
    public Single<List<AllTareosWithResult>> obtenerTareoWithResult(List<String> listTareoEnd) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerTareoWithResult(listTareoEnd);
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
}
