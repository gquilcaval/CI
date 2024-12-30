package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo;

import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_finalizar.IFinalizadoMasivoInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class FinalizarMasivoPresenter extends BasePresenter<IFinalizarMasivoContract.View>
        implements IFinalizarMasivoContract.Presenter {

    String TAG = FinalizarMasivoPresenter.class.getSimpleName();

    private String fechaFinTareo = "";
    private String horaFinTareo = "";
    private String fechaIniRefrigerio = "";
    private String fechaFinRefrigerio = "";

    @Inject
    PreferenceManager preferences;
    @Inject
    IFinalizadoMasivoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private ArrayList<String> listaCodTareos = new ArrayList<>();
    private ArrayList<String> listaFechaTareos = new ArrayList<>();
    private ArrayList<AllEmpleadoRow> listAllEmpleados = new ArrayList<>();

    private boolean isReubicar;

    @Inject
    FinalizarMasivoPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerAllEmpleados(ArrayList<String> listaCodTareos) {
        getCompositeDisposable().add(interactor.obtenerAllEmpleados(listaCodTareos,
                StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doFinally(() -> {
                    Log.e(TAG, "listAllEmpleados: " + listAllEmpleados);
                })
                .subscribe(listAllEmpleados -> {
                            this.listAllEmpleados = (ArrayList<AllEmpleadoRow>) listAllEmpleados;
                        }, error -> {
                            getView().showErrorMessage("Error", error.getMessage());
                        }
                ));
    }

    @Override
    public void finalizarTareo(boolean refrigerio) {
        boolean validaciones = true;
        if (refrigerio) {
            validaciones = validarRefrigerio();
        } else {
            fechaFinRefrigerio = "";
            fechaIniRefrigerio = "";
        }

        if (validaciones) {
            validaciones = validarFechaFin();
        }

        if (validaciones) {
            disposable = interactor.finalizarTareos(listaCodTareos, fechaFinTareo, horaFinTareo,
                    fechaIniRefrigerio, fechaFinRefrigerio)
                    .subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .doFinally(() -> {
                        if (isReubicar) {
                            getView().moveToReubicarAllEmpleados(listAllEmpleados);
                        }
                        getView().closeWindow();
                    })
                    .subscribe(flag -> {
                                getView().showSuccessMessage("Se finalizo el tareo exitosamente");
                                getView().closeWindow();
                            }, error -> {
                                //getView().getMessage(R.string.error_empleado_nomina)
                                getView().showErrorMessage("Error", error.getMessage());
                            }
                    );
        }
    }

    private boolean validarFechaFin() {
        String fechaHoraFinTareo = fechaFinTareo + " " + horaFinTareo;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaHoraFinTareo, Constants.F_LECTURA, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
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

        int rangoRefrigerio = DateUtil.validarRangoMinutos(Constants.F_DATE_TIME_TAREO,
                fechaIniRefrigerio, Constants.F_DATE_TIME_TAREO, fechaFinRefrigerio,
                preferences.getMinimoRefrigerio(), preferences.getMaximoRefrigerio());
        if (rangoRefrigerio == Constants.ERROR_MENOR_MINIMO) {
            getView().showWarningMessage(String.format("Refrigerio debe de ser mayor o igual a %s minutos ",
                    preferences.getMinimoRefrigerio()));
            return false;
        } else if (rangoRefrigerio == Constants.ERROR_MAYOR_MAXIMO) {
            String unidadTiempo = "minutos";
            int tiempo = preferences.getMaximoRefrigerio();

            if (tiempo > 60) {
                tiempo = DateUtil.minutesToHour(tiempo);
                unidadTiempo = "hora(s)";
            }
            getView().showWarningMessage(String.format("Refrigerio debe de ser menor o igual a %s %s",
                    tiempo, unidadTiempo));
            return false;
        }
        return true;
    }

    public boolean validarFechaFinTareo(Date date, String time) {
        String fechaFinTareo = DateUtil.longToStringFormat(date.getTime(), Constants.F_DATE_LECTURA) + " " + time;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
        }

        return true;
    }

    public boolean validarFechaFinTareo(String date, Date time) {
        String fechaFinTareo = date + " " + DateUtil.longToStringFormat(time.getTime(), Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
        }
        return true;
    }

    public boolean validarFechaInicioRefrigerio(Date fecha, String hora) {
        String fechaInicioRefrigerio = DateUtil.longToStringFormat(fecha.getTime(), Constants.F_DATE_LECTURA) + " " + hora;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    public boolean validarFechaInicioRefrigerio(String fecha, Date hora) {
        String fechaInicioRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(), Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    public boolean validarFechaFinRefrigerio(Date fecha, String hora) {
        String fechaFinRefrigerio = DateUtil.longToStringFormat(fecha.getTime(), Constants.F_DATE_LECTURA) + " " + hora;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    public boolean validarFechaFinRefrigerio(String fecha, Date hora) {
        String fechaFinRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(), Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio, Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    public void setFechaFinTareo(String fechaFinTareo) {
        this.fechaFinTareo = fechaFinTareo;
    }

    public void setHoraFinTareo(String horaFinTareo) {
        this.horaFinTareo = horaFinTareo;
    }

    public void setFechaIniRefrigerio(String fechaIniRefrigerio) {
        this.fechaIniRefrigerio = fechaIniRefrigerio;
    }

    public void setFechaFinRefrigerio(String fechaFinRefrigerio) {
        this.fechaFinRefrigerio = fechaFinRefrigerio;
    }

    public void setListaCodTareos(ArrayList<String> listaCodTareos) {
        this.listaCodTareos = listaCodTareos;
    }

    public void setListaFechaTareos(ArrayList<String> listaFechaTareos) {
        this.listaFechaTareos = listaFechaTareos;
    }

    public ArrayList<AllEmpleadoRow> getAllEmpleados() {
        return listAllEmpleados;
    }

    public void setIsReubicar(boolean isReubicar) {
        this.isReubicar = isReubicar;
    }

}
