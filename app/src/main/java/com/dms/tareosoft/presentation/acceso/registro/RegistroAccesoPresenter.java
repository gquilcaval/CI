package com.dms.tareosoft.presentation.acceso.registro;

import android.content.Context;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.acceso.registro.IRegistroAccesoInteractor;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

/**
 * Created by Giancarlos Quilca
 */
public class RegistroAccesoPresenter  extends BasePresenter<IRegistroAccesoContract.View>
        implements IRegistroAccesoContract.Presenter {

    private final String TAG = RegistroAccesoPresenter.class.getSimpleName();
    private List<ClaseTareo> listaClaseTareos;

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    Context context;
    @Inject
    IRegistroAccesoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    private Incidencia nuevoTareo;

    @Inject
    public RegistroAccesoPresenter() {
        listaClaseTareos = new ArrayList<>();
        nuevoTareo = new Incidencia();
        nuevoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
    }


    @Override
    public void registroAcceso(Acceso acceso) {
        String fechaActual = DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO);

        acceso.setFech_registro(fechaActual);
        acceso.setFlg_envio("P");

        Log.e(TAG, "registroAcceso -> "+ acceso);
        getCompositeDisposable().add(interactor.registrarAccesoLocal(acceso)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "guardarTareo doOnComplete: ");
                    getView().showWarningMessage("No se pudo guardar el acceso");
                })
                .subscribe(respuesta -> {
                            Log.e(TAG, "respuesta -> " + respuesta);
                            if (respuesta >= 0) {
                                getView().showSuccessMessage(context.getString(R.string.registro_exitoso));
                                getView().viewResult(acceso);
                            } else {
                                getView().showErrorMessage(context.getString(R.string.error_registrar), "" );
                            }
                        }, error -> {
                            Log.e(TAG, "guardarTareo error: " + error);
                            Log.e(TAG, "guardarTareo error: " + error.toString());
                            Log.e(TAG, "guardarTareo error: " + error.getMessage());
                            Log.e(TAG, "guardarTareo error: " + error.getLocalizedMessage());
                            getView().showErrorMessage("Error al registrar el acceso", error.getMessage());
                        }
                ));
    }

    @Override
    public void obtenerClasesTareo() {

    }

    @Override
    public void setClaseTareo(int pos) {

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
