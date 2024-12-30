package com.dms.tareosoft.domain.tareo_consultar.tareo_consultar_details;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class TareoConsultaDetailsInteractorImpl implements ITareoConsultaDetailsInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoConsultaDetailsInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                              @NonNull DataSourceRemote dataSourceRemote,
                                              @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Flowable<List<AllEmpleadosConsulta>> obtenerEmpleadosConsulta(String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerEmpleadosConsulta(codTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<TareoRow> obtenerTareoDetail(String codTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerTareoDetail(codTareo);
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
