package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.opciones;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.SelectTurno;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_opciones.IOpcionesInteractor;
import com.dms.tareosoft.presentation.dialog.DatePickerFragment;
import com.dms.tareosoft.presentation.dialog.TimePickerFragment;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class OpcNewTareoNotEmployerPresenter extends BasePresenter<IOpcNewTareoNotEmployerContract.View>
        implements IOpcNewTareoNotEmployerContract.Presenter {
    private final String TAG = OpcNewTareoNotEmployerPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    IOpcionesInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private OpcionesTareo mOpcionesTareo;
    private List<Turno> listaTurnos;

    private Calendar calendar;

    @Inject
    public OpcNewTareoNotEmployerPresenter() {
        calendar = Calendar.getInstance(Locale.getDefault());
        if (mOpcionesTareo == null)
            mOpcionesTareo = new OpcionesTareo();
        if (listaTurnos == null)
            listaTurnos = new ArrayList<>();
    }

    @Override
    public void obtenerTurnos() {
        disposable = interactor.listarTurnos()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(resultado -> {
                            listaTurnos = resultado;
                            int posicion = 0;
                            Log.e(TAG, "preferenceManager.getTurno(): " + preferenceManager.getTurno());
                            Turno turno = new Turno();
                            turno.setId(0);
                            turno.setDescripcion(getView().getMessage(R.string.seleccione));

                            if (preferenceManager.getTurno() > 0) {
                                posicion = preferenceManager.getTurno() - 1;
                                preferenceManager.setTurno(listaTurnos.get(posicion).getId());
                            }

                            listaTurnos.add(0, turno);

                            getView().llenarSpinnerTurno(listaTurnos, posicion);
                        }, error -> {
                            Log.e(TAG, "obtenerTurnos error: " + error);
                            Log.e(TAG, "obtenerTurnos error: " + error.toString());
                            Log.e(TAG, "obtenerTurnos error: " + error.getMessage());
                            Log.e(TAG, "obtenerTurnos error: " + error.getLocalizedMessage());
                            getView().showErrorMessage("No se pudo obtener las unidades", error.getMessage());
                        }
                );
    }

    @Override
    public void obtenerUnidadesMedidas() {
        disposable = interactor.listarUnidadMedidas()
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(list -> {
                            List<UnidadMedida> resultado = new ArrayList<>();
                            resultado.add(hintValue());
                            for (UnidadMedida item : list) {
                                resultado.add(item);
                            }
                            getView().listarUnidadMedida(resultado,
                                    preferenceManager.getTareoUnidadMedida());
                        }, error -> {
                            getView().showErrorMessage("No se pudo obtener las unidades", error.getMessage());
                        }
                );
    }

    @Override
    public String getFechaInicio() {
        /*setFechaInicio(DateUtil.dateToStringFormat(calendar.getTime(),
                Constants.F_DATE_TAREO));
        return DateUtil.dateToStringFormat(calendar.getTime(),
                Constants.F_DATE_LECTURA);*/
        setFechaInicio(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));
        return appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA);
    }

    @Override
    public String getHoraInicio() {
        /*setHoraInicio(DateUtil.dateToStringFormat(calendar.getTime(),
                Constants.F_TIME_LECTURA));
        return DateUtil.dateToStringFormat(calendar.getTime(),
                Constants.F_TIME_LECTURA);*/
        setHoraInicio(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));
        return appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA);
    }

    @Override
    public boolean validarFechaInicio(Date dateTime) {
        int cantDias = preferenceManager.getCantDiasFechaIniTareo();
        if (DateUtil.obtenerRestoFechaHora(cantDias) >= dateTime.getTime()) {
            String mensaje = getView().getMessage(R.string.fecha_menor).replace("x", "" + cantDias);
            getView().showWarningMessage(mensaje);
            return false;
        }
        return true;
    }

    public UnidadMedida hintValue() {
        UnidadMedida hint = new UnidadMedida(0,
                getView().getMessage(R.string.seleccione));
        return hint;
    }

    @Override
    public void showDatePickerDialog(FragmentManager fragment, final EditText editText,
                                     Long dateTime) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.e(TAG, "year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String date = DateUtil.dateToStringFormat(calendar.getTime(),
                                Constants.F_DATE_LECTURA);
                        setFechaInicio(DateUtil.dateToStringFormat(calendar.getTime(),
                                Constants.F_DATE_TAREO));
                        Log.e(TAG, "date: " + date);
                        editText.setText(date);
                    }
                }, dateTime);
        newFragment.show(fragment, DatePickerFragment.TAG);
    }

    @Override
    public void showTimePickerDialog(FragmentManager fragment, final EditText editText) {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.e(TAG, "hourOfDay: " + hourOfDay + ", minute: " + minute);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String time = DateUtil.dateToStringFormat(calendar.getTime(),
                                Constants.F_TIME_TAREO);

                        Log.e(TAG, "date: " + time);
                        editText.setText(time);
                        setHoraInicio(time);
                    }
                });
        timePickerFragment.show(fragment, DatePickerFragment.TAG);
    }

    @Override
    public void selectTurnoSegunHora(Spinner spinner) {
        Log.e(TAG, "selectTurnoSegunHora: ");
        String ini1 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "080000";
        String fin1 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "160000";
        String ini2 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "130000";
        String fin2 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "180000";
        String ini3 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "220000";
        String fin3 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "060000";
        String ini4 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "000000";
        String fin4 = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO) + "063000";
        String currentDatetime = appDateTime.getFechaSincronizada(DateUtil.dateToStringFormat(calendar.getTime(),
                Constants.F_DATE_RESULTADO));
        Log.e(TAG, "selectTurnoSegunHora currentDatetime: " + currentDatetime);

        if (DateUtil.ComprobarFechas(currentDatetime, ini1, fin1)) {
            Log.e(TAG, "selectTurnoSegunHora primer turno hora actual: " + currentDatetime + ", inicia: " + ini1 + ", finaliza: " + fin1);
            int pos = getIdTurno(SelectTurno.PRIMERO);
            mOpcionesTareo.setCodigoTurno(pos);
            spinner.setSelection(pos);
        } else if (DateUtil.ComprobarFechas(currentDatetime, ini2, fin2)) {
            Log.e(TAG, "selectTurnoSegunHora segundo turno actual: " + currentDatetime + ",inicia: " + ini2 + ", finaliza: " + fin2);
            int pos = getIdTurno(SelectTurno.SEGUNDO);
            mOpcionesTareo.setCodigoTurno(pos);
            spinner.setSelection(pos);
        } else if (DateUtil.ComprobarFechas(currentDatetime, ini3, fin3)) {
            Log.e(TAG, "selectTurnoSegunHora segundo turno actual: " + currentDatetime + ",inicia: " + ini3 + ", finaliza: " + fin3);
            int pos = getIdTurno(SelectTurno.TERCERO);
            mOpcionesTareo.setCodigoTurno(pos);
            spinner.setSelection(pos);
        } else if (DateUtil.ComprobarFechas(currentDatetime, ini4, fin4)) {
            Log.e(TAG, "selectTurnoSegunHora segundo turno actual: " + currentDatetime + ",inicia: " + ini4 + ", finaliza: " + fin4);
            int pos = getIdTurno(SelectTurno.CUARTO);
            mOpcionesTareo.setCodigoTurno(pos);
            spinner.setSelection(pos);
        }
    }

    @Override
    public void setFechaInicio(String fechaInicio) {
        mOpcionesTareo.setFechaInicio(fechaInicio);
    }

    @Override
    public void setHoraInicio(String horaInicio) {
        mOpcionesTareo.setHoraInicio(horaInicio);
    }

    @Override
    public void setCodigoTurno(int codigoTurno) {
        mOpcionesTareo.setCodigoTurno(codigoTurno);
    }

    @Override
    public void setTipoTareo(int tipoTareo) {
        Log.e(TAG, "setTipoTareo tipoTareo: " + tipoTareo);
        mOpcionesTareo.setTipoTareo(tipoTareo);
    }

    @Override
    public void setTipoResultado(int tipoResultado) {
        Log.e(TAG, "setTipoResultado tipoResultado: " + tipoResultado);
        mOpcionesTareo.setTipoResultado(tipoResultado);
    }

    @Override
    public void setCodigoUnidadMedida(int codigoUnidadMedida) {
        mOpcionesTareo.setCodigoUnidadMedida(codigoUnidadMedida);
    }

    @Override
    public OpcionesTareo getOpcionesTareo() {
        return mOpcionesTareo;
    }

    private int getIdTurno(@SelectTurno int idTurno) {
        Log.e(TAG, "getIdTurno idTurno: " + idTurno);
        int id = 0;
        for (int i = 0; i < listaTurnos.size(); i++) {
            if (listaTurnos.get(i).getId() == idTurno) {
                id = i;
                break;
            }
        }
        Log.e(TAG, "getIdTurno pos: " + id);
        return id;
    }
}
