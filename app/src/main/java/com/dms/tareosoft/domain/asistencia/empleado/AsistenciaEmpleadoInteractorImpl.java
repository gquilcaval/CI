package com.dms.tareosoft.domain.asistencia.empleado;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AsistenciaEmpleadoInteractorImpl implements IAsistenciaEmpleadoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public AsistenciaEmpleadoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal, @NonNull DataSourceRemote dataSourceRemote, @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Observable<List<ClaseTareo>> listarClaseTareo() {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarClasesTareo();
            case ModoTrabajo.BATCH:
                return local.listarClasesTareo().toObservable();
            default:
                return null;
        }
    }

    @Override
    public Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.nivelesTareo(idClaseTareo);
            case ModoTrabajo.BATCH:
                return local.nivelesTareo(idClaseTareo);
            default:
                return null;
        }
    }

    @Override
    public Observable<List<Marcacion>> listarAsistencias() {
         return local.obtenerAsistencias().toObservable();
    }

    @Override
    public Observable<List<Marcacion>> listarAsistenciasHoy() {
        return local.obtenerAsistenciasHoy().toObservable();
    }

    @Override
    public Observable<List<Marcacion>> listarUltimaAsistenciaEmpleado(String codEmpleado) {
        return local.obtenerUltimaAsistenciaEmpleado(codEmpleado).toObservable();
    }

    @Override
    public Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarEmpleado(codEmpleado);
            case ModoTrabajo.BATCH:
                return local.validarEmpleado(codEmpleado);
            default:
                return null;
        }
    }

    @Override
    public Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                               @StatusFinDetalleTareo int statusDetalleTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
            case ModoTrabajo.BATCH:
                return local.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
            default:
                return null;
        }
    }

    @Override
    public Maybe<Long> registrarAsistencia(Marcacion nuevo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.registrarMarcacion(nuevo);
            case ModoTrabajo.BATCH:
                return local.registrarAsistencia(nuevo);
            default:
                return null;
        }
    }

    @Override
    public Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.validarEmpleadoPlanilla(codEmpleado, codPlanilla);
            case ModoTrabajo.BATCH:
                return local.validarEmpleadoPlanilla(codEmpleado, codPlanilla);
            default:
                return null;
        }
    }
}
