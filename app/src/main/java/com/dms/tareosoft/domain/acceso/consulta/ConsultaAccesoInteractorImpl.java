package com.dms.tareosoft.domain.acceso.consulta;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.incidencia.IConsultaIncidenciaInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConsultaAccesoInteractorImpl implements IConsultaAccesoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ConsultaAccesoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                        @NonNull DataSourceRemote dataSourceRemote,
                                        @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<List<Acceso>> findAccesoByDate(String fch_ini, String fch_fin) {
        return local.obtenerAccesoByDate(fch_ini, fch_fin);
    }
}
