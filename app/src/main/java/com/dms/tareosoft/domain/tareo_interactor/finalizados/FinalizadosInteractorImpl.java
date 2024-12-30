package com.dms.tareosoft.domain.tareo_interactor.finalizados;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class FinalizadosInteractorImpl implements IFinalizadosInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public FinalizadosInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosFinalizados(int usuario) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaTareo(StatusTareo.TAREO_FINALIZADO, usuario,
                        StatusDescargaServidor.PENDIENTE);
            case ModoTrabajo.BATCH:
                return local.obtenerListaTareo(StatusTareo.TAREO_FINALIZADO, usuario,
                        StatusDescargaServidor.PENDIENTE);
            default:
                return null;
        }
    }

    @Override
    public Completable deleteTareo(String codigoTareo) {
        return local.deleteTareo(codigoTareo);
    }
}
