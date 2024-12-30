package com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class ReubicarTipoTareoCantidadInteractorImpl
        implements IReubicarTipoTareoCantidadInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ReubicarTipoTareoCantidadInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
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
}
