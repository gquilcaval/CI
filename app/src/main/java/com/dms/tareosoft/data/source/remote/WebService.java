package com.dms.tareosoft.data.source.remote;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.annotacion.StatusTareo;
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

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {

    @GET(Urls.VALIDAR_CONEXION)
    Single<RespuestaGeneral> validarConexion();

    /* Sincronizar */
    @GET(Urls.DOWNLOAD_USUARIOS)
    Observable<List<Usuario>> obtenerUsuarios();

    @GET(Urls.DOWNLOAD_FECHA_HORA)
    Call<String> obtenerFechaHora();

    @GET(Urls.DOWNLOAD_UNIDAD_MEDIDA)
    Observable<List<UnidadMedida>> obtenerUnidadMedida();

    @GET(Urls.DOWNLOAD_TURNO)
    Observable<List<Turno>> obtenerTurnos();

    @GET(Urls.DOWNLOAD_NIVELES_TAREO)
    Observable<List<NivelTareo>> obtenerNivelesTareo();

    @GET(Urls.DOWNLOAD_CLASE_TAREO)
    Observable<List<ClaseTareo>> obtenerClaseTareo();

    @GET(Urls.DOWNLOAD_CONCEPTOS_TAREO)
    Observable<List<ConceptoTareo>> obtenerConceptosTareo();

    @GET(Urls.DOWNLOAD_PERFILES)
    Observable<List<Perfil>> obtenerPerfiles();

    @GET(Urls.DOWNLOAD_EMPLEADOS)
    Observable<List<Empleado>> obtenerEmpleados();

    @GET(Urls.DOWNLOAD_TAREO_XUSUARIO)
    Observable<List<Tareo>> obtenerTareos(@Query("idUsuario") int idUsuario);

    @GET(Urls.DOWNLOAD_TAREO_XUSUARIO)
    Single<List<AllTareoRow>> obtenerAllTareos(@Query("idUsuario") int idUsuario);

    @GET(Urls.DOWNLOAD_DETALLETAREO_XUSUARIO)
    Observable<List<DetalleTareo>> obtenerDetalleTareos(@Query("idUsuario") int idUsuario);

    /* Batch Descarga */
    @POST(Urls.UPLOAD_TAREOS)
    Observable<RespuestaTareo> enviarTareos(@Body List<TareoPendiente> body);

    @POST(Urls.UPLOAD_RESULTADOS)
    Observable<RespuestaTareo> enviarResultados(@Body List<ResultadoTareo> body);

    @POST(Urls.UPLOAD_RESULTADOS_TAREO)
    Maybe<List<String>> enviarResultadoPorTareo(@Body List<ResultadoPorTareo> resultadoPorTareos);

    @POST(Urls.UPLOAD_CONTROLES)
    Observable<RespuestaNumerica> enviarControles(@Body List<DetalleTareoControl> body);

    /* Seguridad */
    @POST(Urls.USUARIO_LOGIN)
    Maybe<UsuarioPerfil> userLogin(@Body BodyLogin body);

    /* Tareo
       0: Todos| 1:Iniciados|2:Finalizados
    */
    @GET(Urls.LISTAR_TAREOS)
    Maybe<List<TareoRow>> listaTareos(@Query("estadoTareo") @StatusTareo int estado);

    @GET(Urls.LISTAR_ALL_EMPLEADOS)
    Maybe<List<AllEmpleadoRow>> listAllEmpleados();

    //todo creados nuevos
    @GET(Urls.DOWNLOAD_UNIDAD_MEDIDA)
    Single<List<UnidadMedida>> listarUnidadesMedida();

    @GET(Urls.DOWNLOAD_TURNO)
    Single<List<Turno>> listarTurnos();

    @GET(Urls.DOWNLOAD_CLASE_TAREO)
    Observable<List<ClaseTareo>> listarClasesTareo();

    @GET(Urls.TAREO)
    Single<Tareo> consultarTareo(@Path("codTareo") String codTareo);

    @GET(Urls.DOWNLOAD_NIVELES_TAREO)
    Single<List<NivelTareo>> nivelesTareo(@Query("identificadorClase") int identificadorClase);

    @GET(Urls.DOWNLOAD_CONCEPTOS_X_NIVEL)
    Single<List<ConceptoTareo>> obtenerConceptosTareo(@Path("identificadorNivel") int identificadorNivel,
                                                      @Path("ordenNivel") int ordenNivel);

    @POST(Urls.GUARDAR_TAREO)
    Maybe<Long> registrarTareo(@Body Tareo tareo);

    @POST(Urls.GUARDAR_DETALLE_TAREO)
    Single<Long[]> agregarEmpleadosTareo(@Body List<DetalleTareo> empleados);

    //todo revisar variable

    @GET(Urls.CREAR_NEW_DETALE_TAREO)
    Single<Long> createNewDetalleTareo(@Body List<DetalleTareo> tareo);

    @GET(Urls.CLASE_TAREO)
    Single<ClaseTareo> consultarClaseTareo(@Path("codTareo") int codTareo);

    @GET(Urls.DOWNLOAD_CONCEPTOS_TAREO)
    Single<List<ConceptoTareo>> obtenerConceptosTareo(@Query("codTareo") int codTareo);

    @GET(Urls.VALIDAR_EMPLEADO_POR_PLANILLA)
    Maybe<Empleado> validarEmpleadoPlanilla(@Path("codPlanilla") String codPlanilla,
                                            @Path("codEmpleado") String codEmpleado);

    @GET(Urls.VALIDAR_EMPLEADO)
    Maybe<Empleado> validarEmpleado(@Path("codEmpleado") String codEmpleado);

    @GET(Urls.DETALLE_ID_EMPLEADO_FIN)
    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(@Path("codEmpleado") String codEmpleado,
                                                        @Path("status") @StatusFinDetalleTareo int statusFin);

    @GET(Urls.EMPLEADO_ID_TAREO_INI)
    Flowable<List<EmpleadoRow>> obtenerEmpleadosSinControl(@Path("codTareo") String codigoTareo,
                                                           @Path("status") @StatusIniDetalleTareo int status);

    @GET(Urls.DETALLE_ID_TAREO_FIN)
    Single<List<DetalleTareo>> obtenerDetalleTareo(@Path("codTareo") String codTareo,
                                                   @Path("status") @StatusFinDetalleTareo int status);

    @GET(Urls.EMPLEADO_ID_TAREO_FIN)
    Single<List<EmpleadoRow>> obtenerListaEmpleados(@Path("codTareo") String codigoTareo,
                                                    @Path("status") @StatusFinDetalleTareo int status);

    @PATCH(Urls.ACTUALIZAR_TAREO)
    Single<Long> actualizarTareo(@Path("codTareo") String codigoTareo,
                                 @Body Tareo tareo);

    @GET(Urls.EMPLEADO_ID_TAREO_FIN)
    Maybe<List<CodigoEmpleadoRow>> obtenerListaEmpleadosFinalizar(@Path("codTareo") String codigoTareo,
                                                                  @Path("status") @StatusFinDetalleTareo int status);

    @GET(Urls.CONSULTAR_TURNO)
    Single<Turno> consultarTurno(@Path("idTurno") int idTurno);

    @GET(Urls.TAREO)
    Single<UnidadMedida> consultarUnidadMedida(@Path("codTareo") int codTareo);

    @HTTP(method = "DELETE", path = Urls.DELETE_TAREO, hasBody = true)
    Completable delete(@Path("codigoTareo") String codTareo,
                       @Body HashMap<String, Integer> body);

    // Asistencia
    @POST(Urls.GUARDAR_MARCACION)
    Maybe<Long> registrarMarcacion(@Body Marcacion marcacion);

    // Incidencia
    @POST(Urls.GUARDAR_INCIDENCIA)
    Maybe<Long> registrarIncidencia(@Body Incidencia incidencia);

    // Acceso
    @POST(Urls.GUARDAR_ACCESO)
    Maybe<ResponseRegistroAcceso> registrarAcceso(@Body Acceso acceso);
}
