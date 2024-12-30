package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorEmpleado;

import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.ResultadoPorEmpleadoModificado;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_resultado.IResultadoInteractor;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.util.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ResultadoPorEmpleadoPresenter extends BasePresenter<IResultadoPorEmpleadoContract.View>
        implements IResultadoPorEmpleadoContract.Presenter {

    String TAG = ResultadoPorEmpleadoPresenter.class.getSimpleName();
    @Inject
    PreferenceManager preferences;
    @Inject
    IResultadoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    DateTimeManager appDateTime;
    private ResultadoTareo nuevo;
    private String codigoTareo = null;

    @Inject
    ResultadoPorEmpleadoPresenter() {
        nuevo = new ResultadoTareo();
        nuevo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
        //TODO getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    public void obtenerEmpleados() {
        Log.e(TAG, "obtenerEmpleados: ");
        getCompositeDisposable().add(interactor.obtenerResultadoEmpleados(codigoTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleados -> {
                            Log.e(TAG, "obtenerEmpleados subscribe: ");
                            getView().mostrarEmpleados(empleados);
                        }, error -> {
                            Log.e(TAG, "obtenerEmpleados error: " + error);
                            Log.e(TAG, "obtenerEmpleados error: " + error.toString());
                            Log.e(TAG, "obtenerEmpleados error: " + error.getMessage());
                            Log.e(TAG, "obtenerEmpleados error: " + error.getLocalizedMessage());
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

    @Override
    public void guardarResultado(double cantidad, String codTareo,
                                 @ResultadoPorEmpleadoModificado int result) {
        switch (result) {
            case ResultadoPorEmpleadoModificado.NUEVO:
                String codResultado = nuevo.getFkDetalleTareo() + appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO);
                nuevo.setCodResultado(codResultado);
                nuevo.setCantidad(cantidad);
                nuevo.setFkUsuarioInsert(preferences.getUsuario());
                nuevo.setFechaRegistro(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));
                nuevo.setHoraRegistro(appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO));//TODO FORMATO?

                String json1 = new Gson().toJson(nuevo);
                Log.e(TAG, "json1: " + json1);
                getCompositeDisposable().add(interactor.guardarResultadoEmpleado(nuevo, codTareo)
                        .subscribeOn(ExecutorThread)
                        .observeOn(UiThread)
                        .subscribe(() -> {
                                    nuevo.setFkDetalleTareo("");
                                    nuevo.setCantidad(0);
                                    nuevo.setCodResultado("");
                                    getView().showSuccessMessage("Se guardo el resultado");
                                },
                                error -> {
                                    getView().showErrorMessage("Error al intentar guardar el resultado", error.getMessage());
                                }));
                break;
            case ResultadoPorEmpleadoModificado.MODIFICADO:
                nuevo.setFechaModificacion(appDateTime.getFechaSincronizada(Constants.F_DATE_TIME_TAREO));
                nuevo.setCantidad(cantidad);
                nuevo.setFkUsuarioUpdate(preferences.getUsuario());

                String json2 = new Gson().toJson(nuevo);
                Log.e(TAG, "json2: " + json2);

                getCompositeDisposable().add(interactor.modificarResultadoEmpleado(nuevo, codTareo)
                        .subscribeOn(ExecutorThread)
                        .observeOn(UiThread)
                        .subscribe(() -> {
                                    nuevo.setFkDetalleTareo("");
                                    nuevo.setCantidad(0);
                                    nuevo.setCodResultado("");
                                    getView().showSuccessMessage("Se guardo el resultado");
                                },
                                error -> {
                                    getView().showErrorMessage("Error al intentar guardar el resultado", error.getMessage());
                                }));
                break;
        }
    }

    public void validarEmpleados(String codEmpleado, String codigoTareo) {
        getCompositeDisposable().add(interactor.validarEmpleado(codigoTareo, codEmpleado)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleado -> {
                            if (empleado.getDetalleTareo() != null) {
                                nuevo.setFkDetalleTareo(empleado.getDetalleTareo());
                                if (empleado.getCantidadProducida() > 0) {
                                    getView().mostrarMensajeEmpleadoConResultado(empleado.getCantidadProducida(),
                                            ResultadoPorEmpleadoModificado.MODIFICADO);
                                } else {
                                    getView().mostrarDialogoResultado(empleado.getCantidadProducida(),
                                            ResultadoPorEmpleadoModificado.NUEVO);
                                }
                            } else {
                                getView().showWarningMessage(getView().getMessage(R.string.empleado_noencontrado_tareo));
                            }
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.empleado_noencontrado_tareo),
                                    error.getMessage());
                        }
                ));
    }

    public void liquidarTareo(String codigoTareo) {
        //"El tareo no se encuentra finalizado."
        getCompositeDisposable().add(interactor.liquidarTareo(codigoTareo,
                appDateTime.getFechaSincronizada(Constants.F_DATE_TIME_TAREO),
                preferences.getUsuario())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            getView().showSuccessMessage("El tareo se liquidó correctamente");
                            getView().salir();
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.empleado_noencontrado_tareo),
                                    error.getMessage());
                        }
                ));

    }

    public void setCodigoTareo(String codigoTareo) {
        this.codigoTareo = codigoTareo;
    }

    //TODO Insercion_Masiva_Resultados
    /*
    * UPDATE TAREOS SET DEC_CANTIDADPRODUCIDA=DEC_CANTIDADPRODUCIDA + ? , ")
                sSQL.Append(" INT_FLGESTADOREGISTRO= CASE INT_FLGESTADOREGISTRO  WHEN 0 THEN 2 ELSE INT_FLGESTADOREGISTRO END ,")
                sSQL.Append(" INT_IDENTIFICADORUSUARIO_UPDATE=?,VCH_FECHAMODIFICACION=? ")
                sSQL.Append(" WHERE VCH_IDENTIFICADORTAREO=?
    * */
}
