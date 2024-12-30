package com.dms.tareosoft.presentation.incidencia;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.asistencia.empleado.IAsistenciaEmpleadoInteractor;
import com.dms.tareosoft.domain.incidencia.IRegistroIncidenciaInteractor;
import com.dms.tareosoft.presentation.asistencia.registro.RegistroAsistenciaActivity;
import com.dms.tareosoft.presentation.asistencia.registro.definicion.AsistenciaDefinicionPresenter;
import com.dms.tareosoft.presentation.asistencia.registro.empleado.IAsistenciaEmpleadoContract;
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

public class RegistroIncidenciaPresenter extends BasePresenter<IRegistroIncidenciaContract.View>
        implements IRegistroIncidenciaContract.Presenter {

    private final String TAG = RegistroIncidenciaPresenter.class.getSimpleName();
    private List<ClaseTareo> listaClaseTareos;

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    Context context;
    @Inject
    IRegistroIncidenciaInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    private Incidencia nuevoTareo;

    @Inject
    public RegistroIncidenciaPresenter() {
        listaClaseTareos = new ArrayList<>();
        nuevoTareo = new Incidencia();
        nuevoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
    }


    @Override
    public void registroIncidencia(Incidencia incidencia) {
        String fechaActual = DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO);
        String horaActual = DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_TAREO);
        incidencia.setDTM_FECMARCA(fechaActual);
        incidencia.setVCH_HORAMARCA(horaActual);
        incidencia.setVCH_USUREG(preferences.getNombreUsuario());
        incidencia.setDTM_FECREG(fechaActual);
        Log.e(TAG, "registroIncidencia -> "+ incidencia);
        getCompositeDisposable().add(interactor.registrarIncidenciaLocal(incidencia)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "guardarTareo doOnComplete: ");
                    getView().showWarningMessage("No se pudo guardar la incidencia");
                })
                .subscribe(respuesta -> {
                            Log.e(TAG, "respuesta -> " + respuesta);
                            if (respuesta >= 0) {
                                getView().showSuccessMessage(context.getString(R.string.registro_exitoso));
                                getView().limpiarCampos();
                            } else {
                                getView().showErrorMessage(context.getString(R.string.error_registrar), "" );
                            }
                        }, error -> {
                            Log.e(TAG, "guardarTareo error: " + error);
                            Log.e(TAG, "guardarTareo error: " + error.toString());
                            Log.e(TAG, "guardarTareo error: " + error.getMessage());
                            Log.e(TAG, "guardarTareo error: " + error.getLocalizedMessage());
                            getView().showErrorMessage("Error al registrar la marcación", error.getMessage());
                        }
                ));
    }

    @Override
    public void obtenerClasesTareo() {
        disposable = interactor.listarClaseTareo().subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    listaClaseTareos.clear();
                    listaClaseTareos.addAll(lista);
                    int index = 1;
                    int posicion = 0;
                    List<String> listaSpinner = new ArrayList<>();
                    listaSpinner.add("Seleccione");

                    for (ClaseTareo item : listaClaseTareos) {
                        listaSpinner.add(item.getDescripcion());
                        if (item.getId() == preferences.getClaseTareo()) {
                            posicion = index;
                        }
                        index += 1;
                    }

                    getView().listarClaseTareos(listaSpinner, posicion);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void setClaseTareo(int pos) {
        if (pos > 0) {
            Log.e(TAG, "setClaseTareo:" + pos);
            ClaseTareo claseTareo = listaClaseTareos.get(pos - 1);
            Log.e(TAG, "setClaseTareo:" + claseTareo);
            nuevoTareo.setVch_ClaseTareo(claseTareo.getDescripcion());
            disposable = interactor.obtenerNivelesTareo(claseTareo.getId()).subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .subscribe(lista -> {
                        if (lista != null && lista.size() > 0) {
                            Log.e(TAG, "setClaseTareo subscribe:" + lista);
                            getView().actualizarVistaNiveles(lista);
                        } else {
                            getView().showErrorMessage("No se pudo obtener las Clases de Tareo", "");
                        }
                    }, error -> {
                        Log.e(TAG, "setClaseTareo error:" + error);
                        Log.e(TAG, "setClaseTareo error:" + error.toString());
                        Log.e(TAG, "setClaseTareo error:" + error.getMessage());
                        getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                    });
        } else {
            nuevoTareo.setVch_Actividad("");
        }
    }

    @Override
    public void setNivel1(String idConceptoTareo) {
        nuevoTareo.setVch_Actividad(idConceptoTareo);
        Log.d(TAG, "SETNIVEL 1 -> " +nuevoTareo);
    }

    @Override
    public void setNivel2(String actividad) {
        nuevoTareo.setVch_Tarea(actividad);
    }

    public void setNivel3(String mensajeIncidencia) {
        nuevoTareo.setVCH_MENSAJE(mensajeIncidencia);
    }

    @Override
    public Incidencia obtenerIncidencia() {
        return nuevoTareo;
    }

    @Override
    public String getPerfilUser() {
        return preferences.getNombrePerfil();
    }
}
