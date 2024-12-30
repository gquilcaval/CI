package com.dms.tareosoft.domain;

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
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.local.dao.IncidenciaDao;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.models.RespuestaNumerica;
import com.dms.tareosoft.domain.models.RespuestaTareo;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public interface IDataSource {

    Maybe<UsuarioPerfil> userLogin(String username, String password);

    Maybe<List<TareoRow>> obtenerListaTareo(@StatusTareo int estado,
                                            int usuario, String estadoEnvio);

    Maybe<List<TareoRow>> obtenerListaTareo(@StatusTareo int estado, @StatusTareo int estado1,
                                            int usuario, String estadoEnvio);

    interface remote extends IDataSource {
        Single<RespuestaGeneral> validarConexion();

        Call<String> obtenerFechaHora();

        Observable<List<Usuario>> obtenerUsuarios();

        Observable<List<UnidadMedida>> obtenerUnidadMedida();

        Observable<List<Turno>> obtenerTurnos();

        Observable<List<ClaseTareo>> obtenerClaseTareo();

        Observable<List<NivelTareo>> obtenerNivelesTareo();

        Observable<List<ConceptoTareo>> obtenerConceptosTareo();

        Observable<List<Tareo>> obtenerTareos(int codigoUsuario);

        Single<List<AllTareoRow>> obtenerAllTareos(int codigoUsuario);

        Observable<List<DetalleTareo>> obtenerDetalleTareos(int codigoUsuario);

        Observable<List<Perfil>> obtenerPerfiles();

        Observable<List<Empleado>> obtenerEmpleados();

        //Descarga
        Observable<RespuestaTareo> enviarListaTareo(List<TareoPendiente> tareos);

        Observable<RespuestaNumerica> enviarListaControl(List<DetalleTareoControl> controles);

        Observable<RespuestaTareo> enviarListaResultados(List<ResultadoTareo> resultados);

        Maybe<Long> registrarTareo(Tareo tareo);

        Single<Long[]> agregarEmpleadosTareo(List<DetalleTareo> empleados);

        Single<Tareo> consultarTareo(String codTareos);

        Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel, int idPadre);

        Maybe<List<String>> enviarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareos);

        Maybe<Long> registrarMarcacion(Marcacion marcacion);

        Maybe<Long> registrarIncidencia(Incidencia incidencia);

        Maybe<ResponseRegistroAcceso> registrarAcceso(Acceso acceso);
    }

    interface local extends IDataSource {
        void insertarUsuarios(List<Usuario> lista);

        void insertarUnidadMedida(List<UnidadMedida> lista);

        void insertarTurnos(List<Turno> lista);

        void insertarConceptoTareo(List<ConceptoTareo> lista);

        void insertarClaseTareo(List<ClaseTareo> lista);

        void insertarTareos(List<Tareo> lista);

        void insertarDetalleTareos(List<DetalleTareo> lista);

        void insertarPerfil(List<Perfil> lista);

        void insertarEmpleados(List<Empleado> lista);

        void insertarNivelesTareo(List<NivelTareo> lista);

        void actualizarEmpleadosTareo(String codTareo, ArrayList<Integer> empleados);

        Maybe<List<TareoRow>> obtenerListaTareoDetalleFin(int estado, int usuario, String estadoEnvio);

        Single<List<UnidadMedida>> listarUnidadesMedida();

        Single<List<Turno>> listarTurnos();

        Single<List<ClaseTareo>> listarClasesTareo();

        Single<List<NivelTareo>> nivelesTareo(int fkConcepto1);

        Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel);

        Single<List<ConceptoTareo>> obtenerConceptosTareoLike(int idNivel, String search);

        Single<List<ConceptoTareo>> obtenerConceptosTareoLike(int idNivel, int idPadre, String search);

        Single<List<ConceptoTareo>> obtenerConceptosTareoLikeCod(int idNivel, String search);

        Single<List<ConceptoTareo>> obtenerConceptosTareoLikeCod(int idNivel, int idPadre, String search);

        //Tareo
        Maybe<EmpleadoControlRow> verificarEmpleadoTareo(String codEmpleado, String codTareo);

        Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);

        Maybe<Empleado> validarEmpleado(String codEmpleado);

        Maybe<Long> registrarEmpleado(Empleado nuevo);

        Maybe<Long> registrarTareo(Tareo nuevo);

        Maybe<Long> registrarAsistencia(Marcacion nuevo);

        Maybe<Long> agregarEmpleadoTareo(DetalleTareo nuevo);

        Single<Long[]> agregarEmpleadosTareo(List<DetalleTareo> lista);

        Single<Tareo> consultarTareo(String codigoTareo);

        Single<List<AllTareosWithResult>> obtenerTareoWithResult(List<String> listTareoEnd);

        void actualizarEmpleados(List<DetalleTareo> empleados);

        Single<ClaseTareo> consultarClaseTareo(int idClaseTareo);

        Single<UnidadMedida> consultarUnidadMedida(int idUnidad);

        Single<Turno> consultarTurno(int idTurno);

        Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

        Single<List<DetalleTareo>> obtenerDetalleTareo(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

        Single<Long> actualizarTareo(Tareo tareo);

        Single<Long> finalizarTareoEmpleado(String codTareo, String empleado, String fechaFinTareo, String horaFinTareo, Boolean refrigerio, String fechaIniRefrigerio, String fechaFinRefrigerio, Boolean ultimo, String horaRegistroSalida);

        Single<Long> finalizarTareos(ArrayList<String> listaCodTareos, String fechaFinTareo, String horaFinTareo, String fechaIniRefrigerio, String fechaFinRefrigerio);

        Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleadosFinalizados(String codTareo);

        Flowable<List<EmpleadoRow>> obtenerEmpleadosFinalizados(String codTareo);

        Flowable<List<EstadoEmpleadoRow>> obtenerEstadoEmpleados(String codTareo);

        Flowable<List<AllEmpleadosConsulta>> obtenerEmpleadosConsulta(String codTareo);

        Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleadosIniciados(String codTareo,
                                                                       @StatusFinDetalleTareo int status);

        Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel, int idPadre);

        Single<Long> finalizarTareoParaReubicar(List<String> listaCodDetalleTareo,
                                                List<String> listaCodTareos, String fechaFinTareo,
                                                String horaFinTareo,
                                                @StatusRefrigerio int statusRefrigerio,
                                                String fechaIniRefrigerio, String fechaFinRefrigerio,
                                                @StatusFinDetalleTareo int statusFinTareo,
                                                @StatusEmpleado int statusEmpleado,
                                                @StatusTareo int statusTareo);

        Single<Long> finalizarEmpleadoParaReubicar(List<String> listaCodDetalleTareo,
                                                   List<String> listaCodEmpleao,
                                                   @StatusRefrigerio int statusRefrigerio,
                                                   String fechaFinTareo, String horaFinTareo,
                                                   String horaIniRefrigerio, String horaFinRefrigerio,
                                                   @StatusFinDetalleTareo int estadoFinTareo,
                                                   @StatusEmpleado int statusEmpleado,
                                                   List<CantEmpleados> listaCantEmpleados);

        Single<Long> crearResultadoParaReubicarEmpleado(List<ResultadoTareo> resultadoTareo,
                                                        List<AllEmpleadoRow> cantProducida);

        Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codEmpleado, String codDetalleTareo);

        Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos);

        Single<List<AllTareoRow>> obtenerAllTareos(int codigoUsuario);

        Maybe<List<AllResultadoPorTareoRow>> obtenerListResultforTareo(String codTareo);

        Single<Long> guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo);

        Single<Long> guardarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareo);

        Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado, @StatusFinDetalleTareo int statusDetalleTareo);

        Maybe<List<TareoRow>> obtenerTareosIniciadosOnly(@StatusTareo int estado, int usuario,
                                                         String estadoEnvio, List<String> listCodTareos,
                                                         List<Integer> listCodClase);

        //ASISTENCIAS
        Single<List<Marcacion>> obtenerAsistencias();
        Single<List<Marcacion>> obtenerAsistenciasHoy();
        Maybe<List<Marcacion>> obtenerAsistenciasPendientes();

        Single<List<Marcacion>> obtenerUltimaAsistenciaEmpleado(String codEmpleado);


        Single<List<Marcacion>> searchAsistenciasEmpleado(String query);

        Maybe<Integer> updateEnvioMarcacion(int idMarcacion);



        // INCIDENCIA
        Maybe<Long> registrarIncidencia(Incidencia nuevo);
        Maybe<List<Incidencia>> obtenerIncidentesPendientesEnviar();
        Maybe<Integer> updateEnvioIncidencia(int idIncidencia);
        Single<List<Incidencia>> obtenerIncidentesByDate(String fch_ini, String fch_fin);

        // ACCESO
        Maybe<Long> registrarAcceso(Acceso nuevo);
        Maybe<List<Acceso>> obtenerAccesosPendientesEnviar();
        Maybe<Integer> updateEnvioAcceso(int id);
        Single<List<Acceso>> obtenerAccesoByDate(String fch_ini, String fch_fin);
    }
}
