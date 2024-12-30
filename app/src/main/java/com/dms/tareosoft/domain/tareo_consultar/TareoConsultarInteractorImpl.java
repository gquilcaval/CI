package com.dms.tareosoft.domain.tareo_consultar;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.AllTareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class TareoConsultarInteractorImpl implements ITareoConsultarInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoConsultarInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<List<AllTareoRow>> obtenerAllTareos(int usuario) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerAllTareos(usuario);
            default:
                return null;
        }
    }
}
