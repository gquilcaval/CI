package com.dms.tareosoft.domain.settings_interactor;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Perfil;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.entities.Usuario;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public class SettingsInteractorImpl implements ISettingsInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferences;

    @Inject
    public SettingsInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                  @NonNull DataSourceRemote dataSourceRemote,
                                  @NonNull PreferenceManager preferences) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferences = preferences;
    }

    @Override
    public Observable<List<Usuario>> cargarUsuarios() {
        return remote.obtenerUsuarios().doOnNext(users -> local.insertarUsuarios(users));
    }

    @Override
    public Observable<List<UnidadMedida>> cargarUnidadMedida() {
        return remote.obtenerUnidadMedida().doOnNext(lista -> local.insertarUnidadMedida(lista));
    }

    @Override
    public Observable<List<Turno>> cargarTurnos() {
        return remote.obtenerTurnos().doOnNext(lista -> local.insertarTurnos(lista));
    }

    @Override
    public Observable<List<ClaseTareo>> cargarClases() {
        return remote.obtenerClaseTareo().doOnNext(lista -> local.insertarClaseTareo(lista));
    }

    @Override
    public Observable<List<Perfil>> cargarPerfiles() {
        return remote.obtenerPerfiles().doOnNext(lista -> local.insertarPerfil(lista));
    }

    @Override
    public Observable<List<NivelTareo>> cargarNiveles() {
        return remote.obtenerNivelesTareo().doOnNext(lista -> local.insertarNivelesTareo(lista));
    }

    @Override
    public Observable<List<Tareo>> cargarTareosAbiertos(int usuario) {
        return remote.obtenerTareos(usuario).doOnNext(lista -> local.insertarTareos(lista));
    }

    @Override
    public Observable<List<DetalleTareo>> cargarDetalleTareosAbiertos(int usuario) {
        return remote.obtenerDetalleTareos(usuario).doOnNext(local::insertarDetalleTareos);
    }

    @Override
    public Observable<List<Empleado>> cargarEmpleados() {
        return remote.obtenerEmpleados().doOnNext(lista -> local.insertarEmpleados(lista));
    }

    @Override
    public Observable<List<ConceptoTareo>> cargarConceptos() {
        return remote.obtenerConceptosTareo().doOnNext(lista -> local.insertarConceptoTareo(lista));
    }

    @Override
    public Single<List<UnidadMedida>> listarUnidadMedidas() {
        switch (preferences.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarUnidadesMedida();
            case ModoTrabajo.BATCH:
                return local.listarUnidadesMedida();
            default:
                return null;
        }
    }

    @Override
    public Single<List<Turno>> listarTurnos() {
        switch (preferences.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarTurnos();
            case ModoTrabajo.BATCH:
                return local.listarTurnos();
            default:
                return null;
        }
    }

    @Override
    public Observable<List<ClaseTareo>> listarClaseTareo() {
        switch (preferences.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.listarClasesTareo();
            case ModoTrabajo.BATCH:
                return local.listarClasesTareo().toObservable();
            default:
                return null;
        }
    }

    @Override
    public Single<RespuestaGeneral> validarConexion() {
        return remote.validarConexion();
    }

    @Override
    public Call<String> obtenerFechaHora() {
        return remote.obtenerFechaHora();
    }

}
