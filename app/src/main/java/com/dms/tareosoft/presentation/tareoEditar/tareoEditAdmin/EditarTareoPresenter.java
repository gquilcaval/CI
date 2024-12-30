package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.tareo_editar.IEditarTareoInteractor;
import com.dms.tareosoft.presentation.dialog.DatePickerFragment;
import com.dms.tareosoft.presentation.dialog.TimePickerFragment;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class EditarTareoPresenter extends BasePresenter<IEditarTareoContract.View>
        implements IEditarTareoContract.Presenter {

    String TAG = EditarTareoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IEditarTareoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public EditarTareoPresenter() {
        //todo getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void actualizarTareo(Tareo tareo, int niveles, List<DetalleTareo> detalleTareos, OpcionesTareo campos) {
        getView().showProgressbar(getView().getMessage(R.string.guardando),
                getView().getMessage(R.string.actualizar_tareo));

        if (validarCamposTareo(tareo, niveles, detalleTareos.size())) {
            // Completar campos tareo
            Tareo newTareo = new Tareo();
            newTareo.setCodTareo(tareo.getCodTareo());
            newTareo.setFechaInicio(campos.getFechaInicio());
            newTareo.setHoraInicio(campos.getHoraInicio());
            newTareo.setUsuarioUpdate(preferenceManager.getUsuario());
            newTareo.setCantTrabajadores(detalleTareos.size());

            //Actualizamos lista detalleTareo(en caso se encuentren nuevos empleados)
            for (DetalleTareo item : detalleTareos) {
                String idDetalleTareo = tareo.getCodTareo() + item.getCodigoEmpleado() + item.getFechaIngreso();
                item.setFkTareo(tareo.getCodTareo());
                item.setCodDetalleTareo(idDetalleTareo);
            }
            String json = new Gson().toJson(newTareo);
            Log.e(TAG, "actualizarTareo json: " + json);

            getCompositeDisposable().add(interactor.actualizarTareo(newTareo)
                    .subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .subscribe(respuesta -> {
                                Log.e(TAG, "guardarTareo subscribe respuesta: " + respuesta);
                                if (detalleTareos != null
                                        && detalleTareos.size() > 0) {
                                    registrarEmpleadoEnTareo(detalleTareos);
                                } else {
                                    getView().showSuccessMessage("Se Actualizó correctamente el tareo");
                                    getView().salir();
                                }
                            }, error -> {
                                Log.e(TAG, "actualizarTareo error: " + error);
                                Log.e(TAG, "actualizarTareo error: " + error.toString());
                                Log.e(TAG, "actualizarTareo error: " + error.getMessage());
                                getView().showErrorMessage("No se pudo actualizar el Tareo", error.getMessage());
                            }
                    ));
        } else {
            getView().hiddenProgressbar();
        }
    }

    private void registrarEmpleadoEnTareo(List<DetalleTareo> detalleTareo) {
        String json = new Gson().toJson(detalleTareo);
        Log.e(TAG, "registrarEmpleadoEnTareo json: " + json);
        getCompositeDisposable().add(interactor.registrarEmpleadoEnTareo(detalleTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleado -> {
                            Log.e(TAG, "guardarEmpleadosTareo subscribe empleado : " + empleado);
                            getView().showSuccessMessage("Se Actualizó correctamente el tareo");
                            getView().salir();
                        },
                        error -> {
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error);
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error.toString());
                            Log.e(TAG, "guardarEmpleadosTareo error: " + error.getMessage());
                            getView().showErrorMessage("No se completó el registro de empleados", error.getMessage());
                        }
                ));
    }

    private boolean validarCamposTareo(Tareo nuevoTareo, int niveles, int empleados) {
        ArrayList<String> listaErrores = new ArrayList<>();

        //Validar Definicion : 0
        int page = 0;

        if (TextUtils.isEmpty(nuevoTareo.getTipoPlanilla())) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_clase_tareo));
        }

        String errorNiveles = "";
        if (niveles == 0) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_nivel));
        }

        if (niveles > 0 && nuevoTareo.getFkConcepto1() < 1) {
            errorNiveles += getView().getMessage(R.string.sin_nivel) + " 1, ";
        }

        if (niveles > 1 && nuevoTareo.getFkConcepto2() < 1) {
            errorNiveles += getView().getMessage(R.string.sin_nivel) + " 2, ";
        }

        if (niveles > 2 && nuevoTareo.getFkConcepto3() < 1) {
            errorNiveles += getView().getMessage(R.string.sin_nivel) + " 3, ";
        }

        if (niveles > 3 && nuevoTareo.getFkConcepto4() < 1) {
            errorNiveles += getView().getMessage(R.string.sin_nivel) + " 4, ";
        }

        if (niveles > 4 && nuevoTareo.getFkConcepto5() < 1) {
            errorNiveles += getView().getMessage(R.string.sin_nivel) + " 5";
        }

        if (errorNiveles.length() > 0) {
            listaErrores.add(Constants.GUION + errorNiveles);
        }

        //Validar Opciones
        page = 1;
        if (TextUtils.isEmpty(nuevoTareo.getFechaInicio())
                || TextUtils.isEmpty(nuevoTareo.getHoraInicio())) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_feha_inicio_tareo));
        }

        if (nuevoTareo.getTipoTareo() == TypeTareo.TIPO_TAREO_DESTAJO
                && nuevoTareo.getFkUnidadMedida() < 1) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_unidad_medida));
        }

        //Validar Empleado
        page = 2;
        if (empleados < 1) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_trabajadores));
        }

        if (listaErrores.size() > 0) {
            getView().showDetailedErrorDialog(listaErrores);
            getView().changePage(page);
        }

        return listaErrores.size() == 0;
    }

    @Override
    public void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.e(TAG, "year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                String date = DateUtil.setDateFormat(String.valueOf(year + "/" + (month + 1) + "/" + dayOfMonth),
                        Constants.F_DATE_LECTURA,
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
                String time = DateUtil.setDateFormat(String.valueOf(hourOfDay + "" + minute ),
                        Constants.F_TIME_LECTURA,
                        Constants.F_TIME_TAREO);
                Log.e(TAG, "time: " + time);
                editText.setText(time);
            }
        });
        timePickerFragment.show(fragment.getFragmentManager(), DatePickerFragment.TAG);
    }
}
