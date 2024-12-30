package com.dms.tareosoft.domain.tareo_interactor.iniciados;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class IniciadosInteractorImpl implements IIniciadosInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public IniciadosInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                   @NonNull DataSourceRemote dataSourceRemote,
                                   @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(int estado, int usuario, String status) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaTareo(estado, usuario, status);
            case ModoTrabajo.BATCH:
                return local.obtenerListaTareo(estado, usuario, status);
            default:
                return null;
        }
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int estado, @StatusTareo int estado1,
                                                        int usuario, @StatusDescargaServidor String status) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaTareo(estado, usuario, status);
            case ModoTrabajo.BATCH:
                return local.obtenerListaTareo(estado, estado1, usuario, status);
            default:
                return null;
        }
    }

    @Override
    public Completable deleteTareo(String codigoTareo, HashMap<String, Integer> body) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.deleteTareo(codigoTareo, body);
            case ModoTrabajo.BATCH:
                return local.deleteTareo(codigoTareo);
            default:
                return null;
        }
    }
}