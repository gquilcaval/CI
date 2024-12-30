package com.dms.tareosoft.presentation.fragments.settings;

import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.SyncExitosa;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.data.models.ContenidoGeneral;
import com.dms.tareosoft.data.models.ContenidoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.models.RespuestaGeneral;
import com.dms.tareosoft.domain.settings_interactor.ISettingsInteractor;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.MyCallback;
import com.dms.tareosoft.util.TextUtil;

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

public class SettingPresenter extends BasePresenter<ISettingContract.View>
        implements ISettingContract.Presenter {
    private List<UnidadMedida> listaUnidadMedidas;
    private List<Turno> listaTurnos;
    private List<ClaseTareo> listaClaseTareos;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    DateTimeManager dateTimeManager;

    @Inject
    ISettingsInteractor interactor;

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;

    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private ContenidoGeneral camposGeneral;
    private ContenidoTareo camposTareo;
    private ContenidoAjustes camposAjustes;

    @Inject
    SettingPresenter(PreferenceManager preferenceManager, ISettingsInteractor interactor) {
        this.preferenceManager = preferenceManager;
        this.interactor = interactor;

        this.listaUnidadMedidas = new ArrayList<>();
        this.listaTurnos = new ArrayList<>();
        this.listaClaseTareos = new ArrayList<>();

        camposGeneral = new ContenidoGeneral();
        camposTareo = new ContenidoTareo();
        camposAjustes = new ContenidoAjustes();
    }

    @Override
    public void attachView(ISettingContract.View view) {
        super.attachView(view);
        getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void cargarVista() {
        if (preferenceManager.getModoTrabajo() == ModoTrabajo.LINEA) {
            getView().setTextButton(getView().getMessage(R.string.btn_validar_conexion));
        }

        //Validar reinicio por cambio de web service
        if (preferenceManager.getNuevoServicio()) {
            String titulo = getView().getMessage(R.string.validando);
            String mensaje = getView().getMessage(R.string.conectando);
            if (preferenceManager.getModoTrabajo() == ModoTrabajo.BATCH) {
                titulo = getView().getMessage(R.string.carga);
                mensaje = getView().getMessage(R.string.maestros);
            }
            getView().showProgressbar(titulo, mensaje);
            cargarCamposPreferencias();
            validarConexion();
        } else {
            //Verificamos si la fecha necesita ser actualizada
            if (preferenceManager.getServicioValidado()) {
                getView().showProgressbar("Servicio", "Validando fecha");
                dateTimeManager.verificarFechaEquipo(new MyCallback() {
                    @Override
                    public void onSucess() {
                        getView().hiddenProgressbar();
                        mostrarFechaHora();
                    }

                    @Override
                    public void onFailure(String message) {
                        getView().hiddenProgressbar();
                        mostrarFechaHora();
                    }
                });
            }

            cargarCamposPreferencias();
            listarUnidadesMedidas();
            listarTurnos();
            listarClasesTareo();
        }
    }

    private void cargarCamposPreferencias() {
        ContenidoGeneral contenidoGeneral = preferenceManager.getContenidoGeneral();
        contenidoGeneral.setFechaHora(dateTimeManager.getFechaSincronizada(Constants.F_FECHAHORA_WS));

        getView().mostrarCampos(contenidoGeneral, preferenceManager.getContenidoTareo(), preferenceManager.getContenidoAjustes());
        getView().limpiarFlags();
    }

    private void listarTurnos() {
        disposable = interactor.listarTurnos()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(resultado -> {
                            List<String> listaSpinner = new ArrayList<>();
                            int posicion = 0;
                            int indice = 1;
                            if (resultado.size() > 0) {
                                listaSpinner.add(getView().getMessage(R.string.seleccione));
                            } else {
                                listaSpinner.add(getView().getMessage(R.string.sin_datos));
                            }
                            listaTurnos.clear();
                            listaTurnos.addAll(resultado);
                            for (Turno item : listaTurnos) {
                                if (!(item.getId() == preferenceManager.getTurno())) {
                                    indice += 1;
                                } else {
                                    posicion = indice;
                                }
                                listaSpinner.add(item.getDescripcion());
                            }
                            getView().listarTurno(listaSpinner, posicion);
                        },
                        error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.sin_datos), error.getMessage());
                        }
                );
    }

    private void listarUnidadesMedidas() {
        disposable = interactor.listarUnidadMedidas()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(list -> {
                            listaUnidadMedidas.clear();
                            if (list.size() > 0) {
                                listaUnidadMedidas.add(new UnidadMedida(0,
                                        getView().getMessage(R.string.seleccione)));
                            } else {
                                listaUnidadMedidas.add(new UnidadMedida(0,
                                        getView().getMessage(R.string.sin_datos)));
                            }
                            for (UnidadMedida item : list) {
                                listaUnidadMedidas.add(item);
                            }
                            getView().listarUnidadMedida(listaUnidadMedidas,
                                    preferenceManager.getTareoUnidadMedida());
                        },
                        error -> {
                            getView().showErrorMessage("No se pudo obtener las unidades", error.getMessage());
                        }
                );
    }

    private void listarClasesTareo() {
        disposable = interactor.listarClaseTareo()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> getView().limpiarFlags())
                .subscribe(resultado -> {
                            List<String> listaSpinner = new ArrayList<>();
                            int posicion = 0;
                            int indice = 1;
                            if (resultado.size() > 0) {
                                listaSpinner.add(getView().getMessage(R.string.seleccione));
                            } else {
                                listaSpinner.add(getView().getMessage(R.string.sin_datos));
                            }
                            listaClaseTareos.clear();
                            listaClaseTareos.addAll(resultado);
                            for (ClaseTareo item : listaClaseTareos) {
                                if (!(item.getId() == preferenceManager.getClaseTareo())) {
                                    indice += 1;
                                } else {
                                    posicion = indice;
                                }
                                listaSpinner.add(item.getDescripcion());
                            }
                            getView().listarClaseTarea(listaSpinner, posicion);
                        },
                        error -> {
                            getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                        }
                );
    }

    @Override
    public void validarConexion() {
        disposable = interactor.validarConexion()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribeWith(new DisposableSingleObserver<RespuestaGeneral>() {
                    @Override
                    public void onSuccess(RespuestaGeneral respuesta) {
                        if (respuesta.getCodigo() == Constants.API_SUCCESS) {
                            dateTimeManager.sincronizarFechaHora(new MyCallback() {
                                @Override
                                public void onSucess() {
                                    mostrarFechaHora();
                                    switch (preferenceManager.getModoTrabajo()) {
                                        case ModoTrabajo.BATCH:
                                            sincronizarMaestros();
                                            break;
                                        case ModoTrabajo.LINEA:
                                            if (preferenceManager.getNuevoServicio()) {
                                                preferenceManager.setNuevoServicio(false);
                                                preferenceManager.setServicioValidado(true);
                                            }
                                            getView().showSuccessMessage(getView().getMessage(R.string.servicio_correcto));
                                            validarFechaHora();
                                            break;
                                    }
                                }

                                @Override
                                public void onFailure(String message) {
                                    getView().showErrorMessage("No se pudo obtener la fecha del servidor", message);
                                }
                            });
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

    private void validarFechaHora() {
        String mensaje = dateTimeManager.validarFechaHora();
        if (mensaje != null) {
            getView().actualizarFechaHora(mensaje);
            mostrarFechaHora();
        }
    }

    private void mostrarFechaHora() {
        String fechaServidor = preferenceManager.getFechaHoraServidor();
        getView().mostrarFechaHora(fechaServidor, dateTimeManager.getFechaSincronizada(Constants.F_FECHAHORA_WS));

        if (preferenceManager.getFechaDesfasada()) {
            getView().fechaDesfasada();
        }
    }

    /**
     * Descarga Maestros
     **/
    @Override
    public void sincronizarMaestros() {
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
                    if (preferenceManager.getNuevoServicio()) {
                        preferenceManager.setNuevoServicio(false);
                        preferenceManager.setServicioValidado(true);
                    }
                    preferenceManager.setValidaDescargaExitosa(true);
                    getView().showSuccessMessage("Descarga de maestros exitosa.");
                    validarFechaHora();
                    cargarCamposPreferencias();
                    listarUnidadesMedidas();
                    listarTurnos();
                    listarClasesTareo();
                })
                .subscribe(list -> {
                }, throwable -> {
                    Log.d("SETTINGPRESENTER", "error -> " + throwable);
                    if (isViewAttached()) {
                        getView().showErrorMessage("No se pudo descargar los maestros.", throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void validarCambios(boolean flag_tab_general, boolean flag_tab_ajustes, boolean flag_tab_tareo) {
        boolean flgNuevoServicio = false;

        //Validar si se cambio la webservice
        if (camposGeneral.getUrlServicioWeb() != null
                && !preferenceManager.getWebService().equalsIgnoreCase(camposGeneral.getUrlServicioWeb())) {
            String urlWebService = Constants.URL_SECURITY + camposGeneral.getUrlServicioWeb();
            if (TextUtil.validarURL(urlWebService)) {
                flgNuevoServicio = true;
            } else {
                getView().showWarningMessage(getView().getMessage(R.string.web_service_incorrecto));
            }
        }

        //Validar si se cambio otros parametros para guardarlos
        if (flag_tab_general || flag_tab_ajustes || flag_tab_tareo) {
            //Se guardan los campos nuevos, de modificarse la url se reiniciara
            guardarCampos(flgNuevoServicio, flag_tab_general, flag_tab_ajustes, flag_tab_tareo);
        } else {
            if (flgNuevoServicio) {
                getView().reiniciar();
            }
        }
    }

    @Override
    public void guardarCampos(boolean flag_url, boolean flag_tab_general,
                              boolean flag_tab_ajustes, boolean flag_tab_tareo) {
        preferenceManager.setSyncExitosa(SyncExitosa.EXITOSA);
        if (flag_tab_general) {
            if (camposGeneral.getModoTrabajo() > 0) {
                preferenceManager.setModoTrabajo(camposGeneral.getModoTrabajo());
            }
            preferenceManager.setFlgErrores(camposGeneral.isErroresDetallados());
            preferenceManager.setSonidoLectura(camposGeneral.isSound());
            preferenceManager.setValidaDescarga(camposGeneral.isValidaDescarga());
            preferenceManager.setMostrarModuloAsistencia(camposGeneral.isActiveModuloAsistencia());
            preferenceManager.setMostrarModuloIncidencia(camposGeneral.isActiveModuloIncidencia());
            preferenceManager.setMostrarModuloAcceso(camposGeneral.isActiveModuloAccceso());
            if (camposGeneral.getTimeOut() > 0) {
                preferenceManager.setTimeOut(camposGeneral.getTimeOut());
            }
            if (camposGeneral.getDuracionRefrigerio() > 0) {
                preferenceManager.setDuracionRefrigerio(camposGeneral.getDuracionRefrigerio());
            }
            if (camposGeneral.getMargenDiferencia() > 0) {
                preferenceManager.setMaximoMargen(camposGeneral.getMargenDiferencia());
            }
        }

        if (flag_tab_tareo) {
            preferenceManager.setTareoUnidadMedida(camposTareo.getUnidadMedida());
            preferenceManager.setTurno(camposTareo.getTurno());
            preferenceManager.setClaseTareo(camposTareo.getClaseTareo());
            /*if (camposTareo.getUnidadMedida() > 0) {
                int posicion = camposTareo.getUnidadMedida() - 1;
                preferenceManager.setTareoUnidadMedida(listaUnidadMedidas.get(posicion).getId());
            }
            if (camposTareo.getTurno() > 0) {
                int posicion = camposTareo.getTurno() - 1;
                preferenceManager.setTurno(listaTurnos.get(posicion).getId());
            }
            if (camposTareo.getClaseTareo() > 0) {
                int posicion = camposTareo.getClaseTareo() - 1;
                preferenceManager.setClaseTareo(listaClaseTareos.get(posicion).getId());
            }*/
        }

        if (flag_tab_ajustes) {
            preferenceManager.setAjustesUnidadMedida(camposAjustes.isUnidadMedida());
            preferenceManager.setFechaHoraInicioManual(camposAjustes.isFechaHoraInicio());
            preferenceManager.setFechaHoraFinManual(camposAjustes.isFechaHoraFin());
            preferenceManager.setVigenciaDescarga(camposAjustes.isVigenciaDescarga());
            preferenceManager.setRegistrarPersona(camposAjustes.isRegistrarEmpleado());
            preferenceManager.setRegistrarTareoNotEmployer(camposAjustes.isRegistrarTareoNotEmpleado());
            preferenceManager.setTimeWorker(camposAjustes.getTimeWorker());
        }

        if (flag_url) {
            preferenceManager.setWebService(camposGeneral.getUrlServicioWeb());
            preferenceManager.setNuevoServicio(true);
            //validamos verificar conexion
            getView().reiniciar();
        } else {
            getView().showSuccessMessage("Se guardaron los cambios.");
            getView().closed();
        }
    }

    @Override
    public void descargarMaestros(boolean flag_tab_general, boolean flag_tab_ajustes, boolean flag_tab_tareo) {
        //De modificarse la url se reiniciara despues de guardar
        validarCambios(flag_tab_general, flag_tab_ajustes, flag_tab_tareo);
        //Hacer la descarga
        getView().showProgressbar("Descarga", "Tablas maestras...");
        validarConexion();
    }

    public boolean guardarWebService(String urlWebService) {
        if (!preferenceManager.getWebService().equals(urlWebService)
                && TextUtil.validarURL(urlWebService)) {
            preferenceManager.setWebService(urlWebService);
            camposGeneral.setUrlServicioWeb(urlWebService);
            return true;
        }
        return false;
    }

    public void setCodigoUnidadMedida(int id) {
        camposTareo.setUnidadMedida(id);
    }

    public void setCodigoTurno(int id) {
        camposTareo.setTurno(id);
    }

    public void setSound(boolean checked) {
        camposGeneral.setSound(checked);
    }

    public void setValidaDescarga(boolean checked) {
        camposGeneral.setValidaDescarga(checked);
    }

    public void setMostrarModuloAsistencia(boolean checked) {
        camposGeneral.setActiveModuloAsistencia(checked);
    }

    public void setMostrarModuloIncidencia(boolean checked) {
        camposGeneral.setActiveModuloIncidencia(checked);
    }

    public void setMostrarModuloAcceso(boolean checked) {
        camposGeneral.setActiveModuloAccceso(checked);
    }

    public void setErroresDetallados(boolean checked) {
        camposGeneral.setErroresDetallados(checked);
    }

    public void setModoTrabajo(@ModoTrabajo int modo) {
        camposGeneral.setModoTrabajo(modo);
    }

    public void setRegistrarEmpleado(boolean checked) {
        camposAjustes.setRegistrarEmpleado(checked);
    }

    public void setRegistrarTareoNotEmpleado(boolean checked) {
        camposAjustes.setRegistrarTareoNotEmpleado(checked);
    }

    public void setTimeSendDataWorker(int time) {
        camposAjustes.setTimeWorker(time);
    }

    public void setMargenDiferencia(int margen) {
        camposGeneral.setMargenDiferencia(margen);
    }

    public void setAjustesUnidadMedida(boolean checked) {
        camposAjustes.setUnidadMedida(checked);
    }

    public void setAjustesFechaHoraInicio(boolean checked) {
        camposAjustes.setFechaHoraInicio(checked);
    }

    public void setAjustesFechaHoraFin(boolean checked) {
        camposAjustes.setFechaHoraFin(checked);
    }

    public void setAjustesVigenciaDescarga(boolean checked) {
        camposAjustes.setVigenciaDescarga(checked);
    }

    public void setClaseTareo(int pos) {
        camposTareo.setClaseTareo(pos);
    }

    public void setTimeOut(int seconds) {
        camposGeneral.setTimeOut(seconds);
    }

    public void setDuracionRefrigerio(int seconds) {
        camposGeneral.setDuracionRefrigerio(seconds);
    }
}