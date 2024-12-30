package com.dms.tareosoft.data.repository;

import android.util.Base64;

import androidx.room.Transaction;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
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
import com.dms.tareosoft.data.models.UsuarioPerfil;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.local.DbTareo;
import com.dms.tareosoft.data.source.local.dao.AccesoDao;
import com.dms.tareosoft.data.source.local.dao.ClaseTareoDao;
import com.dms.tareosoft.data.source.local.dao.ConceptosTareoDao;
import com.dms.tareosoft.data.source.local.dao.DetalleTareoControlDao;
import com.dms.tareosoft.data.source.local.dao.DetalleTareoDao;
import com.dms.tareosoft.data.source.local.dao.EmpleadosDao;
import com.dms.tareosoft.data.source.local.dao.IncidenciaDao;
import com.dms.tareosoft.data.source.local.dao.MarcacionDao;
import com.dms.tareosoft.data.source.local.dao.NivelesTareoDao;
import com.dms.tareosoft.data.source.local.dao.PerfilDao;
import com.dms.tareosoft.data.source.local.dao.ResultadoPorTareoDao;
import com.dms.tareosoft.data.source.local.dao.ResultadoTareoDao;
import com.dms.tareosoft.data.source.local.dao.TareoDao;
import com.dms.tareosoft.data.source.local.dao.TurnoDao;
import com.dms.tareosoft.data.source.local.dao.UnidadMedidaDao;
import com.dms.tareosoft.data.source.local.dao.UsuarioDao;
import com.dms.tareosoft.domain.IDataSource;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.presentation.models.AllEmpleadoConsultaRow;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.presentation.models.ResultadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class DataSourceLocal implements IDataSource.local {
    private DbTareo database;

    private UsuarioDao daoUsuario;
    private UnidadMedidaDao daoUnidadMedida;
    private TurnoDao daoTurno;
    private ClaseTareoDao daoClaseTareo;
    private NivelesTareoDao daoNivelesTareo;
    private ConceptosTareoDao daoConcepto;
    private TareoDao daoTareo;
    private DetalleTareoDao daoDetalleTareo;
    private ResultadoTareoDao daoResultadoTareo;
    private ResultadoPorTareoDao daoResultadoPorTareo;
    private PerfilDao daoPerfil;
    private EmpleadosDao daoEmpleado;
    private DetalleTareoControlDao daoDetalleTareoControl;
    private MarcacionDao daoMarcacion;
    private IncidenciaDao daoIncidencia;
    private AccesoDao daoAcceso;

    private final ThreadPoolExecutor threadPoolExecute = new ThreadPoolExecutor(4,
            8, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(30));

    @Inject
    public DataSourceLocal(DbTareo appDatabase) {
        this.database = appDatabase;

        daoUsuario = database.usuarioDao();
        daoUnidadMedida = database.unidadMedidaDao();
        daoTurno = database.turnoDao();
        daoClaseTareo = database.claseTareoDao();
        daoNivelesTareo = database.nivelesTareoDao();
        daoConcepto = database.conceptosTareoDao();
        daoTareo = database.tareoDao();
        daoDetalleTareo = database.detalleTareoDao();
        daoPerfil = database.perfilDao();
        daoEmpleado = database.empleadosDao();
        daoResultadoTareo = database.resultadoTareoDao();
        daoResultadoPorTareo = database.resultadoPorTareoDao();
        daoDetalleTareoControl = database.detalleTareoControlDao();
        daoMarcacion = database.marcacionDao();
        daoIncidencia = database.incidenciaDao();
        daoAcceso = database.accesoDao();
    }

    @Override
    public Maybe<UsuarioPerfil> userLogin(String username, String password) {
        byte[] data = password.getBytes(Charset.forName("UTF-8"));
        // byte[] data = password.getBytes(StandardCharsets.UTF_16LE);
        String passwordEncode = Base64.encodeToString(data, Base64.NO_WRAP);
        return daoUsuario.validUser(username, passwordEncode);
    }

    @Override
    public Maybe<List<TareoRow>> obtenerListaTareo(int estado, int usuario, String estadoEnvio) {
        return daoTareo.listaTareos(estado, usuario, estadoEnvio);
    }

    @Override
    public Maybe<List<TareoRow>> obtenerListaTareo(@StatusTareo int estado, @StatusTareo int estado1,
                                                   int usuario, String estadoEnvio) {
        return daoTareo.listaTareos(estado, estado1, usuario, estadoEnvio);
    }

    @Override
    public Maybe<List<TareoRow>> obtenerListaTareoDetalleFin(@StatusTareo int estado, int usuario,
                                                             @StatusDescargaServidor String estadoEnvio) {
        return daoTareo.listaTareosDetalleFin(estado, usuario, estadoEnvio);
    }

    @Override
    public Single<List<UnidadMedida>> listarUnidadesMedida() {
        return daoUnidadMedida.lista();
    }

    @Override
    public Single<List<Turno>> listarTurnos() {
        return daoTurno.lista();
    }

    @Override
    public Single<List<ClaseTareo>> listarClasesTareo() {
        return daoClaseTareo.lista();//.toObservable();
    }

    @Override
    public Single<List<NivelTareo>> nivelesTareo(int idClaseTareo) {
        return daoNivelesTareo.lista(idClaseTareo);
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel) {
        return daoConcepto.lista(idNivel);
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareo(int idNivel, int idPadre) {
        return daoConcepto.listaxPadre(idNivel, idPadre);
    }

    @Override
    public Single<Long> finalizarTareoParaReubicar(List<String> listaCodDetalleTareo,
                                                   List<String> listaCodTareos, String fechaFinTareo,
                                                   String horaFinTareo,
                                                   @StatusRefrigerio int statusRefrigerio,
                                                   String fechaIniRefrigerio, String fechaFinRefrigerio,
                                                   @StatusFinDetalleTareo int statusFinTareo,
                                                   @StatusEmpleado int statusEmpleado,
                                                   @StatusTareo int statusTareo) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.finalizarDetalleTareosParaReubicar(listaCodDetalleTareo, statusRefrigerio,
                        fechaFinTareo + " " + horaFinTareo, fechaFinTareo,
                        horaFinTareo, fechaIniRefrigerio, fechaFinRefrigerio, statusFinTareo);
                daoDetalleTareo.deleteDetalleTareoById(listaCodDetalleTareo);
                daoEmpleado.liberarEmpleadosTareos(listaCodTareos, statusEmpleado);
                daoTareo.finalizarTareos(listaCodTareos, fechaFinTareo, horaFinTareo, statusTareo);
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los tareos"));
            }
        });
    }

    @Override
    public Single<Long> finalizarEmpleadoParaReubicar(List<String> listaCodDetalleTareo,
                                                      List<String> listaCodEmpleado,
                                                      @StatusRefrigerio int statusRefrigerio,
                                                      String fechaFinTareo, String horaFinTareo,
                                                      String horaIniRefrigerio, String horaFinRefrigerio,
                                                      @StatusFinDetalleTareo int estadoFinTareo,
                                                      @StatusEmpleado int statusEmpleado,
                                                      List<CantEmpleados> listCantEmpleados) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.finalizarDetalleTareosParaReubicar(listaCodDetalleTareo, statusRefrigerio,
                        fechaFinTareo + " " + horaFinTareo, fechaFinTareo,
                        horaFinTareo, horaIniRefrigerio, horaFinRefrigerio, estadoFinTareo);
                daoDetalleTareo.deleteDetalleTareoById(listaCodDetalleTareo);
                daoEmpleado.liberarEmpleados(listaCodEmpleado, statusEmpleado);
                threadPoolExecute.execute(() -> {
                    for (CantEmpleados ce : listCantEmpleados) {
                        daoTareo.disminuirEmpleado(ce.getCodigo(), ce.getCantEmpleados());
                    }
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los empleados"));
            }
        });
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareoLike(int idNivel, String search) {
        return daoConcepto.listaLike(idNivel, search);
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareoLike(int idNivel, int idPadre, String search) {
        return daoConcepto.listaxPadreLike(idNivel, idPadre, search);
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareoLikeCod(int idNivel, String search) {
        return daoConcepto.listaLikeCod(idNivel, search);
    }

    @Override
    public Single<List<ConceptoTareo>> obtenerConceptosTareoLikeCod(int idNivel, int idPadre, String search) {
        return daoConcepto.listaxPadreLikeCod(idNivel, idPadre, search);
    }

    @Override
    public Maybe<EmpleadoControlRow> verificarEmpleadoTareo(String codEmpleado, String codTareo) {
        return daoEmpleado.buscarporTareo(codEmpleado, codTareo);
    }

    @Override
    public Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla) {
        return daoEmpleado.buscarporNomina(codPlanilla, codEmpleado);
    }

    @Override
    public Maybe<Empleado> validarEmpleado(String codEmpleado) {
        return daoEmpleado.buscarporCodigo(codEmpleado);
    }

    @Override
    public Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado, @StatusFinDetalleTareo int statusDetalleTareo) {
        return daoDetalleTareo.validarExistenciaEmpleadoporDni(codEmpleado, statusDetalleTareo);
    }

    @Override
    public Maybe<Long> registrarEmpleado(Empleado nuevo) {
        return daoEmpleado.nuevo(nuevo);
    }

    @Override
    public Maybe<Long> registrarTareo(Tareo nuevo) {
        return daoTareo.nuevo(nuevo);
    }

    @Override
    public Maybe<Long> registrarAsistencia(Marcacion nuevo) {
        return daoMarcacion.nuevo(nuevo);
    }

    @Override
    public Maybe<Long> agregarEmpleadoTareo(DetalleTareo nuevo) {
        return daoDetalleTareo.registrarTrabajadorTareo(nuevo);
    }

    @Override
    public Single<Long[]> agregarEmpleadosTareo(List<DetalleTareo> lista) {
        return daoDetalleTareo.registrarTrabajadoresTareo(lista);
    }

    @Override
    public Single<Tareo> consultarTareo(String codigoTareo) {
        return daoTareo.consultarTareo(codigoTareo);
    }

    @Override
    public void actualizarEmpleados(List<DetalleTareo> empleados) {
        threadPoolExecute.execute(() -> {
            for (DetalleTareo item : empleados) {
                daoEmpleado.asignarTareo(item.getFkTareo(),
                        StatusEmpleado.EMPLEADO_NO_LIBRE,
                        item.getFkEmpleado());
            }
        });
    }

    @Override
    public void actualizarEmpleadosTareo(String codTareo, ArrayList<Integer> empleados) {
        threadPoolExecute.execute(() -> {
            daoEmpleado.asignarTareo(codTareo,
                    StatusEmpleado.EMPLEADO_NO_LIBRE,
                    empleados);
        });
    }

    @Override
    public void insertarNivelesTareo(List<NivelTareo> lista) {
        threadPoolExecute.execute(() -> {
            daoNivelesTareo.deleteAll();
            daoNivelesTareo.insertAll(lista);
        });
    }

    @Override
    public void insertarUsuarios(List<Usuario> usuarios) {
        threadPoolExecute.execute(() -> {
            daoUsuario.deleteAll();
            daoUsuario.insertAll(usuarios);
        });
    }

    @Override
    public void insertarUnidadMedida(List<UnidadMedida> lista) {
        threadPoolExecute.execute(() -> {
            daoUnidadMedida.deleteAll();
            daoUnidadMedida.insertAll(lista);
        });
    }

    @Override
    public void insertarTurnos(List<Turno> lista) {
        threadPoolExecute.execute(() -> {
            daoTurno.deleteAll();
            daoTurno.insertAll(lista);
        });
    }

    @Override
    public void insertarClaseTareo(List<ClaseTareo> lista) {
        threadPoolExecute.execute(() -> {
            daoClaseTareo.deleteAll();
            daoClaseTareo.insertAll(lista);
        });
    }

    @Override
    public void insertarConceptoTareo(List<ConceptoTareo> lista) {
        threadPoolExecute.execute(() -> {
            daoConcepto.deleteAll();
            daoConcepto.insertAll(lista);
        });
    }

    @Override
    public void insertarTareos(List<Tareo> lista) {
        threadPoolExecute.execute(() -> {
            //daoTareo.deleteAll();
            daoTareo.insertAll(lista);
        });
    }

    @Override
    @Transaction
    public void insertarDetalleTareos(List<DetalleTareo> lista) {
        threadPoolExecute.execute(() -> {
            //daoDetalleTareo.deleteAll();
            daoDetalleTareo.insertAll(lista);
        });
    }

    @Override
    public void insertarPerfil(List<Perfil> lista) {
        threadPoolExecute.execute(() -> {
            daoPerfil.deleteAll();
            daoPerfil.insertAll(lista);
        });
    }

    @Override
    public void insertarEmpleados(List<Empleado> lista) {
        threadPoolExecute.execute(() -> {
            daoEmpleado.deleteAll();
            daoEmpleado.insertAll(lista);
        });
    }

    @Override
    public Single<ClaseTareo> consultarClaseTareo(int idClaseTareo) {
        return daoClaseTareo.consultar(idClaseTareo);
    }

    @Override
    public Single<UnidadMedida> consultarUnidadMedida(int idUnidad) {
        return daoUnidadMedida.consultar(idUnidad);
    }

    @Override
    public Single<Turno> consultarTurno(int idTurno) {
        return daoTurno.consultar(idTurno);
    }

    @Override
    public Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo) {
        return daoDetalleTareo.consultarEmpleados(codigoTareo, statusFinTareo);
    }

    @Override
    public Single<List<DetalleTareo>> obtenerDetalleTareo(String codTareo,
                                                          @StatusFinDetalleTareo int statusFinTareo) {
        return daoDetalleTareo.consultar(codTareo, statusFinTareo);
    }

    @Override
    @Transaction
    public Single<Long> actualizarTareo(Tareo tareo) {
        return Single.create(emitter -> {
            try {
                daoTareo.actualizar(tareo.getCodTareo(), tareo.getCantTrabajadores(),
                        tareo.getFechaInicio(), tareo.getHoraInicio(), tareo.getUsuarioUpdate());
                daoEmpleado.liberarEmpleados(tareo.getCodTareo(), StatusEmpleado.EMPLEADO_LIBRE);
                daoDetalleTareo.deleteAllTareo(tareo.getCodTareo());
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se actualizo el documento"));
            }
        });
    }

    @Override
    @Transaction
    public Single<Long> finalizarTareoEmpleado(String codTareo, String empleado,
                                               String fechaFinTareo, String horaFinTareo,
                                               Boolean refrigerio, String fechaIniRefrigerio,
                                               String fechaFinRefrigerio, Boolean ultimo,
                                               String horaRegistroSalida) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.finalizarTareoEmpleado(codTareo, fechaFinTareo, horaFinTareo,
                        refrigerio, fechaIniRefrigerio, fechaFinRefrigerio,
                        StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO, empleado,
                        horaRegistroSalida);
                daoEmpleado.liberarEmpleado(codTareo, empleado, StatusEmpleado.EMPLEADO_LIBRE);
                if (ultimo) {
                    daoTareo.finalizarTareo(codTareo, fechaFinTareo, horaFinTareo, StatusTareo.TAREO_FINALIZADO);
                }
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los tareos"));
            }
        });
    }

    @Override
    @Transaction
    public Single<Long> finalizarTareos(ArrayList<String> listaCodTareos, String fechaFinTareo,
                                        String horaFinTareo, String fechaIniRefrigerio,
                                        String fechaFinRefrigerio) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.finalizarDetalleTareos(listaCodTareos, fechaFinTareo, horaFinTareo,
                        fechaIniRefrigerio, fechaFinRefrigerio,
                        StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO);
                daoEmpleado.liberarEmpleadosTareos(listaCodTareos, StatusEmpleado.EMPLEADO_LIBRE);
                daoTareo.finalizarTareos(listaCodTareos, fechaFinTareo, horaFinTareo,
                        StatusTareo.TAREO_FINALIZADO);
                emitter.onSuccess(1L);

            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los tareos"));
            }
        });
    }

    @Override
    public Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleadosFinalizados(String codTareo) {
        return daoDetalleTareo.codigosEmpleadosxEstadoFinTareo(codTareo,
                StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO);
    }

    @Override
    public Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleadosIniciados(String codTareo,
                                                                          @StatusFinDetalleTareo int estado) {
        return daoDetalleTareo.codigosEmpleadosxEstadoFinTareo(codTareo, estado);
    }

    @Override
    public Flowable<List<EmpleadoRow>> obtenerEmpleadosFinalizados(String codTareo) {
        return daoDetalleTareo.listaEmpleadosFinalizados(codTareo,
                StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO);
    }

    @Override
    public Flowable<List<EstadoEmpleadoRow>> obtenerEstadoEmpleados(String codTareo) {
        return daoDetalleTareo.listaEstadoEmpleados(codTareo);
    }

    @Override
    public Flowable<List<AllEmpleadosConsulta>> obtenerEmpleadosConsulta(String codTareo) {
        return daoDetalleTareo.obtenerEmpleadosConsulta(codTareo);
    }

    public Completable deleteTareo(String codigoTareo) {
        return daoTareo.delete(codigoTareo).doOnTerminate(() ->
                daoEmpleado.liberarEmpleadoDeleteTareo(codigoTareo, StatusEmpleado.EMPLEADO_LIBRE));
    }

    public Flowable<List<ResultadoRow>> obtenerResultadoEmpleados(String codigoTareo) {
        return daoResultadoTareo.listaEmpleados(codigoTareo);
    }

    public Single<EmpleadoResultadoRow> validarEmpleadoTareo(String codigoTareo, String codEmpleado) {
        return daoDetalleTareo.validarResultadoEmpleado(codigoTareo, codEmpleado);
    }

    public Completable guardarResultadoEmpleado(ResultadoTareo nuevo, String codDetalleTareo) {
        return daoResultadoTareo.insert(nuevo).doOnTerminate(() ->
                daoTareo.actualizarTotalProducido(codDetalleTareo));
    }

    public Completable modificarResultadoEmpleado(ResultadoTareo nuevo, String codTareo) {
        return daoResultadoTareo.actualizarCantidad(nuevo.getFechaModificacion(),
                nuevo.getCantidad(), nuevo.getFkUsuarioUpdate(), nuevo.getFkDetalleTareo()).doOnTerminate(() ->
                daoTareo.actualizarTotalProducido(codTareo));
    }

    public Completable guardarControl(DetalleTareoControl nuevo) {
        return daoDetalleTareoControl.insert(nuevo);
    }

    public Flowable<List<EmpleadoRow>> obtenerControlEmpleados(String codTareo) {
        return daoDetalleTareoControl.listaEmpleados(codTareo);
    }

    public Flowable<List<EmpleadoRow>> obtenerEmpleadosSinControl(String codTareo,
                                                                  @StatusIniDetalleTareo int estadoIniciado) {
        return daoDetalleTareo.listaEmpleadosSinControl(codTareo,
                estadoIniciado);
    }

    public Completable liquidarTareo(String codigoTareo, String fechaModicacion, int usuario) {
        return daoTareo.liquidar(codigoTareo, StatusTareo.TAREO_LIQUIDADO, fechaModicacion, usuario);
    }

    public Maybe<List<TareoPendiente>> tareosPendientesEnvio(@StatusDescargaServidor String estadoEnvio) {
        return daoTareo.porEstados(StatusTareo.TAREO_FINALIZADO, StatusTareo.TAREO_LIQUIDADO, estadoEnvio);
    }

    public Maybe<List<TareoPendiente>> tareosPendientesEnvio(@StatusTareo int status,
                                                             @StatusDescargaServidor String estadoEnvio) {
        return daoTareo.porEstados(status, estadoEnvio);
    }

    public Completable actualizarTareos(ArrayList<String> codigos, String flag) {
        return daoTareo.actualizarEstado(codigos, flag);
    }

    public Maybe<List<ResultadoTareo>> getAllResultado(ArrayList<String> codigos, String estadoResultado) {
        return daoResultadoTareo.getAllByEstadoxTareo(codigos, estadoResultado);
    }

    public Completable actualizarResultados(ArrayList<String> codigos, String flag) {
        return daoResultadoTareo.actualizarEstado(codigos, flag);
    }

    public Completable actualizarControles(ArrayList<Integer> codigos, String flagEstado) {
        return daoDetalleTareoControl.actualizarEstado(codigos, flagEstado);
    }

    public Maybe<List<DetalleTareoControl>> controlesPendientes() {
        return daoDetalleTareoControl.pendientes(StatusDescargaServidor.PENDIENTE);
    }

    public Maybe<List<ResultadoPorTareo>> obtenerResultadoPorTareo(ArrayList<String> codigosTareo,
                                                                   @StatusDescargaServidor String statusServer) {
        return daoResultadoPorTareo.obtenerResultadoPorTareo(codigosTareo, statusServer);
    }

    public Completable actualizarResultadoPorTareo(ArrayList<String> codigosTareo,
                                                   @StatusDescargaServidor String statusServer) {
        return daoResultadoPorTareo.actualizarResultadoPorTareo(codigosTareo, statusServer);
    }

    public Completable cambiarEstadoTareo(String flagInicial, String flagFinal) {
        return daoTareo.cambiarEstado(flagInicial, flagFinal);
    }

    public Maybe<List<AllEmpleadoRow>> obtenerAllEmpleadosWithTareo(@StatusTareo int statusTareo,
                                                                    @StatusFinDetalleTareo int estadoFinTareo,
                                                                    int idUsuario) {
        return daoDetalleTareo.allEmpleadosWithTareos(statusTareo, estadoFinTareo, idUsuario);
    }

    public Single<List<AllEmpleadoRow>> obtenerAllEmpleadosWithCodTareo(ArrayList<String> listCodTareo,
                                                                        @StatusFinDetalleTareo int estadoFinTareo) {
        return daoDetalleTareo.obtenerAllEmpleadosWithCodTareo(listCodTareo, estadoFinTareo);
    }

   /* @Override
    @Transaction
    public Single<Long> finalizarTareoParaReubicar(List<AllEmpleadoRow> listEmpleadoProFinalizar,
                                                   List<String> listaCodDetalleTareo,
                                                   @StatusRefrigerio int statusRefrigerio,
                                                   String fechaFinTareo, String horaFinTareo,
                                                   @StatusTareo int estadoTareo,
                                                   @StatusEmpleado int statusEmpleado,
                                                   String horaIniRefrigerio, String horaFinRefrigerio,
                                                   @StatusFinDetalleTareo int estadoFinTareo) {
        return Single.create(emitter -> {
            try {

                daoDetalleTareo.finalizarDetalleTareosParaReubicar(listaCodDetalleTareo, statusRefrigerio,
                        fechaFinTareo, horaFinTareo, horaIniRefrigerio, horaFinRefrigerio, estadoFinTareo);


                threadPoolExecute.execute(() -> {
                    for (AllEmpleadoRow empleadoRow : listEmpleadoProFinalizar) {
                        daoTareo.finalizarTareo(empleadoRow.getCodigoTareo(), fechaFinTareo, horaFinTareo, estadoTareo);
                        daoEmpleado.liberarEmpleado(empleadoRow.getCodigoTareo(), empleadoRow.getCodigoEmpleado(), statusEmpleado);

                        daoTareo.disminuirEmpleado(empleadoRow.getCodigoTareo(), 1);
                    }
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los tareos"));
            }
        });
    }*/

    /*@Override
    @Transaction
    public Single<Long> finalizarEmpleadoParaReubicar(List<String> listaCodDetalleTareo,
                                                      List<String> listaCodEmpleao,
                                                      @StatusRefrigerio int statusRefrigerio,
                                                      String fechaFinTareo, String horaFinTareo,
                                                      String horaIniRefrigerio, String horaFinRefrigerio,
                                                      @StatusFinDetalleTareo int estadoFinTareo,
                                                      @StatusEmpleado int statusEmpleado,
                                                      List<CantEmpleados> listaCantEmpleados) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.finalizarDetalleTareosParaReubicar(listaCodDetalleTareo, statusRefrigerio,
                        fechaFinTareo, horaFinTareo, horaIniRefrigerio, horaFinRefrigerio, estadoFinTareo);
                daoEmpleado.liberarEmpleados(listaCodEmpleao, statusEmpleado);
                threadPoolExecute.execute(() -> {
                    for (CantEmpleados ce : listaCantEmpleados) {
                        daoTareo.disminuirEmpleado(ce.getCodigo(), ce.getCantEmpleados());
                    }
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los empleados"));
            }
        });
    }*/

    @Override
    @Transaction
    public Single<Long> crearResultadoParaReubicarEmpleado(List<ResultadoTareo> resultadoTareo,
                                                           List<AllEmpleadoRow> cantProducida) {
        return Single.create(emitter -> {
            try {
                daoResultadoTareo.insertAllResultadoTareoVoid(resultadoTareo);
                threadPoolExecute.execute(() -> {
                    for (AllEmpleadoRow produccion : cantProducida) {
                        daoTareo.aumentarCantProducida(produccion.getCantProducida(), produccion.getCodigoTareo());
                    }
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los empleados"));
            }
        });
    }

    @Override
    @Transaction
    public Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos) {
        return Single.create(emitter -> {
            try {
                daoDetalleTareo.insertAllVoid(detalleTareos);
                String codTareo = detalleTareos.get(0).getFkTareo();
                daoTareo.aumentarEmpleados(codTareo, detalleTareos.size());
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo finalizar los empleados"));
            }
        });
    }

    @Override
    public Single<List<AllTareoRow>> obtenerAllTareos(int codigoUsuario) {
        return daoTareo.obtenerAllTareos(codigoUsuario);
    }

    @Override
    public Single<List<AllTareosWithResult>> obtenerTareoWithResult(List<String> listTareoEnd) {
        return daoTareo.obtenerTareoWithResult(listTareoEnd);
    }

    public Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codTareo, String codEmpleado) {
        return daoDetalleTareo.validarResultadoEmpleado(codTareo, codEmpleado);
    }

    public Single<List<AllEmpleadoConsultaRow>> obtenerAllEmpleados(String codTareo) {
        return daoDetalleTareo.obtenerAllEmpleados(codTareo);
    }

    public Single<TareoRow> obtenerTareoDetail(String codTareo) {
        return daoTareo.obtenerTareoDetail(codTareo);
    }

    @Override
    public Maybe<List<AllResultadoPorTareoRow>> obtenerListResultforTareo(String codTareo) {
        return daoResultadoPorTareo.obtenerListResultforTareo(codTareo);
    }

    @Override
    @Transaction
    public Single<Long> guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo) {
        return Single.create(emitter -> {
            try {
                daoResultadoPorTareo.insertVoid(resultadoPorTareo);
                threadPoolExecute.execute(() -> {
                    daoTareo.aumentarCantProducida(resultadoPorTareo.getCantidad(),
                            resultadoPorTareo.getFkTareo());
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo guardar Elresultado por tareo"));
            }
        });
    }

    @Override
    @Transaction
    public Single<Long> guardarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareo) {
        return Single.create(emitter -> {
            try {
                daoResultadoPorTareo.insertAll(resultadoPorTareo);
                threadPoolExecute.execute(() -> {
                    for (ResultadoPorTareo item : resultadoPorTareo) {
                        daoTareo.aumentarCantProducida(item.getCantidad(),
                                item.getFkTareo());
                    }
                });
                emitter.onSuccess(1L);
            } catch (Exception e) {
                emitter.onError(new Throwable("No se pudo guardar Elresultado por tareo"));
            }
        });
    }


    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciadosOnly(@StatusTareo int estado, int usuario,
                                                            @StatusDescargaServidor String estadoEnvio,
                                                            List<String> listCodTareos,
                                                            List<Integer> listCodClase) {
        return daoTareo.obtenerTareosIniciadosOnly(estado, usuario,
                estadoEnvio, listCodTareos, listCodClase);
    }

    @Override
    public Single<List<Marcacion>> obtenerAsistencias() {
        return daoMarcacion.listaAsistencias();
    }

    @Override
    public Single<List<Marcacion>> obtenerAsistenciasHoy() {
        return daoMarcacion.listaAsistenciasHoy();
    }

    @Override
    public Maybe<List<Marcacion>> obtenerAsistenciasPendientes() {
        return daoMarcacion.listaAsistenciaPendientes();
    }

    @Override
    public Single<List<Marcacion>> obtenerUltimaAsistenciaEmpleado(String codEmpleado) {
        return daoMarcacion.lastMarcacionByEmpleado(codEmpleado);
    }

    @Override
    public Single<List<Marcacion>> searchAsistenciasEmpleado(String query) {
        return daoMarcacion.searchMarcacion(query, query);
    }

    @Override
    public Maybe<Integer> updateEnvioMarcacion(int idMarcacion) {
        return daoMarcacion.updateEnvioMarcacion(idMarcacion);
    }

    @Override
    public Maybe<Long> registrarIncidencia(Incidencia nuevo) {
        return daoIncidencia.nuevo(nuevo);
    }

    @Override
    public Maybe<List<Incidencia>> obtenerIncidentesPendientesEnviar() {
        return daoIncidencia.listaIncidenciaPendientes();
    }

    @Override
    public Maybe<Integer> updateEnvioIncidencia(int idIncidencia) {
        return daoIncidencia.updateEnvioIncidencia(idIncidencia);
    }

    @Override
    public Single<List<Incidencia>> obtenerIncidentesByDate(String fch_ini, String fch_fin) {
        return daoIncidencia.getIncidenciasByDate(fch_ini, fch_fin);
    }

    @Override
    public Maybe<Long> registrarAcceso(Acceso nuevo) {
        return daoAcceso.nuevo(nuevo);
    }

    @Override
    public Maybe<List<Acceso>> obtenerAccesosPendientesEnviar() {
        return daoAcceso.listaAccesoPendientes();
    }

    @Override
    public Maybe<Integer> updateEnvioAcceso(int id) {
        return daoAcceso.updateEnvioAcceso(id);
    }

    @Override
    public Single<List<Acceso>> obtenerAccesoByDate(String fch_ini, String fch_fin) {
        return daoAcceso.getAccesosByDate(fch_ini, fch_fin);
    }
}