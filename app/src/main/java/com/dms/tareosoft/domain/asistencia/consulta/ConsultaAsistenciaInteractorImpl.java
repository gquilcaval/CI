package com.dms.tareosoft.domain.asistencia.consulta;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public class ConsultaAsistenciaInteractorImpl implements IConsultaAsistenciaInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public ConsultaAsistenciaInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                            @NonNull DataSourceRemote dataSourceRemote,
                                            @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Observable<List<Marcacion>> searchAsistenciasEmpleado(String query) {
        return local.searchAsistenciasEmpleado(query).toObservable();
    }
}
