package com.dms.tareosoft.domain.sync;

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
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.models.RespuestaNumerica;
import com.dms.tareosoft.domain.models.RespuestaTareo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public interface ISyncInteractor {
    Single<RespuestaGeneral> validarConexion();

    Call<String> obtenerFechaHora();

    /*Batch*/
    Observable<List<Usuario>> cargarUsuarios();

    Observable<List<Perfil>> cargarPerfiles();

    Observable<List<NivelTareo>> cargarNiveles();

    Observable<List<ClaseTareo>> cargarClases();

    Observable<List<ConceptoTareo>> cargarConceptos();

    Observable<List<Tareo>> cargarTareosAbiertos(int usuario);

    Observable<List<DetalleTareo>> cargarDetalleTareosAbiertos(int usuario);

    Observable<List<UnidadMedida>> cargarUnidadMedida();

    Observable<List<Turno>> cargarTurnos();

    Observable<List<Empleado>> cargarEmpleados();

    Observable<RespuestaTareo> descargarTareos(List<TareoPendiente> tareos);

    /**/

    Maybe<List<DetalleTareoControl>> listaControles();

    Observable<RespuestaTareo> descargarResultados(List<ResultadoTareo> solicitud);

    Observable<RespuestaNumerica> descargarControles(List<DetalleTareoControl> controles);

    Completable actualizarTareos(ArrayList<String> codigos, String flag);

    Completable actualizarResultados(ArrayList<String> codigos, String flag);

    Completable actualizarControles(ArrayList<Integer> datos, String estado);

    //Obtener resultados de los Tareos Liquidados/Finalizados
    Maybe<List<ResultadoTareo>> obtenerResultadosPendientes(ArrayList<String> codigos,
                                                            @StatusDescargaServidor String statusServer);

    Maybe<List<TareoPendiente>> getTareos(@StatusDescargaServidor String statusServer);

    Maybe<List<TareoPendiente>> getTareos(@StatusTareo int status,
                                          @StatusDescargaServidor String estadoEnvio);

    Completable cambiarEstadoTareos(@StatusDescargaServidor String enviado,
                                    @StatusDescargaServidor String backup);

    Maybe<List<ResultadoPorTareo>> obtenerResultadoPorTareo(ArrayList<String> codigosTareo,
                                                            @StatusDescargaServidor String statusServer);

    Maybe<List<String>> enviarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareos);

    Completable actualizarResultadoPorTareo(ArrayList<String> codigosTareo,
                                            @StatusDescargaServidor String statusServer);

    Maybe<Long> registrarAsistencia(Marcacion nuevo);

    Maybe<List<Marcacion>> obtenerMarcacionesLocal();

    Maybe<Integer> updateEnvioMarcacion(int idMarcacion);

    Maybe<Long> enviarIncidencias(Incidencia incidencia);

    Maybe<ResponseRegistroAcceso> enviarAcceso(Acceso acceso);

    Maybe<List<Incidencia>> obtenerIncidenciasLocal();

    Maybe<Integer> updateEnvioIncidencia(int idIncidencia);

    Maybe<List<Acceso>> obtenerAccesosLocal();

    Maybe<Integer> updateEnvioAcceso(int id);

}