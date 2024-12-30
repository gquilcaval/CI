package com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;
import com.dms.tareosoft.presentation.dialog.DatePickerFragment;
import com.dms.tareosoft.presentation.dialog.TimePickerFragment;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class EmpleadoPresenter extends BasePresenter<IEmpleadoContract.View>
        implements IEmpleadoContract.Presenter {

    String TAG = EmpleadoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
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

    private List<DetalleTareo> mDetalleTareos;
    private String mFechaInicio, mHoraInicio;
    private String mDateTareo = null;
    private int mIndex = -1;
    private Calendar calendar;

    @Inject
    public EmpleadoPresenter() {
        mDetalleTareos = new ArrayList<>();
        calendar = Calendar.getInstance(Locale.getDefault());
    }

    @Override
    public void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.e(TAG, "year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = DateUtil.dateToStringFormat(calendar.getTime(),
                        Constants.F_DATE_LECTURA);
                Log.e(TAG, "date: " + date);
                editText.setText(date);
            }
        }, dateTime);
        newFragment.show(fragment, DatePickerFragment.TAG);
    }

    @Override
    public void showTimePickerDialog(Fragment fragment, final EditText editText) {
        TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.e(TAG, "hourOfDay: " + hourOfDay + ", minute: " + minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String time = DateUtil.dateToStringFormat(calendar.getTime(),
                        Constants.F_TIME_LECTURA);
                Log.e(TAG, "date: " + time);
                editText.setText(time);
            }
        });
        timePickerFragment.show(fragment.getFragmentManager(), DatePickerFragment.TAG);
    }

    @Override
    public void evaluarEmpleado(String codEmpleado, String tipoPlanilla,
                                String fecha, String hora) {
        Log.e(TAG, "evaluarEmpleado codEmpleado : " + codEmpleado + ", tipoPlanilla: " + tipoPlanilla +
                ", fecha: " + fecha + ", hora: " + hora);
        if (preferences.getSonidoLectura()) {
            getView().playSound();
        }
        if (TextUtils.isEmpty(tipoPlanilla)) {
            getView().showErrorMessage(getView().getMessage(R.string.clase_erronea), null);
        } else {
            if (!empleadoEnLista(codEmpleado, fecha, hora)) {
                //Si se permite registrar personas solo con codigo
                if (preferences.getRegistrarPersona()) {
                    getCompositeDisposable().add(interactor.validarExistenciaEmpleado(codEmpleado)
                            .subscribeOn(ExecutorThread)
                            .observeOn(UiThread)
                            .doOnComplete(() -> {
                                Log.e(TAG, "evaluarEmpleado doOnComplete : " + preferences.getRegistrarPersona());
                                if (preferences.getRegistrarPersona()) {
                                    getCompositeDisposable().add(
                                            interactor.validarExistenciaEmpleadoporDni(codEmpleado,
                                                    StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO)
                                                    .subscribeOn(ExecutorThread)
                                                    .observeOn(UiThread)
                                                    .doOnComplete(() -> {
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni doOnComplete: ");
                                                       /* Empleado e = new Empleado();
                                                        e.setCodigoEmpleado(codEmpleado);
                                                        getView().mostrarMensajeRegistroConfirmacion(e, fecha, hora);*/
                                                        registrarEmpleadoTareoPorDni(codEmpleado, fecha, hora);

                                                    })
                                                    .subscribe(empleado -> {
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni subscribe: " + empleado);
                                                        if (empleado != null)
                                                            getView().showWarningMessage(getView().getMessage(R.string.empleado_ocupado));
                                                    }, error -> {
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni error : " + error);
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni error : " + error.toString());
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni error : " + error.getMessage());
                                                        Log.e(TAG, "evaluarEmpleado validarExistenciaEmpleadoporDni error : " + error.getLocalizedMessage());
                                                        getView().showErrorMessage("No se pudo verificar el estado del empleado",
                                                                error.getMessage());
                                                    }));
                                } else {
                                    getView().showWarningMessage("No se puede agregar empleados, " +
                                            "revise su configuracion");
                                }
                            })
                            .subscribe(empleado -> {
                                Log.e(TAG, "evaluarEmpleado subscribe : " + empleado);


                                        if (empleado.getTipoPlanilla().equals(tipoPlanilla)) {
                                            if (!empleado.getMensaje().toUpperCase(Locale.ROOT).equals("ACTIVO")) {
                                                getView().mostrarMensajeRegistroConfirmacion(empleado, fecha, hora);
                                            } else {
                                                registrarEmpleadoTareo(empleado, fecha, hora);
                                            }

                                        } else {
                                            getView().showWarningMessage(getView().getMessage(R.string.empleado_no_encontrado_nomina));
                                        }



                            }, error -> {
                                Log.e(TAG, "evaluarEmpleado error : " + error);
                                Log.e(TAG, "evaluarEmpleado error : " + error.toString());
                                Log.e(TAG, "evaluarEmpleado error : " + error.getMessage());
                                Log.e(TAG, "evaluarEmpleado error : " + error.getLocalizedMessage());
                                getView().showErrorMessage(getView().getMessage(R.string.error_empleado_bd),
                                        error.getMessage());
                            }));
                } else {
                    validarEmpleadoNomina(codEmpleado, tipoPlanilla, fecha, hora);
                }
            }
        }
    }

    @Override
    public void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora) {
        Log.e(TAG, "registrarEmpleadoTareo: " + empleado);
        if (empleado.getEstadoEmpleado() == StatusEmpleado.EMPLEADO_NO_LIBRE) {
            getView().showWarningMessage(
                    getView().getMessage(R.string.empleado_ocupado));
        } else {
            DetalleTareo nuevoDetalle = new DetalleTareo();
            nuevoDetalle.setFlgEstadoIniTareo(StatusIniDetalleTareo.TAREO_DETALLE_INICIADO);


            nuevoDetalle.setFechaIngreso(DateUtil.changeStringDateTimeFormat(fecha,
                    Constants.F_DATE_TAREO, Constants.F_DATE_TAREO));
            nuevoDetalle.setHoraIngreso(hora);

            nuevoDetalle.setCodigoEmpleado(empleado.getCodigoEmpleado());
            nuevoDetalle.setFkEmpleado(empleado.getId());

            //En los registros de auditoria se registraran tal cual la fecha del equipo
            nuevoDetalle.setFechaRegistro(DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO));
            nuevoDetalle.setHoraRegistroInicio(DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_TAREO));

            mDetalleTareos.add(nuevoDetalle);

            EmpleadoRow nuevaFila = new EmpleadoRow();
            nuevaFila.setCodigoEmpleado(empleado.getCodigoEmpleado());
            nuevaFila.setEmpleado(String.format("%s %s %s",
                    empleado.getNombres(),
                    empleado.getApellidoPaterno(),
                    empleado.getApellidoMaterno()));
            nuevaFila.setFechaHoraIngreso(nuevoDetalle.getFechaIngreso() + " " + nuevoDetalle.getHoraIngreso());
            nuevaFila.setSupervisor(empleado.getSupervisor());

            getView().agregarEmpleado(nuevaFila);

            getView().reiniciarFechaInicio();
            getView().reiniciarHoraInicio();

            getView().showSuccessMessage(getView().getMessage(R.string.empleado_registrado));
            getView().showWarningMessage(empleado.getMensaje());
        }
    }

    @Override
    public List<DetalleTareo> obtenerEmpleados() {
        return mDetalleTareos;
    }

    @Override
    public boolean validarFecha(Date date, String hora) {
        String fechaInicioEmpleado = DateUtil.dateToStringFormat(date, Constants.F_DATE_TAREO) + " " + hora;

        if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, fechaInicioEmpleado,
                Constants.F_DATE_TIME_TAREO,
                mFechaInicio + " " + mHoraInicio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fecha_empleado_menor_fecha_tareo));
            return false;
        }
        return true;
    }

    @Override
    public boolean validarHora(String fecha, Date hora) {
        String fechaInicioEmpleado = fecha + " " + DateUtil.dateToStringFormat(hora, Constants.F_DATE_TIME_TAREO);

        if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, fechaInicioEmpleado,
                Constants.F_DATE_TIME_TAREO,
                mFechaInicio + " " + mHoraInicio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fecha_empleado_menor_fecha_tareo));
            return false;
        }
        return true;
    }

    @Override
    public void setFechaInicio(String fechaInicio) {
        mFechaInicio = fechaInicio;
        Log.e(TAG, "mFechaInicio : " + mFechaInicio);
    }

    @Override
    public void setHoraInicio(String horaInicio) {
        mHoraInicio = horaInicio;
        Log.e(TAG, "mHoraInicio : " + mHoraInicio);
    }

    @Override
    public void validarVista() {
        if (!preferences.getFechaHoraInicioManual()) {
            getView().ocultarTodo();
        } else {
            getView().mostrarTodo();
        }
    }

    @Override
    public boolean isRegisterNotEmployer() {
        return preferences.getContenidoAjustes().isRegistrarTareoNotEmpleado();
    }

    private boolean empleadoEnLista(String codEmpleado, String fecha, String hora) {
        Log.e(TAG, "empleadoEnLista codEmpleado: " + codEmpleado + ", fecha: " + fecha + ", hora: " + hora);
        String dateCurrent = DateUtil.setDateFormat(fecha + " " + hora,
                Constants.F_DATE_TIME_TAREO,
                Constants.F_DATE_RESULTADO);
        if (existEmpleadoEnLista(codEmpleado)) {
            Log.e(TAG, "empleadoEnLista dateCurrent: " + dateCurrent);
            Log.e(TAG, "empleadoEnLista mIndex: " + mIndex);
            Log.e(TAG, "empleadoEnLista mDateTareo: " + mDateTareo);
            String finalHora = null;
            Calendar calendar;
            if (!dateCurrent.isEmpty() && !mDateTareo.isEmpty()) {

                try {
                    String dateIni = DateUtil.setDateFormat(mDateTareo,
                            Constants.F_LECTURA,
                            Constants.F_DATE_RESULTADO);
                    calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTime(DateUtil.fromStringToDate(dateIni,
                            new SimpleDateFormat(Constants.F_DATE_RESULTADO)));
                    calendar.add(Calendar.MINUTE, 5);
                    finalHora = new SimpleDateFormat(Constants.F_DATE_RESULTADO).format(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "empleadoEnLista finalHora: " + finalHora);

            if (!DateUtil.ComprobarFechas(dateCurrent,
                    DateUtil.setDateFormat(mDateTareo,
                            Constants.F_LECTURA,
                            Constants.F_DATE_RESULTADO),
                    finalHora)) {
                getView().showWarningMessage(getView().getMessage(R.string.message_out_hour));
            } else {
                getView().confirmDialog(getView().getMessage(R.string.title_confirmar),
                        getView().getMessage(R.string.confirmar_quitar_empleado),
                        () -> {
                            mDetalleTareos.remove(mIndex);
                            getView().quitarEmpleado(mIndex);
                            getView().showSuccessMessage(getView().getMessage(R.string.empleado_retirado));
                        });
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean existEmpleadoEnLista(String codEmpleado) {
        boolean resultado = false;
        mDateTareo = null;
        mIndex = -1;
        if (mDetalleTareos != null && mDetalleTareos.size() > 0) {
            for (int i = 0; i < mDetalleTareos.size(); i++) {
                if (mDetalleTareos.get(i).getCodigoEmpleado().equals(codEmpleado)) {
                    Log.e(TAG, "mDetalleTareos.get(i): " + mDetalleTareos.get(i));
                    Log.e(TAG, "mDetalleTareos.get(i).getHoraIngreso(): " + mDetalleTareos.get(i).getHoraIngreso());
                    Log.e(TAG, "mDetalleTareos.get(i).getFechaIngreso(): " + mDetalleTareos.get(i).getFechaIngreso());
                    resultado = true;
                    mIndex = i;
                    mDateTareo = DateUtil.changeStringDateTimeFormat(String.format("%s %s",
                            mDetalleTareos.get(i).getFechaIngreso(),
                            mDetalleTareos.get(i).getHoraIngreso()),
                            Constants.F_DATE_TIME_TAREO,
                            Constants.F_LECTURA);
                    Log.e(TAG, "existEmpleadoEnLista: " + resultado);
                    Log.e(TAG, "existEmpleadoEnLista mDateTareo: " + mDateTareo);
                    break;
                }
            }
        } else {
            resultado = false;
            Log.e(TAG, "existEmpleadoEnLista: " + resultado);
        }
        return resultado;
    }

    public void registrarEmpleadoTareoPorDni(String codEmpleado, String fecha, String hora) {
        DetalleTareo nuevoDetalle = new DetalleTareo();
        nuevoDetalle.setFlgEstadoIniTareo(StatusIniDetalleTareo.TAREO_DETALLE_INICIADO);
        nuevoDetalle.setFechaIngreso(DateUtil.changeStringDateTimeFormat(fecha,
                Constants.F_DATE_TAREO, Constants.F_DATE_TAREO));
        nuevoDetalle.setHoraIngreso(hora);

        nuevoDetalle.setCodigoEmpleado(codEmpleado);
        nuevoDetalle.setFkEmpleado(0);

        //En los registros de auditoria se registraran tal cual la fecha del equipo
        nuevoDetalle.setFechaRegistro(DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO));
        nuevoDetalle.setHoraRegistroInicio(DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_TAREO));

        mDetalleTareos.add(nuevoDetalle);

        EmpleadoRow nuevaFila = new EmpleadoRow();
        nuevaFila.setCodigoEmpleado(codEmpleado);
        nuevaFila.setEmpleado("Sin información");
        nuevaFila.setFechaHoraIngreso(nuevoDetalle.getFechaIngreso() + " " + nuevoDetalle.getHoraIngreso());

        getView().agregarEmpleado(nuevaFila);

        getView().reiniciarFechaInicio();
        getView().reiniciarHoraInicio();

        getView().showSuccessMessage(getView().getMessage(R.string.empleado_registrado));
    }

    private void validarEmpleadoNomina(String codEmpleado, String nomina,
                                       String fecha, String hora) {
        Log.e(TAG, "validarEmpleadoNomina codEmpleado: " + codEmpleado + ", nomina: " + nomina +
                ", fecha: " + fecha + ", hora: " + hora);
        getCompositeDisposable().add(interactor.validarEmpleadoPlanilla(codEmpleado, nomina)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "validarEmpleadoNomina doOnComplete");
                    getView().showErrorMessage(getView().getMessage(R.string.empleado_no_encontrado_nomina), null);
                    if (preferences.getRegistrarPersona()) {
                        Log.e(TAG, "validarEmpleadoNomina doOnComplete if ");
                        getView().showErrorMessage(getView().getMessage(R.string.empleado_no_encontrado_nomina), null);
                    } else {
                        Log.e(TAG, "validarEmpleadoNomina doOnComplete else ");
                        getView().showWarningMessage("No se puede agregar empleados, " +
                                "revise su configuracion");
                    }
                })
                .subscribe(empleado -> {
                            Log.e(TAG, "validarEmpleadoNomina subscribe: " + empleado);
                    if (empleado != null) {
                        try {
                            if (empleado.getEstadoEmpleado() == 1) {
                                if (!empleado.getMensaje().toUpperCase(Locale.ROOT).equals("ACTIVO")) {
                                    getView().mostrarMensajeRegistroConfirmacion(empleado, fecha, hora);
                                } else {
                                    registrarEmpleadoTareo(empleado, fecha, hora);
                                }

                                //registrarEmpleadoTareo(empleado, fecha, hora);

                            }
                            else {
                                getView().showErrorMessage(empleado.getMensaje(),"Error");
                            }
                        } catch (NullPointerException e) {
                            if (!empleado.getMensaje().toUpperCase(Locale.ROOT).equals("ACTIVO")) {
                                getView().mostrarMensajeRegistroConfirmacion(empleado, fecha, hora);
                            } else {
                                registrarEmpleadoTareo(empleado, fecha, hora);
                            }
                           // registrarEmpleadoTareo(empleado, fecha, hora);
                        }

                    }

                        }, error -> {
                            Log.e(TAG, "validarEmpleadoNomina error: " + error);
                            Log.e(TAG, "validarEmpleadoNomina error: " + error.toString());
                            Log.e(TAG, "validarEmpleadoNomina error: " + error.getMessage());
                            Log.e(TAG, "validarEmpleadoNomina error: " + error.getLocalizedMessage());
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

}
