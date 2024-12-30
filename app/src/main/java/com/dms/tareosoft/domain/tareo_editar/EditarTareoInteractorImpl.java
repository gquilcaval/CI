package com.dms.tareosoft.domain.tareo_editar;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class EditarTareoInteractorImpl implements IEditarTareoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public EditarTareoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<Tareo> consultarTareo(String codigoTareo) {
        return local.consultarTareo(codigoTareo);
    }

    @Override
    public Single<Long> actualizarTareo(Tareo tareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.actualizarTareo(tareo);
            case ModoTrabajo.BATCH:
                return local.actualizarTareo(tareo);
            default:
                return null;
        }
    }

    @Override
    public Single<Long[]> registrarEmpleadoEnTareo(List<DetalleTareo> detalleTareos) {
        return local.agregarEmpleadosTareo(detalleTareos).doOnSuccess(longs ->
                local.actualizarEmpleados(detalleTareos));
    }

}
