package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_tareo;

import android.content.Context;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TabTareoPresenter extends BasePresenter<ITabTareoContract.View>
        implements ITabTareoContract.Presenter {

    String TAG = TabTareoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    Context context;
    @Inject
    IEmpleadosInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private String fechaInicioTareo;
    private String fechaFinRefrigerio;
    private String fechaIniRefrigerio;
    private String fechaFinTareo;
    private String horaFinTareo;
    private List<CodigoEmpleadoRow> trabajadoresPendientes;

    @Inject
    public TabTareoPresenter() {
        trabajadoresPendientes = new ArrayList<>();
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void finalizarEmpleado(String codigoTareo, String codigoEmpleado, boolean refrigerio) {
        boolean validaciones = true;
        if (refrigerio) {
            validaciones = validarRefrigerio();
        } else {
            fechaFinRefrigerio = "";
            fechaIniRefrigerio = "";
        }

        if (!validarFechaFin()) {
            validaciones = false;
        }
        if (!empleadoPendiente(codigoEmpleado)) {
            validaciones = false;
        }

        if (validaciones) {
            String horaRegistroSalida = DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_LECTURA);
            Boolean ultimo = (trabajadoresPendientes.size() == 1);
            disposable = interactor.finalizarTareoEmpleado(codigoTareo, codigoEmpleado, fechaFinTareo, horaFinTareo, refrigerio, fechaIniRefrigerio, fechaFinRefrigerio, ultimo, horaRegistroSalida)
                    .subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .subscribe(flag -> {
                                if (flag == 1) {
                                    getView().showSuccessMessage("Se finalizo el tareo exitosamente");
                                    quitarTrabajador(codigoEmpleado);
                                    if (ultimo) {
                                        getView().cerrar();
                                    }
                                } else {
                                    getView().showWarningMessage(getView().getMessage(R.string.error_inesperado));
                                }
                            },
                            error -> {
                                getView().showErrorMessage("Error", error.getMessage());
                            }
                    );
        }

    }

    private void quitarTrabajador(String codigoEmpleado) {
        for (int i = 0; i < trabajadoresPendientes.size(); i++) {
            CodigoEmpleadoRow empleado = trabajadoresPendientes.get(i);
            if (empleado.getCodigoEmpleado().equals(codigoEmpleado) || empleado.getNumeroDocumento().equals(codigoEmpleado)) {
                trabajadoresPendientes.remove(empleado);
                break;
            }
        }
    }

    private boolean empleadoPendiente(String codigoEmpleado) {
        boolean flag = false;
        for (CodigoEmpleadoRow empleado : trabajadoresPendientes) {
            Log.e(TAG, "empleado" + empleado);
            if (empleado.getCodigoEmpleado().equals(codigoEmpleado)
                    || empleado.getNumeroDocumento().equals(codigoEmpleado)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            getView().showWarningMessage(getView().getMessage(R.string.empleado_not_found));
        }
        return flag;
    }

    public void obtenerCodigoEmpleados(String codTareo) {
        Log.e(TAG, "obtenerCodigoEmpleados: ");
        disposable = interactor.obtenerCodigoEmpleados(codTareo,
                StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "obtenerCodigoEmpleados doOnComplete: ");
                })
                .subscribe(empleado -> {
                            Log.e(TAG, "obtenerCodigoEmpleados subscribe: " + empleado);
                            trabajadoresPendientes = empleado;
                            Log.e(TAG, "obtenerCodigoEmpleados subscribe : " + trabajadoresPendientes);
                        },
                        error -> {
                            Log.e(TAG, "obtenerCodigoEmpleados error: " + error);
                            Log.e(TAG, "obtenerCodigoEmpleados error: " + error.toString());
                            Log.e(TAG, "obtenerCodigoEmpleados error: " + error.getMessage());
                            Log.e(TAG, "obtenerCodigoEmpleados error: " + error.getLocalizedMessage());
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_bd), error.getMessage());
                        }
                );
    }

    public boolean validarFechaFinTareo(Date date, String time) {
        String fechaFinTareo = DateUtil.longToStringFormat(date.getTime(), Constants.F_DATE_LECTURA) + " " + time;
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
            return false;
        }
        return true;
    }

    public boolean validarFechaFinTareo(String date, Date time) {
        String fechaFinTareo = date + " " + DateUtil.longToStringFormat(time.getTime(), Constants.F_TIME_LECTURA);
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
            return false;
        }
        return true;
    }

    private boolean validarFechaFin() {
        String fechaHoraFinTareo = fechaFinTareo + " " + horaFinTareo;
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaHoraFinTareo, Constants.F_LECTURA, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
            return false;
        }
        return true;
    }

    public boolean validarFechaInicioRefrigerio(Date fecha, String hora) {
        String fechaInicioRefrigerio = DateUtil.longToStringFormat(fecha.getTime(), Constants.F_DATE_LECTURA) + " " + hora;
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
            return false;
        }
        return true;
    }

    public boolean validarFechaInicioRefrigerio(String fecha, Date hora) {
        String fechaInicioRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(), Constants.F_TIME_LECTURA);
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
            return false;
        }
        return true;
    }

    public boolean validarFechaFinRefrigerio(Date fecha, String hora) {
        String fechaFinRefrigerio = DateUtil.longToStringFormat(fecha.getTime(), Constants.F_DATE_LECTURA) + " " + hora;
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
            return false;
        }
        return true;
    }

    public boolean validarFechaFinRefrigerio(String fecha, Date hora) {
        String fechaFinRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(), Constants.F_TIME_LECTURA);
        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
            return false;
        }
        return true;
    }

    private boolean validarRefrigerio() {
        String fechaHoraFinTareo = fechaFinTareo + " " + horaFinTareo;
        if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, fechaFinRefrigerio, Constants.F_LECTURA, fechaIniRefrigerio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_refrig));
            return false;
        } else if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, fechaHoraFinTareo, Constants.F_DATE_TIME_TAREO, fechaFinRefrigerio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_tareo_menor_fin_refrig));
            return false;
        }

        int rangoRefrigerio = DateUtil.validarRangoMinutos(Constants.F_DATE_TIME_TAREO, fechaIniRefrigerio, Constants.F_DATE_TIME_TAREO, fechaFinRefrigerio, preferences.getMinimoRefrigerio(), preferences.getMaximoRefrigerio());
        if (rangoRefrigerio == Constants.ERROR_MENOR_MINIMO) {
            getView().showWarningMessage("Refrigerio debe de ser mayor o igual a " + preferences.getMinimoRefrigerio() + " minutos ");
            return false;
        } else if (rangoRefrigerio == Constants.ERROR_MAYOR_MAXIMO) {
            String unidadTiempo = "minutos";
            int tiempo = preferences.getMaximoRefrigerio();

            if (tiempo > 60) {
                tiempo = DateUtil.minutesToHour(tiempo);
                unidadTiempo = "hora(s)";
            }
            getView().showWarningMessage("Refrigerio debe de ser menor o igual a " + tiempo + " " + unidadTiempo);
            return false;
        }
        return true;
    }

    public void setFechaFinRefrigerio(String fechaFinRefrigerio) {
        this.fechaFinRefrigerio = fechaFinRefrigerio;
    }

    public void setFechaIniRefrigerio(String fechaIniRefrigerio) {
        this.fechaIniRefrigerio = fechaIniRefrigerio;
    }

    /* "yyyyMMdd HH:mm:ss" */
    public void setFechaFinTareo(String fechaFinTareo) {
        this.fechaFinTareo = fechaFinTareo;
    }

    public void setHoraFinTareo(String horaFinTareo) {
        this.horaFinTareo = horaFinTareo;
    }

    public void camposRequeridos(String fechaInicioTareo) {
        this.fechaInicioTareo = fechaInicioTareo;
    }
}
