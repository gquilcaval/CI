package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_finalizar;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ReubicarTipoEmpleadoFinalizarPresenter extends BasePresenter<IReubicarTipoEmpleadoFinalizarContract.View>
        implements IReubicarTipoEmpleadoFinalizarContract.Presenter {

    String TAG = ReubicarTipoEmpleadoFinalizarPresenter.class.getSimpleName();

    private List<String> listaFechaTareos = new ArrayList<>();

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    ReubicarTipoEmpleadoFinalizarPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void validarCampos() {
        if (!preferences.getFechaHoraInicioManual()) {
            getView().ocultarTodo();
        } else {
            getView().mostrarTodo();
        }
    }

    @Override
    public boolean validarFechaFinTareo(Date date, String time) {
        String fechaFinTareo = DateUtil.longToStringFormat(date.getTime(),
                Constants.F_DATE_LECTURA) + " " + time;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean validarFechaFinTareo(String date, Date time) {
        String fechaFinTareo = date + " " + DateUtil.longToStringFormat(time.getTime(), Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinTareo,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarFechaInicioRefrigerio(Date fecha, String hora) {
        String fechaInicioRefrigerio = DateUtil.longToStringFormat(fecha.getTime(), Constants.F_DATE_LECTURA) + " " + hora;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarFechaInicioRefrigerio(String fecha, Date hora) {
        String fechaInicioRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(), Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioRefrigerio,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.ini_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarFechaFinRefrigerio(Date fecha, String hora) {
        String fechaFinRefrigerio = DateUtil.longToStringFormat(fecha.getTime(),
                Constants.F_DATE_LECTURA) + " " + hora;
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarFechaFinRefrigerio(String fecha, Date hora) {
        String fechaFinRefrigerio = fecha + " " + DateUtil.longToStringFormat(hora.getTime(),
                Constants.F_TIME_LECTURA);
        for (String fechaInicioTareo : listaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaFinRefrigerio,
                    Constants.F_DATE_TIME_TAREO, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_tareo));
                return false;
            }
        }
        return true;
    }

    public void setListaFechaTareos(List<String> listaFechaTareos) {
        this.listaFechaTareos = listaFechaTareos;
    }
}
