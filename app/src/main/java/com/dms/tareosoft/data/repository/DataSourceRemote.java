package com.dms.tareosoft.data.repository;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
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
import com.dms.tareosoft.data.models.UsuarioPerfil;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.remote.WebService;
import com.dms.tareosoft.domain.IDataSource;
import com.dms.tareosoft.domain.models.BodyLogin;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.models.RespuestaNumerica;
import com.dms.tareosoft.domain.models.RespuestaTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public class DataSourceRemote implements IDataSource.remote {

    private WebService api;

    @Inject
    public DataSourceRemote(WebService api) {
        this.api = api;
    }

    /*: Seguridad*/
    @Override
    public Maybe<UsuarioPerfil> userLogin(String username, String password) {
        return api.userLogin(new BodyLogin(username, password));
    }

    /*: Maestros */
    @Override
    public Single<RespuestaGeneral> validarConexion() {
        return api.validarConexion();
    }

    @Override
    public Call<String> obtenerFechaHora() {
        return api.obtenerFechaHora();
    }

    @Override
    public Observable<List<Usuario>> obtenerUsuarios() {
        return api.obtenerUsuarios();
    }

    @Override
    public Observable<List<UnidadMedida>> obtenerUnidadMedida() {
        return api.obtenerUnidadMedida();
    }

    @Override
    public Observable<List<Turno>> obtenerTurnos() {
        return api.obtenerTurnos();
    }

    @Override
    public Observable<List<ClaseTareo>> obtenerClaseTareo() {
        return api.obtenerClaseTareo();
    }

    @Override
    public Observable<List<NivelTareo>> obtenerNivelesTareo() {
        return api.obtenerNivelesTareo();
    }

    @Override
    public Observable<List<ConceptoTareo>> obtenerConceptosTareo() {
        return api.obtenerConceptosTareo();
    }

    @Override
    public Observable<List<Tareo>> obtenerTareos(int codigoUsuario) {
        return api.obtenerTareos(codigoUsuario);
    }

    @Override
    public Single<List<AllTareoRow>> obtenerAllTareos(int codigoUsuario) {
        return api.obtenerAllTareos(codigoUsuario);
    }

    @Override
    public Observable<List<DetalleTareo>> obtenerDetalleTareos(int codigoUsuario) {
        Log.d("SETTINGPRESENTER", " " +api.obtenerDetalleTareos(codigoUsuario));
        return api.obtenerDetalleTareos(codigoUsuario);
    }

    @Override
    public Observable<List<Perfil>> obtenerPerfiles() {
        return api.obtenerPerfiles();
    }

    @Override
    public Observable<List<Empleado>> obtenerEmpleados() {
        return api.obtenerEmpleados();
    }

    @Override
    public Maybe<List<String>> enviarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareos) {
        return api.enviarResultadoPorTareo(resultadoPorTareos);
    }

    @Override
    public Maybe<Long> registrarMarcacion(Marcacion marcacion) {
        return api.registrarMarcacion(marcacion);
    }

    @Override
    public Maybe<Long> registrarIncidencia(Incidencia incidencia) {
        return api.registrarIncidencia(incidencia);
    }

    @Override
    public Maybe<ResponseRegistroAcceso> registrarAcceso(Acceso acceso) {
        return api.registrarAcceso(acceso);
    }

    /*: Tareo*/
    @Override
    public Maybe<List<TareoRow>> obtenerListaTareo(int estado, int usuario, String estadoEnvio) {
        return api.listaTareos(estado);
    }

    @Override
    public Maybe<List<TareoRow>> obtenerListaTareo(int estado, int estado1, int usuario, String estadoEnvio) {
        return api.listaTareos(estado);
    }

    /*Descarga*/
    @Override
    public Observable<RespuestaTareo> enviarListaTareo(List<TareoPendiente> tareos) {
        return api.enviarTareos(tareos);
    }

    @Override
    public Observable<RespuestaTareo> enviarListaResultados(List<ResultadoTareo> resultados) {
        return api.enviarResultados(resultados);
    }

    @Override
    public Maybe<Long> registrarTareo(Tareo tareo) {
        return api.registrarTareo(tareo);
    }

    @Override
    public Single<Long[]> agregarEmpleadosTareo(List<DetalleTareo> empleados) {
        return api.agregarEmpleadosTareo(empleados);
    }

    @Override
    public Observable<RespuestaNumerica> enviarListaControl(List<DetalleTareoControl> controles) {
        return api.enviarControles(controles);
    }

    public Maybe<List<AllEmpleadoRow>> viewAllEmpleados() {
        return api.listAllEmpleados();
    }

    public Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos) {
        return api.createNewDetalleTareo(detalleTareos);
    }

    public Single<Tareo> consultarTareo(String codTareos) {
        return api.consultarTareo(codTareos);
    }

    public Single<ClaseTareo> consultarClaseTareo(int idClaseTareo) {
        return api.consultarClaseTareo(idClaseTareo);
    }

    public Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel) {
        return api.obtenerConceptosTareo(idNivel);
    }

    public Single<List<NivelTareo>> nivelesTareo(int idClaseTareo) {
        return api.nivelesTareo(idClaseTareo);
    }

    public Single<List<DetalleTareo>> obtenerDetalleTareo(String codTareo,
                                                          @StatusFinDetalleTareo int statusFinTareo) {
        return api.obtenerDetalleTareo(codTareo, statusFinTareo);
    }

    public Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla) {
        return api.validarEmpleadoPlanilla(codEmpleado, codPlanilla);
    }

    public Maybe<Empleado> validarEmpleado(String codEmpleado) {
        return api.validarEmpleado(codEmpleado);
    }

    public Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                               @StatusFinDetalleTareo int statusFin) {
        return api.validarExistenciaEmpleadoporDni(codEmpleado, statusFin);
    }

    public Single<Long> actualizarTareo(Tareo tareo) {
        return api.actualizarTareo(tareo.getCodTareo(), tareo);
    }

    public Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo,
                                                           @StatusFinDetalleTareo int status) {
        return api.obtenerListaEmpleados(codigoTareo, status);
    }

    public Maybe<List<CodigoEmpleadoRow>> obtenerListaEmpleadosFinalizar(String codigoTareo,
                                                                         @StatusFinDetalleTareo int status) {
        return api.obtenerListaEmpleadosFinalizar(codigoTareo, status);
    }

    public Flowable<List<EmpleadoRow>> obtenerEmpleadosSinControl(String codigoTareo,
                                                                  @StatusIniDetalleTareo int status) {
        return api.obtenerEmpleadosSinControl(codigoTareo, status);
    }

    //todo nuevos
    public Single<List<Turno>> listarTurnos() {
        return api.listarTurnos();
    }

    public Single<List<UnidadMedida>> listarUnidadesMedida() {
        return api.listarUnidadesMedida();
    }

    public Observable<List<ClaseTareo>> listarClasesTareo() {
        return api.listarClasesTareo();
    }

    public Single<Turno> consultarTurno(int idTurno) {
        return api.consultarTurno(idTurno);
    }

    public Single<UnidadMedida> consultarUnidadMedida(int idUnidad) {
        return api.consultarUnidadMedida(idUnidad);
    }

    public Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel, int idPadre) {
        return api.obtenerConceptosTareo(idNivel, idPadre);
    }

    public Completable deleteTareo(String codigoTareo, HashMap<String, Integer> body) {
        return api.delete(codigoTareo, body);
    }
}