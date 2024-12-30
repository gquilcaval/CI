package com.dms.tareosoft.presentation.tareoEditar.tareoEditUser;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_editar_emp.ITareoEditarEmpInteractor;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
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

public class EditarEmpleadosPresenter extends BasePresenter<IEditarEmpleadosContract.View>
        implements IEditarEmpleadosContract.Presenter {

    String TAG = EditarEmpleadosPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    ITareoEditarEmpInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private List<DetalleTareo> mDetalleTareos;
    private String fechaHoraInicioTareo;
    private Tareo tareo;
    private String mDateTareo = null;
    private int mIndex = -1;

    @Inject
    public EditarEmpleadosPresenter() {
        mDetalleTareos = new ArrayList<>();
    }

    @Override
    public void cargarTareo(String codTareo) {
        getView().showProgressbar("Cargando", "Consultando tareo");
        disposable = interactor.obtenerTareo(codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareo -> {
                    Log.e(TAG, "cargarTareo subscribe :" + tareo);
                    this.tareo = tareo;
                    Log.e(TAG, "nomina :" + this.tareo.getTipoPlanilla());
                    fechaHoraInicioTareo = this.tareo.getFechaInicio() + " " + this.tareo.getHoraInicio();
                    Log.e(TAG, "fechaHoraInicioTareo: " + fechaHoraInicioTareo);
                    getView().hiddenProgressbar();
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void evaluarEmpleado(String codEmpleado, String fecha, String hora) {
        Log.e(TAG, "evaluarEmpleado codEmpleado: " + codEmpleado + " nomina: " + tareo.getTipoPlanilla() + " fecha: " + fecha + " hora: " + hora);
        if (preferences.getSonidoLectura()) {
            getView().playSound();
        }

        if (TextUtils.isEmpty(tareo.getTipoPlanilla())) {
            getView().showErrorMessage(getView().getMessage(R.string.clase_erronea), null);
        } else {
            if (!empleadoEnLista(codEmpleado, fecha, hora)) {
                //Si se permite registrar personas solo con codigo
                if (preferences.getRegistrarPersona()) {
                    disposable = interactor.validarExistenciaEmpleado(codEmpleado)
                            .subscribeOn(ExecutorThread)
                            .observeOn(UiThread)
                            .doOnComplete(() -> {
                                //"Código de empleado no encontrado."
                                Log.e(TAG, "doOnComplete evaluarEmpleado");
                                if (preferences.getRegistrarPersona()) {
                                    Log.e(TAG, "doOnComplete evaluarEmpleado if ");
                                    getCompositeDisposable().add(
                                            interactor.validarExistenciaEmpleadoporDni(codEmpleado,
                                                    StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO)
                                                    .subscribeOn(ExecutorThread)
                                                    .observeOn(UiThread)
                                                    .doOnComplete(() -> {
                                                        registrarEmpleadoTareoPorDni(codEmpleado, fecha, hora);
                                                        Log.e(TAG, "doOnComplete evaluarEmpleado preferences.getRegistrarPersona() if ");
                                                    })
                                                    .subscribe(empleado -> {
                                                        getView().showWarningMessage(getView().getMessage(R.string.empleado_ocupado));
                                                        Log.e(TAG, "subscribe evaluarEmpleado preferences.getRegistrarPersona() if ");
                                                    }));
                                } else {
                                    Log.e(TAG, "doOnComplete evaluarEmpleado else ");
                                    getView().showWarningMessage("No se puede agregar empleados, " +
                                            "revise su configuracion");
                                }
                            })
                            .subscribe(empleado -> {
                                        Log.e(TAG, "subscribe evaluarEmpleado");
                                        //Validar Nomina
                                        if (empleado.getTipoPlanilla().equals(tareo.getTipoPlanilla())) {
                                            registrarEmpleadoTareo(empleado, fecha, hora);
                                        } else {
                                            getView().showWarningMessage(getView().getMessage(R.string.empleado_no_encontrado_nomina));
                                        }
                                    }, error -> {
                                        getView().showErrorMessage(getView().getMessage(R.string.error_empleado_bd), error.getMessage());
                                    }
                            );
                } else {
                    validarEmpleadoNomina(codEmpleado, tareo.getTipoPlanilla(), fecha, hora);
                }
            }
        }
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

    private void validarEmpleadoNomina(String codEmpleado, String nomina, String fecha, String hora) {
        getCompositeDisposable().add(interactor.validarEmpleadoPlanilla(codEmpleado, nomina)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    getView().showErrorMessage(getView().getMessage(R.string.empleado_no_encontrado_nomina), null);
                })
                .subscribe(empleado -> {
                            registrarEmpleadoTareo(empleado, fecha, hora);
                        },
                        error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

    @Override
    public void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora) {

        if (empleado.getEstadoEmpleado() == StatusEmpleado.EMPLEADO_NO_LIBRE) {
            getView().showErrorMessage(
                    getView().getMessage(R.string.empleado_ocupado),
                    null);
        } else {

            DetalleTareo nuevoDetalle = new DetalleTareo();

            nuevoDetalle.setFkTareo(tareo.getCodTareo());
            nuevoDetalle.setFlgEstadoIniTareo(StatusIniDetalleTareo.TAREO_DETALLE_INICIADO);

            nuevoDetalle.setFechaIngreso(DateUtil.changeStringDateTimeFormat(fecha, Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO));
            nuevoDetalle.setHoraIngreso(hora);

            nuevoDetalle.setCodigoEmpleado(empleado.getCodigoEmpleado());
            nuevoDetalle.setFkEmpleado(empleado.getId());
            //En los registros de auditoria se registraran tal cual la fecha del equipo
            nuevoDetalle.setFechaRegistro(DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO));
            nuevoDetalle.setHoraRegistroInicio(DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_TAREO));

            //Actualizamos
            String idDetalleTareo = tareo.getCodTareo() + empleado.getCodigoEmpleado() + nuevoDetalle.getFechaIngreso();
            nuevoDetalle.setCodDetalleTareo(idDetalleTareo);

            mDetalleTareos.add(nuevoDetalle);

            EmpleadoRow nuevaFila = new EmpleadoRow();
            nuevaFila.setCodigoEmpleado(empleado.getCodigoEmpleado());
            nuevaFila.setEmpleado(empleado.getNombres() + " " + empleado.getApellidoPaterno() + " " + empleado.getApellidoMaterno());
            nuevaFila.setFechaHoraIngreso(nuevoDetalle.getFechaIngreso() + " " + nuevoDetalle.getHoraIngreso());
            getView().agregarEmpleado(nuevaFila);

            getView().reiniciarFechaInicio();
            getView().reiniciarHoraInicio();

            getView().showSuccessMessage(getView().getMessage(R.string.empleado_registrado));
        }
    }

    @Override
    public List<DetalleTareo> obtenerEmpleados() {
        return mDetalleTareos;
    }

    @Override
    public void cargarEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo) {
        Log.e(TAG, "cargarEmpleados codigoTareo : " + codigoTareo + " statusFinTareo: " + statusFinTareo);
        disposable = interactor.obtenerEmpleados(codigoTareo, statusFinTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleados -> {
                            Log.e(TAG, "cargarEmpleados subscribe : " + empleados);
                            mDetalleTareos.clear();
                            mDetalleTareos.addAll(empleados);
                            cargarListaEmpleados(codigoTareo, statusFinTareo);
                        },
                        error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                );
    }

    @Override
    public void cargarListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo) {
        Log.e(TAG, "cargarListaEmpleados codigoTareo : " + codigoTareo + " statusFinTareo: " + statusFinTareo);
        disposable = interactor.obtenerListaEmpleados(codigoTareo, statusFinTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                            Log.e(TAG, "cargarListaEmpleados subscribe : " + lista);
                            getView().mostrarEmpleados(lista);
                        },
                        error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                );
    }

    @Override
    public boolean validarFecha(Date date, String hora) {
        String fechaInicioEmpleado = DateUtil.dateToStringFormat(date, Constants.F_DATE_LECTURA) + " " + hora;

        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioEmpleado,
                Constants.F_DATE_TIME_TAREO, fechaHoraInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fecha_empleado_menor_fecha_tareo));
            return false;
        }
        return true;
    }

    @Override
    public boolean validarHora(String fecha, Date hora) {
        String fechaInicioEmpleado = fecha + " " + DateUtil.dateToStringFormat(hora, Constants.F_TIME_LECTURA);

        if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaInicioEmpleado,
                Constants.F_DATE_TIME_TAREO, fechaHoraInicioTareo)) {
            getView().showWarningMessage(getView().getMessage(R.string.fecha_empleado_menor_fecha_tareo));
            return false;
        }
        return true;
    }

    @Override
    public void guardarTareo() {
        getView().showProgressbar("Actualizando Tareo",
                "Guardando nueva informacion en el tareo");
        tareo.setCantTrabajadores(mDetalleTareos.size());
        if (validarCamposTareo(tareo.getCantTrabajadores())) {
            disposable = interactor.actualizarTareo(tareo)
                    .subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .doFinally(() -> {
                        Log.e(TAG, "guardarTareo doFinally: ");
                        getView().hiddenProgressbar();
                        guardarEmpleadosTareo(mDetalleTareos);
                    })
                    .subscribe(respuesta -> {
                                Log.e(TAG, "guardarTareo subscribe: " + respuesta);
                            }, error -> {
                                getView().hiddenProgressbar();
                                Log.e(TAG, "guardarTareo error: " + error);
                                Log.e(TAG, "guardarTareo error: " + error.toString());
                                Log.e(TAG, "guardarTareo error: " + error.getMessage());
                                getView().showErrorMessage("No se pudo actualizar el Tareo", error.getMessage());
                            }
                    );
        } else {
            getView().hiddenProgressbar();
        }

    }

    @Override
    public void guardarEmpleadosTareo(List<DetalleTareo> detalleTareo) {
        getView().showProgressbar("Actualizando Detalle Tareo",
                "Guardando nueva informacion en el detalle Tareo");
        disposable = interactor.registrarEmpleadoxsTareo(detalleTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleado -> {
                            Log.e(TAG, "guardarEmpleadosTareo subscribe: " + empleado);
                            getView().showSuccessMessage("Se guardo correctamente el tareo");
                            getView().salir();
                        },
                        error -> {
                            getView().hiddenProgressbar();
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error);
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error.toString());
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error.getMessage());
                            getView().showErrorMessage("No se completó el registro de empleados", error.getMessage());
                        }
                );
    }

    @Override
    public String fechaInicio() {
        Log.e(TAG, "fechaHoraInicioTareo: " + fechaHoraInicioTareo);
        Log.e(TAG, "fechaInicio: " + DateUtil.changeStringDateTimeFormat(fechaHoraInicioTareo,
                Constants.F_DATE_TIME_TAREO,
                Constants.F_DATE_LECTURA));
        return DateUtil.changeStringDateTimeFormat(fechaHoraInicioTareo,
                Constants.F_DATE_TIME_TAREO,
                Constants.F_DATE_LECTURA);
    }

    @Override
    public String horaInicio() {
        Log.e(TAG, "fechaHoraInicioTareo: " + fechaHoraInicioTareo);
        Log.e(TAG, "horaInicio: " + DateUtil.changeStringDateTimeFormat(fechaHoraInicioTareo,
                Constants.F_DATE_TIME_TAREO,
                Constants.F_TIME_LECTURA));
        return DateUtil.changeStringDateTimeFormat(fechaHoraInicioTareo,
                Constants.F_DATE_TIME_TAREO,
                Constants.F_TIME_LECTURA);
    }

    public void registrarEmpleadoTareoPorDni(String codEmpleado, String fecha, String hora) {

        DetalleTareo nuevoDetalle = new DetalleTareo();
        String idDetalleTareo = tareo.getCodTareo() + codEmpleado + fecha + hora;
        nuevoDetalle.setCodDetalleTareo(idDetalleTareo);
        nuevoDetalle.setFkTareo(tareo.getCodTareo());
        nuevoDetalle.setFlgEstadoIniTareo(StatusIniDetalleTareo.TAREO_DETALLE_INICIADO);

        nuevoDetalle.setFechaIngreso(DateUtil.changeStringDateTimeFormat(fecha,
                Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO));
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

    private boolean validarCamposTareo(int empleados) {
        ArrayList<String> listaErrores = new ArrayList<>();

        //Validar Empleado
        if (empleados < 1) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_trabajadores));
        }

        if (listaErrores.size() > 0) {
            getView().showDetailedErrorDialog(listaErrores);
        }

        return listaErrores.size() == 0;
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
}
