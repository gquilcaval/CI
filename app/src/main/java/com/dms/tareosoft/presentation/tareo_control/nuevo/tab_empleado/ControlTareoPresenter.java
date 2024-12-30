package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_empleado;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_control.IControlTareoInteractor;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.util.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ControlTareoPresenter extends BasePresenter<IControlTareoContract.View>
        implements IControlTareoContract.Presenter {

    String TAG = ControlTareoPresenter.class.getSimpleName();

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferences;
    @Inject
    IControlTareoInteractor interactor;

    @Inject
    public ControlTareoPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void guardarControl(EmpleadoControlRow empleado, String codigoTareo) {
        Log.e(TAG, "guardarControl empleado: " + empleado + ", codigoTareo: " + codigoTareo);
        DetalleTareoControl nuevo = new DetalleTareoControl();
        nuevo.setFkEmpleado(empleado.getIdEmpleado());
        nuevo.setCodEmpleado(empleado.getCodigoEmpleado());
        nuevo.setFkTareo(codigoTareo);
        nuevo.setFkUsuarioInsert(preferences.getUsuario());
        nuevo.setFechaControl(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));
        nuevo.setHoraControl(appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO));
        nuevo.setFkUsuarioInsert(preferences.getUsuario());
        nuevo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);

        String json = new Gson().toJson(nuevo);
        Log.e(TAG, "guardarControl json: " + json);

        disposable = interactor.guardarControl(nuevo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            Log.e(TAG, "guardarControl subscribe: ");
                            //TODO  'ACTUALIZA DATOS DEL EMPLEADO
                            getView().showSuccessMessage("Se añadio la verificacion");
                        },
                        error -> {
                            //getView().getMessage(R.string.error_empleado_nomina)
                            Log.e(TAG, "guardarControl error: " + error);
                            Log.e(TAG, "guardarControl error: " + error.toString());
                            Log.e(TAG, "guardarControl error: " + error.getMessage());
                            Log.e(TAG, "guardarControl error: " + error.getLocalizedMessage());
                            getView().showErrorMessage("Error", error.getMessage());
                        }
                );

    }

    @Override
    public void validarTrabajador(String codTareo, String codEmpleado) {
        Log.e(TAG, "validarTrabajador codTareo: " + codTareo + ", codEmpleado: " + codEmpleado);
        getCompositeDisposable().add(interactor.validarEmpleadoTareo(codEmpleado, codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "validarTrabajador doOnComplete");
                    getView().showErrorMessage(getView().getMessage(R.string.empleado_noencontrado_tareo), null);
                }).subscribe(empleado -> {
                            Log.e(TAG, "validarTrabajador subscribe: " + empleado);
                            if (!TextUtils.isEmpty(empleado.getCodigoEmpleado())) {
                                if (empleado.getControles() > preferences.getLimiteControles()) {
                                    getView().showWarningMessage("Alcanzó el máximo de verificaciones");
                                } else {
                                    guardarControl(empleado, codTareo);
                                }
                            } else {
                                getView().showWarningMessage(getView().getMessage(R.string.empleado_noencontrado_tareo));
                            }
                        }, error -> {
                            Log.e(TAG, "validarTrabajador error" + error);
                            Log.e(TAG, "validarTrabajador error" + error.toString());
                            Log.e(TAG, "validarTrabajador error" + error.getMessage());
                            Log.e(TAG, "validarTrabajador error" + error.getLocalizedMessage());
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_tareo), error.getMessage());
                        }
                ));
    }

    @Override
    public void obtenerControles(String codTareo) {
        Log.e(TAG, "obtenerControles codTareo: " + codTareo);
        getCompositeDisposable().add(interactor.obtenerControlEmpleados(codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleadoRows -> {
                    Log.e(TAG, "obtenerControles subscribe" + empleadoRows);
                    getView().mostrarEmpleados(empleadoRows);
                }, error -> {
                    Log.e(TAG, "obtenerControles error" + error);
                    Log.e(TAG, "obtenerControles error" + error.toString());
                    Log.e(TAG, "obtenerControles error" + error.getMessage());
                    Log.e(TAG, "obtenerControles error" + error.getLocalizedMessage());
                    getView().showErrorMessage(getView().getMessage(R.string.error_empleado_tareo), error.getMessage());
                })
        );
    }
}
