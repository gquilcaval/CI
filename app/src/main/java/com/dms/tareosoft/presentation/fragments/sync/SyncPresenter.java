package com.dms.tareosoft.presentation.fragments.sync;

import android.os.Handler;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.pojos.CodigoCadena;
import com.dms.tareosoft.data.pojos.CodigoNumerico;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.sync.ISyncInteractor;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.TextUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncPresenter extends BasePresenter<ISyncContract.View>
        implements ISyncContract.Presenter {

    String TAG = SyncPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    ISyncInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    DateTimeManager appDateTime;

    int countEnvioAsistencis = 0;
    int totalEnvioAsistencias = 0;

    int countEnvioIncidencias = 0;
    int totalEnvioIncidencias = 0;

    int countEnvioAccesos = 0;
    int totalEnvioAccesos = 0;

    @Inject
    public SyncPresenter(ISyncInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void attachView(ISyncContract.View view) {
        super.attachView(view);
        getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    public void validarConexion() {
        getView().showProgressbar("Conectando", "Obteniendo fecha y hora servidor ...");
        disposable = interactor.validarConexion()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribeWith(new DisposableSingleObserver<RespuestaGeneral>() {
                    @Override
                    public void onSuccess(RespuestaGeneral respuesta) {
                        if (respuesta.getCodigo() == Constants.API_SUCCESS) {
                            sincronizarFechaHora();
                        } else {
                            if (isViewAttached()) {
                                getView().showWarningMessage(respuesta.getMensaje());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showErrorMessage("No se pudo conectar con el servicio", e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void sincronizarFechaHora() {
        interactor.obtenerFechaHora().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String fechaServidor = response.body();
                String fechaEquipo = DateUtil.obtenerFechaHoraEquipo(Constants.F_LECTURA);
//TODO REMOVER
                preferenceManager.setFechaHoraServidor(fechaServidor);
                preferenceManager.setMargenTiempo(DateUtil.stringToLongFormat(fechaServidor, Constants.F_FECHAHORA_WS) - DateUtil.stringToLongFormat(fechaEquipo, Constants.F_LECTURA));
                getView().mostrarFechaHora(fechaServidor, appDateTime.getFechaSincronizada(Constants.F_FECHAHORA_WS));

                getView().showSuccessMessage(getView().getMessage(R.string.servicio_correcto));
                validarFechaHora();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (isViewAttached()) {
                    getView().showErrorMessage("No se pudo obtener la fecha del servidor", t.getMessage());
                }
            }
        });
    }

    @Override
    public void validarFechaHora() {
        String fechaServidor = preferenceManager.getFechaHoraServidor();
        if (!fechaServidor.equals(Constants.sinFecha)) {
            //getMargenTiempo : Diferencia que diferencia el servidor del equipo
            if (preferenceManager.getMargenTiempo() > TextUtil.convertIntToLong(preferenceManager.getMaximoMargen())) { //TODO VALIDAR LONG
                getView().actualizarFechaHora("El equipo movil se encuentra retrasado más de " + preferenceManager.getMaximoMargen() + " min.");
                preferenceManager.setFechaDesfasada(true);
            } else if (preferenceManager.getMargenTiempo() < -((preferenceManager.getMaximoMargen() * 60) * 1000)) {
                getView().actualizarFechaHora("El equipo movil se encuentra adelantado más de " + preferenceManager.getMaximoMargen() + " min.");
                preferenceManager.setFechaDesfasada(true);
            } else {
                preferenceManager.setFechaDesfasada(false);
            }
        }
    }

    public void cargarMaestros() {
        getView().showProgressbar("Carga", "Cargando maestros...");
        List<Observable> observableList = new ArrayList<>(Arrays.asList(
                interactor.cargarUsuarios(),
                interactor.cargarUnidadMedida(),
                interactor.cargarTurnos(),
                interactor.cargarClases(),
                interactor.cargarConceptos(),
                interactor.cargarNiveles(),
                interactor.cargarPerfiles(),
                interactor.cargarTareosAbiertos(preferenceManager.getCodigoEnvioUsuario()),
                interactor.cargarDetalleTareosAbiertos(preferenceManager.getCodigoEnvioUsuario()),
                interactor.cargarEmpleados()
        ));

        getCompositeDisposable().add(Observable.fromIterable(observableList)
                .subscribeOn(ExecutorThread)
                .flatMap((Function<Observable, ObservableSource<?>>) observable -> observable.observeOn(UiThread))
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    getView().showSuccessMessage("Carga de maestros exitosa.");
                })
                .subscribe(list -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().showErrorMessage("No se pudo descargar los maestros.", throwable.getMessage());
                    }
                })
        );
    }

    public void verifyTareosPending() {
        getView().showProgressPercentage("Descarga", "Obteniendo tareos del dispositivo ...");
        getCompositeDisposable().add(interactor.getTareos(StatusTareo.TAREO_ACTIVO,
                StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    //validar si el tareo es destajo tiene que ingresar su resultado para poder ser enviado.
                    if (lista != null && lista.size() > 0) {
                        getView().hiddenProgressPercentage();
                        if (preferenceManager.getValidaDescarga()) {
                            getView().mostrarMensajeTareosActivos();
                        } else {
                            obtenerTareos();
                        }
                    } else {
                        obtenerTareos();
                    }
                }, throwable -> {
                    getView().hiddenProgressPercentage();
                    getView().showWarningMessage("No se pudo obtener los registros");
                }));
    }

    public void obtenerTareos() {
        //10%  Se envian todos los Tareos Cerrados o Liquidados
        Log.e(TAG, "obtenerTareos");
        getView().updatePercentage(10, "Enviando tareos ...");
        getCompositeDisposable().add(interactor.getTareos(StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    //validar si el tareo es destajo tiene que ingresar su resultado para poder ser enviado.
                    if (lista != null && lista.size() > 0) {
                        Log.e(TAG, "obtenerTareos subscribe: " + lista);
                        getView().updatePercentage(20, "Enviando tareos ...");
                        enviarTareos(lista);
                    } else {
                        getView().mostrarMensajeSinDatos();
                        getView().hiddenProgressPercentage();
                    }
                }, throwable -> {
                    Log.e(TAG, "obtenerTareos throwable: " + throwable);
                    Log.e(TAG, "obtenerTareos throwable: " + throwable.toString());
                    Log.e(TAG, "obtenerTareos throwable: " + throwable.getMessage());
                    Log.e(TAG, "obtenerTareos throwable: " + throwable.getLocalizedMessage());
                    getView().showWarningMessage("No se pudo obtener los registros");
                    getView().hiddenProgressPercentage();
                }));
    }

    private void enviarTareos(List<TareoPendiente> solicitud) {
        String json = new Gson().toJson(solicitud);
        Log.e(TAG, "enviarTareos json: " + json);
        getView().updatePercentage(30, "Actualizando tareos ...");
        ArrayList<String> tareosEnviados = new ArrayList();
        getCompositeDisposable().add(interactor.descargarTareos(solicitud)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {
                    Log.e(TAG, "enviarTareos subscribe: " + respuesta);
                    if (respuesta.getStatus()) {
                        //30%
                        //Actualizo que se envio exitosamente los tareos 'E'(Pendiente tablas relacionadas al tareo: resultado y verificaciones)
                        getView().updatePercentage(40, "Actualizando tareos ...");
                        for (CodigoCadena item : respuesta.getDatos()) {
                            tareosEnviados.add(item.getCodigo());
                        }
                        if (tareosEnviados != null && tareosEnviados.size() > 0)
                            actualizarTareos(tareosEnviados, StatusDescargaServidor.ENVIADO);
                        else
                            getView().showWarningMessage(respuesta.getMsg());
                    } else {
                        getView().showWarningMessage(respuesta.getMsg());
                    }
                }, throwable -> {
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarTareos throwable: " + throwable);
                    Log.e(TAG, "enviarTareos throwable: " + throwable.toString());
                    Log.e(TAG, "enviarTareos throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarTareos throwable: " + throwable.getLocalizedMessage());
                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarTareos(ArrayList<String> codigos, String estado) {
        Log.e(TAG, "actualizarTareos");
        getCompositeDisposable().add(interactor.actualizarTareos(codigos, estado)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            Log.e(TAG, "actualizarTareos subscribe: ");
                            getView().updatePercentage(50, "Obteniendo resultados ...");
                            obtenerResultadoPorEmpleados(codigos);
                        }, throwable -> {
                            Log.e(TAG, "actualizarTareos throwable: " + throwable);
                            Log.e(TAG, "actualizarTareos throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarTareos throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarTareos throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    private void obtenerResultadoPorEmpleados(ArrayList<String> codigosTareo) {
        Log.e(TAG, "obtenerResultadoPorEmpleados");
        getCompositeDisposable().add(interactor.obtenerResultadosPendientes(codigosTareo,
                StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    //50%
                    Log.e(TAG, "obtenerResultadoPorEmpleados subscribe: " + lista);
                    getView().updatePercentage(60, "Enviando resultados ...");
                    if (lista != null && lista.size() > 0) {
                        enviarResultadosPorEmpleados(lista, codigosTareo);
                    } else {
                        obtenerResultadoPorTareo(codigosTareo);
                    }
                }, throwable -> {
                    Log.e(TAG, "obtenerResultadoPorEmpleados throwable: " + throwable);
                    Log.e(TAG, "obtenerResultadoPorEmpleados throwable: " + throwable.toString());
                    Log.e(TAG, "obtenerResultadoPorEmpleados throwable: " + throwable.getMessage());
                    Log.e(TAG, "obtenerResultadoPorEmpleados throwable: " + throwable.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showWarningMessage("No se pudo obtener los registros");
                }));
    }

    private void enviarResultadosPorEmpleados(List<ResultadoTareo> solicitud,
                                              ArrayList<String> codigosTareo) {
        getView().updatePercentage(70, "Actualizando resultados ...");
        String json = new Gson().toJson(solicitud);
        Log.e(TAG, "enviarResultadosPorEmpleados json: " + json);
        ArrayList<String> codigoResultados = new ArrayList<>();
        getCompositeDisposable().add(interactor.descargarResultados(solicitud)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {
                    Log.e(TAG, "enviarResultadosPorEmpleados subscribe: " + respuesta);
                    if (respuesta.getStatus()) {
                        //60% actualizar resultados
                        for (CodigoCadena item : respuesta.getDatos()) {
                            codigoResultados.add(item.getCodigo());
                        }
                        actualizarResultadosPorEmpleados(codigoResultados, codigosTareo);
                    } else {
                        getView().hiddenProgressPercentage();
                        getView().mostrarMensajeSyncExitosa();
                    }
                }, throwable -> {
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable);
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.toString());
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.getLocalizedMessage());
                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarResultadosPorEmpleados(ArrayList<String> codigos,
                                                  ArrayList<String> codigosTareo) {
        Log.e(TAG, "actualizarResultadosPorEmpleados");
        getCompositeDisposable().add(interactor.actualizarResultados(codigos, StatusDescargaServidor.BACKUP)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            //70% Enviamos verificaciones
                            Log.e(TAG, "actualizarResultadosPorEmpleados subscribe: ");
                            getView().updatePercentage(80, "Obteniendo verificaciones ...");
                            obtenerResultadoPorTareo(codigosTareo);
                        }, throwable -> {
                            getView().hiddenProgressbar();
                            Log.e(TAG, "actualizarResultadosPorEmpleados throwable: " + throwable);
                            Log.e(TAG, "actualizarResultadosPorEmpleados throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarResultadosPorEmpleados throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarResultadosPorEmpleados throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    private void obtenerResultadoPorTareo(ArrayList<String> codigosTareo) {
        Log.e(TAG, "obtenerResultadoPorTareo");
        getCompositeDisposable().add(interactor.obtenerResultadoPorTareo(codigosTareo, StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(resultadoPorTareoList -> {
                            //70% Enviamos verificaciones
                            Log.e(TAG, "obtenerResultadoPorTareo subscribe: " + resultadoPorTareoList);
                            if (resultadoPorTareoList != null && resultadoPorTareoList.size() > 0) {
                                getView().updatePercentage(80, "Obteniendo verificaciones ...");
                                enviarResultadoPorTareo(resultadoPorTareoList, codigosTareo);
                            } else {
                                obtenerControles();
                            }
                        }, throwable -> {
                            getView().hiddenProgressbar();
                            Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable);
                            Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.toString());
                            Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getMessage());
                            Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    private void enviarResultadoPorTareo(List<ResultadoPorTareo> resultadoPorTareos,
                                         ArrayList<String> codigosTareo) {
        getView().updatePercentage(80, "Obteniendo verificaciones ...");
        String json = new Gson().toJson(resultadoPorTareos);
        Log.e(TAG, "enviarResultadoPorTareo json: " + json);
        getCompositeDisposable().add(interactor.enviarResultadoPorTareo(resultadoPorTareos)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(resultado -> {
                            //70% Enviamos verificaciones
                            Log.e(TAG, "enviarResultadoPorTareo subscribe: " + resultado);
                            actualizarResultadoPorTareo(codigosTareo);
                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "enviarResultadoPorTareo throwable: " + throwable);
                            Log.e(TAG, "enviarResultadoPorTareo throwable: " + throwable.toString());
                            Log.e(TAG, "enviarResultadoPorTareo throwable: " + throwable.getMessage());
                            Log.e(TAG, "enviarResultadoPorTareo throwable: " + throwable.getLocalizedMessage());
                            getView().mostrarMensajeSyncFallida(throwable.getMessage());
                        }
                ));
    }

    private void actualizarResultadoPorTareo(ArrayList<String> codigosTareo) {
        Log.e(TAG, "actualizarResultadoPorTareo");
        getCompositeDisposable().add(interactor.actualizarResultadoPorTareo(codigosTareo,
                StatusDescargaServidor.BACKUP)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            //70% Enviamos verificaciones
                            Log.e(TAG, "actualizarResultadoPorTareo subscribe: ");
                            getView().updatePercentage(80, "Obteniendo verificaciones ...");
                            obtenerControles();
                        }, throwable -> {
                            getView().hiddenProgressbar();
                            Log.e(TAG, "actualizarResultadoPorTareo throwable: " + throwable);
                            Log.e(TAG, "actualizarResultadoPorTareo throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarResultadoPorTareo throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarResultadoPorTareo throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    private void obtenerControles() {
        Log.e(TAG, "obtenerControles");
        getCompositeDisposable().add(interactor.listaControles()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    //75%
                    Log.e(TAG, "obtenerControles subscribe: " + lista);
                    if (lista != null && lista.size() > 0) {
                        getView().updatePercentage(85, "Enviando verificaciones ...");
                        enviarControles(lista);
                    } else {
                        actualizarTareosBackup();
                    }
                }, throwable -> {
                    Log.e(TAG, "obtenerControles throwable: " + throwable);
                    Log.e(TAG, "obtenerControles throwable: " + throwable.toString());
                    Log.e(TAG, "obtenerControles throwable: " + throwable.getMessage());
                    Log.e(TAG, "obtenerControles throwable: " + throwable.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showWarningMessage("No se pudo obtener los registros");
                }));
    }

    private void enviarControles(List<DetalleTareoControl> solicitud) {
        getView().updatePercentage(90, "Actualizando verificaciones ...");
        String json = new Gson().toJson(solicitud);
        Log.e(TAG, "enviarControles json: " + json);
        ArrayList<Integer> codigosControles = new ArrayList<>();
        getCompositeDisposable().add(interactor.descargarControles(solicitud)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {
                    Log.e(TAG, "enviarControles subscribe: " + respuesta);
                    if (respuesta.getStatus()) {
                        //90%
                        for (CodigoNumerico item : respuesta.getDatos()) {
                            codigosControles.add(item.getCodigo());
                        }
                        actualizarControles(codigosControles);
                    } else {
                        getView().hiddenProgressPercentage();
                        getView().mostrarMensajeSyncFallida(respuesta.getMsg());
                    }

                }, throwable -> {
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarControles throwable: " + throwable);
                    Log.e(TAG, "enviarControles throwable: " + throwable.toString());
                    Log.e(TAG, "enviarControles throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarControles throwable: " + throwable.getLocalizedMessage());
                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarControles(ArrayList<Integer> codigos) {
        getView().updatePercentage(95, "Actualizando estados ...");
        getCompositeDisposable().add(interactor.actualizarControles(codigos, StatusDescargaServidor.BACKUP)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarControles subscribe: ");
                            actualizarTareosBackup();
                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarControles throwable: " + throwable);
                            Log.e(TAG, "actualizarControles throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarControles throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarControles throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    private void actualizarTareosBackup() {
        getView().updatePercentage(100, "Descarga Completada ...");
        getCompositeDisposable().add(interactor.cambiarEstadoTareos(StatusDescargaServidor.ENVIADO,
                StatusDescargaServidor.BACKUP)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarTareosBackup subscribe: ");
                            getView().mostrarMensajeSyncExitosa();

                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarTareosBackup throwable: " + throwable);
                            Log.e(TAG, "actualizarTareosBackup throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarTareosBackup throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarTareosBackup throwable: " + throwable.getLocalizedMessage());
                            getView().mostrarMensajeSyncFallida(throwable.getMessage());
                        }
                ));
    }

    /****   MODULO ASISTENCIA  ***/
    @Override
    public void obtenerMarcacionesPendientesEnvio() {
        getView().showProgressbar("Buscar", "Buscando Asistencias");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getView().hiddenProgressbar();
                Log.e(TAG, "obtenerResultadoPorTareo");
                getCompositeDisposable().add(interactor.obtenerMarcacionesLocal()
                        .subscribeOn(ExecutorThread)
                        .observeOn(UiThread)
                        .subscribe(result -> {
                            Log.d(TAG, "obtenerMarcacionesPendientesEnvio RESULT -> " + result);
                            Log.d(TAG, "obtenerMarcacionesPendientesEnvio RESULT size-> " + result.size());
                                    totalEnvioAsistencias = result.size();
                                    if (totalEnvioAsistencias == 0) {
                                        getView().showProgressbar("Buscar", "0 Asistencias encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                                obtenerIncidenciasLocal();
                                            };
                                        }, 2000); //Cad
                                    } else {
                                        getView().showProgressbar("Buscar", result.size() + " Asistencias encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                                getView().showProgressPercentage("Envio", "Enviando Asistencias ...");
                                                for (Marcacion marcacion: result) {
                                                    enviarMarcaciones(marcacion);
                                                }
                                            };
                                        }, 2000); //Cad

                                    }

                                }, throwable -> {
                                    getView().hiddenProgressbar();
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable);
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.toString());
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getMessage());
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getLocalizedMessage());
                                    getView().showErrorMessage("", throwable.getMessage());
                                    obtenerIncidenciasLocal();
                                }
                        ));
            };
        }, 2000); //Cad

    }
    private void enviarMarcaciones(Marcacion marcacion) {
        Log.e(TAG, "renviarMarcacion -> " + marcacion);
        getCompositeDisposable().add(interactor.registrarAsistencia(marcacion)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {

                    Log.d(TAG, "ENVIAR asistencias -> " + respuesta);
                    if (respuesta == 1) {
                        countEnvioAsistencis += 1;
                        int percentage = (countEnvioAsistencis * 100 / totalEnvioAsistencias);
                        getView().updatePercentage(percentage, "Enviando Asistencias ...");
                        actualizarMarcaciones(marcacion.getIdMarcacion());
                        if (totalEnvioAsistencias == countEnvioAsistencis) {
                            countEnvioAsistencis = 0;
                            totalEnvioAsistencias = 0;
                            getView().updatePercentage(100, "Envio de asistencias completado ...");
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    getView().hiddenProgressPercentage();
                                    obtenerIncidenciasLocal();
                                };
                            }, 2000); //Cad

                        }
                    }
                }, throwable -> {
                    totalEnvioAsistencias = 0;
                    countEnvioAsistencis = 0;
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable);
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.toString());
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarResultadosPorEmpleados throwable: " + throwable.getLocalizedMessage());
                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarMarcaciones(int idMarcacion) {
        getCompositeDisposable().add(interactor.updateEnvioMarcacion(idMarcacion)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarControles throwable: " + throwable);
                            Log.e(TAG, "actualizarControles throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarControles throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarControles throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));
    }

    /** MODULO MARCACION INCIDENCIA ***/

    public  void obtenerIncidenciasLocal()  {
        getView().showProgressbar("Buscar", "Buscando Incidencias");
        new Handler().postDelayed(new Runnable() {
            public void run() {
            getView().hiddenProgressbar();
                getCompositeDisposable().add(interactor.obtenerIncidenciasLocal()
                        .subscribeOn(ExecutorThread)
                        .observeOn(UiThread)
                        .subscribe(result -> {
                                    totalEnvioIncidencias = result.size();
                                    if (totalEnvioIncidencias == 0) {
                                        getView().showProgressbar("Buscar", "0 Incidencias encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                                obtenerAccesoLocal();
                                            };
                                        }, 2000); //Cad
                                    } else {
                                        getView().showProgressbar("Buscar", result.size() + " Incidencias encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                                getView().showProgressPercentage("Envio", "Enviando Incidencias ...");
                                                for (Incidencia incidencia: result) {
                                                    Log.e(TAG, "result list -> " + incidencia);
                                                    enviarIncidencia(incidencia);
                                                }
                                            };
                                        }, 2000); //Cad

                                    }
                                }, throwable -> {
                                    getView().hiddenProgressbar();
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable);
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.toString());
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getMessage());
                                    Log.e(TAG, "obtenerResultadoPorTareo throwable: " + throwable.getLocalizedMessage());
                                    getView().showErrorMessage("", throwable.getMessage());
                                    obtenerAccesoLocal();
                                }
                        ));
            };
        }, 2000); //Cada 5 segundos, 5000 milisegundos.


    }

    private void enviarIncidencia(Incidencia incidencia) {
        getCompositeDisposable().add(interactor.enviarIncidencias(incidencia)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {
                    Log.d(TAG, "ENVIAR INCIDENCIAS -> " + respuesta);
                    if (respuesta == 1) {
                        countEnvioIncidencias += 1;
                        int percentage = (countEnvioIncidencias * 100 / totalEnvioIncidencias);
                        getView().updatePercentage(percentage, "Enviando Incidencias ...");
                        actualizarIncidencia(incidencia.getInt_MarcaInsidencia());
                        if (totalEnvioIncidencias == countEnvioIncidencias) {
                            countEnvioIncidencias = 0;
                            totalEnvioIncidencias = 0;
                            getView().updatePercentage(100, "Envio de incidencias completado ...");
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    getView().hiddenProgressPercentage();
                                    obtenerAccesoLocal();
                                };
                            }, 2000); //Cad
                        }
                    }
                }, throwable -> {
                    countEnvioIncidencias = 0;
                    totalEnvioIncidencias = 0;
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable);
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.toString());
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.getLocalizedMessage());

                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarIncidencia(int idIncidencia) {
        getCompositeDisposable().add(interactor.updateEnvioIncidencia(idIncidencia)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable);
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));

    }



    /** MODULO MARCACION ACCESO ***/

    public  void obtenerAccesoLocal()  {
        getView().showProgressbar("Buscar", "Buscando Accesos");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getView().hiddenProgressbar();
                getCompositeDisposable().add(interactor.obtenerAccesosLocal()
                        .subscribeOn(ExecutorThread)
                        .observeOn(UiThread)
                        .subscribe(result -> {
                                    totalEnvioAccesos = result.size();
                                    if (totalEnvioAccesos == 0) {
                                        getView().showProgressbar("Buscar", "0 Accesos encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                            };
                                        }, 2000); //Cad
                                    } else {
                                        getView().showProgressbar("Buscar", result.size() + " Accesos encontradas");
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                getView().hiddenProgressbar();
                                                getView().showProgressPercentage("Envio", "Enviando Accesos ...");
                                                for (Acceso acceso: result) {
                                                    Log.e(TAG, "result list -> " + acceso);
                                                    enviarAcceso(acceso);
                                                }
                                            };
                                        }, 2000); //Cad

                                    }
                                }, throwable -> {
                                    getView().hiddenProgressbar();
                                    Log.e(TAG, "obtenerAccesoLocal throwable: " + throwable);
                                    Log.e(TAG, "obtenerAccesoLocal throwable: " + throwable.toString());
                                    Log.e(TAG, "obtenerAccesoLocal throwable: " + throwable.getMessage());
                                    Log.e(TAG, "obtenerAccesoLocal throwable: " + throwable.getLocalizedMessage());
                                    getView().showErrorMessage("", throwable.getMessage());
                                }
                        ));
            };
        }, 2000); //Cada 5 segundos, 5000 milisegundos.


    }

    private void enviarAcceso(Acceso acceso) {
        Log.d(TAG, "ENVIAR Acceso -> " + acceso);
        getCompositeDisposable().add(interactor.enviarAcceso(acceso)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(respuesta -> {
                    Log.d(TAG, "ENVIAR Acceso respuesta-> " + respuesta);
                    try {
                        if (respuesta.getSuccess() == true) {
                            countEnvioAccesos += 1;
                            int percentage = (countEnvioAccesos * 100 / totalEnvioAccesos);
                            getView().updatePercentage(percentage, "Enviando Acceso ...");
                            actualizarAcceso(acceso.getId());
                            if (totalEnvioAccesos == countEnvioAccesos) {
                                countEnvioAccesos = 0;
                                totalEnvioAccesos = 0;
                                getView().updatePercentage(100, "Envio de Accesos completado ...");
                            }
                        }
                    } catch (NullPointerException e) { Log.e(TAG, "enviarAcceso RESPONSE NULL: " );}

                }, throwable -> {
                    countEnvioAccesos = 0;
                    totalEnvioAccesos = 0;
                    getView().hiddenProgressPercentage();
                    Log.e(TAG, "enviarAcceso throwable: " + throwable);
                    Log.e(TAG, "enviarAcceso throwable: " + throwable.toString());
                    Log.e(TAG, "enviarAcceso throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarAcceso throwable: " + throwable.getLocalizedMessage());
                    getView().mostrarMensajeSyncFallida(throwable.getMessage());
                }));
    }

    private void actualizarAcceso(int id) {
        getCompositeDisposable().add(interactor.updateEnvioAcceso(id)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            getView().hiddenProgressPercentage();
                            Log.e(TAG, "actualizarAcceso throwable: " + throwable);
                            Log.e(TAG, "actualizarAcceso throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarAcceso throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarAcceso throwable: " + throwable.getLocalizedMessage());
                            getView().showErrorMessage("", throwable.getMessage());
                        }
                ));

    }

}