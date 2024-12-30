package com.dms.tareosoft.domain.sync;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Perfil;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.entities.Usuario;
import com.dms.tareosoft.data.models.ResponseRegistroAcceso;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.models.RespuestaNumerica;
import com.dms.tareosoft.domain.models.RespuestaTareo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public class SyncInteractorImpl implements ISyncInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferences;

    @Inject
    public SyncInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                              @NonNull DataSourceRemote dataSourceRemote,
                              @NonNull PreferenceManager preferences) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferences = preferences;
    }

    @Override
    public Single<RespuestaGeneral> validarConexion() {
        return remote.validarConexion();
    }

    @Override
    public Call<String> obtenerFechaHora() {
        return remote.obtenerFechaHora();
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
    public Maybe<List<TareoPendiente>> getTareos(@StatusDescargaServidor String statusServer) {
        return local.tareosPendientesEnvio(statusServer);
    }

    @Override
    public Maybe<List<TareoPendiente>> getTareos(@StatusTareo int status,
                                                 @StatusDescargaServidor String statusServer) {
        return local.tareosPendientesEnvio(status, statusServer);
    }

    @Override
    public Completable cambiarEstadoTareos(String flagInicial, String flagFinal) {
        return local.cambiarEstadoTareo(flagInicial, flagFinal);
    }

    @Override
    public Maybe<List<ResultadoPorTareo>> obtenerResultadoPorTareo(ArrayList<String> codigosTareo,
                                                                  @StatusDescargaServidor String statusServer) {
        return local.obtenerResultadoPorTareo(codigosTareo, statusServer);
    }

    @Override
    public Maybe<List<String>> enviarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareos) {
        return remote.enviarResultadoPorTareo(resultadoPorTareos);
    }

    @Override
    public Completable actualizarResultadoPorTareo(ArrayList<String> codigosTareo,
                                                                     @StatusDescargaServidor String statusServer) {
        return local.actualizarResultadoPorTareo(codigosTareo, statusServer);
    }

    @Override
    public Maybe<Long> enviarIncidencias(Incidencia incidencia) {
        return remote.registrarIncidencia(incidencia);
    }

    @Override
    public Maybe<ResponseRegistroAcceso> enviarAcceso(Acceso acceso) {
        return remote.registrarAcceso(acceso);
    }

    @Override
    public Maybe<List<Incidencia>> obtenerIncidenciasLocal() {
        return local.obtenerIncidentesPendientesEnviar();
    }

    @Override
    public Maybe<Integer> updateEnvioIncidencia(int idIncidencia) {
        return local.updateEnvioIncidencia(idIncidencia);
    }

    @Override
    public Maybe<List<Acceso>> obtenerAccesosLocal() {
        return local.obtenerAccesosPendientesEnviar();
    }

    @Override
    public Maybe<Integer> updateEnvioAcceso(int id) {
        return local.updateEnvioAcceso(id);
    }

    @Override
    public Maybe<Long> registrarAsistencia(Marcacion nuevo) {
        return remote.registrarMarcacion(nuevo);
    }

    @Override
    public Maybe<List<Marcacion>> obtenerMarcacionesLocal() {
        return local.obtenerAsistenciasPendientes();
    }

    @Override
    public Maybe<Integer> updateEnvioMarcacion(int idMarcacion) {
        return local.updateEnvioMarcacion(idMarcacion);
    }

    @Override
    public Maybe<List<DetalleTareoControl>> listaControles() {
        return local.controlesPendientes();
    }

    @Override
    public Observable<RespuestaTareo> descargarTareos(List<TareoPendiente> tareos) {
        return remote.enviarListaTareo(tareos);
    }

    @Override
    public Observable<RespuestaTareo> descargarResultados(List<ResultadoTareo> resultados) {
        return remote.enviarListaResultados(resultados);
    }

    @Override
    public Observable<RespuestaNumerica> descargarControles(List<DetalleTareoControl> controles) {
        return remote.enviarListaControl(controles);
    }

    @Override
    public Completable actualizarTareos(ArrayList<String> codigos, String flag) {
        return local.actualizarTareos(codigos, flag);
    }

    @Override
    public Completable actualizarResultados(ArrayList<String> codigos, String flag) {
        return local.actualizarResultados(codigos, flag);
    }

    @Override
    public Completable actualizarControles(ArrayList<Integer> codigos, String estado) {
        return local.actualizarControles(codigos, estado);
    }

    @Override
    public Maybe<List<ResultadoTareo>> obtenerResultadosPendientes(ArrayList<String> codigos,
                                                                   @StatusDescargaServidor String statusServer) {
        return local.getAllResultado(codigos, statusServer);
    }

    @Override
    public Observable<List<ConceptoTareo>> cargarConceptos() {
        return remote.obtenerConceptosTareo().doOnNext(lista -> local.insertarConceptoTareo(lista));
    }
}
