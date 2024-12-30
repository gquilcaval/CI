package com.dms.tareosoft.domain.incidencia;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.asistencia.consulta.IConsultaAsistenciaInteractor;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ConsultaIncidenciaInteractorImpl implements IConsultaIncidenciaInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ConsultaIncidenciaInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                            @NonNull DataSourceRemote dataSourceRemote,
                                            @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Single<List<Incidencia>> findIncidenciaByDate(String fch_ini, String fch_fin) {
        return local.obtenerIncidentesByDate(fch_ini, fch_fin);
    }
}
