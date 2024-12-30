package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.tareo_nuevo.INuevoTareoInteractor;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.ReubicarTipoEmpleadoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.TextUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class NewTareoNotEmployerPresenter extends BasePresenter<NewTareoNotEmployerContract.View>
        implements NewTareoNotEmployerContract.Presenter {

    String TAG = NewTareoNotEmployerPresenter.class.getSimpleName();

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    INuevoTareoInteractor interactor;

    @Inject
    public NewTareoNotEmployerPresenter() {
        //TODO  getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void guardarTareo(Tareo nuevoTareo, int niveles, OpcionesTareo campos) {
        // Completar campos tareo
        nuevoTareo.setFechaInicio(campos.getFechaInicio());
        ReubicarTipoEmpleadoActivity.horaInicio = campos.getHoraInicio(); // se define hora inicio provicional momentaneo
        nuevoTareo.setHoraInicio(campos.getHoraInicio());
        nuevoTareo.setFkTurno(campos.getCodigoTurno());
        nuevoTareo.setTipoTareo(campos.getTipoTareo());
        nuevoTareo.setTipoResultado(campos.getTipoResultado());
        nuevoTareo.setUsuarioInsert(preferenceManager.getUsuario());
        nuevoTareo.setFkUnidadMedida(campos.getCodigoUnidadMedida());
        //Campos de auditoria se registra tal cual hora y fecha del equipo
        nuevoTareo.setFechaRegistroInicio(DateUtil.obtenerFechaHoraEquipo(Constants.F_DATE_TAREO));
        nuevoTareo.setHoraRegistroInicio(DateUtil.obtenerFechaHoraEquipo(Constants.F_TIME_TAREO));
        //nuevoTareo.setCantTrabajadores(empleados.size());

        nuevoTareo.setFlgEstadoRegistro(0);
        nuevoTareo.setFlgHoraInicio(String.valueOf(1));
        nuevoTareo.setFlgFechaInicio(1);
        nuevoTareo.setFlgHoraFin(String.valueOf(0));
        nuevoTareo.setFlgFechaFin(0);

        if (validarCamposTareo(nuevoTareo, niveles)) {

            String codTareo = TextUtil.toString(nuevoTareo.getFkConcepto1()) + TextUtil.toString(nuevoTareo.getFkConcepto2()) + TextUtil.toString(nuevoTareo.getFkConcepto3()) +
                    TextUtil.toString(nuevoTareo.getFkConcepto4()) + TextUtil.toString(nuevoTareo.getFkConcepto5()) +
                    nuevoTareo.getFechaInicio() + nuevoTareo.getHoraInicio().replace(":", ""); // + ID_TERMINAL
            nuevoTareo.setCodTareo(codTareo);

            if (campos.getTipoTareo() == TypeTareo.TIPO_TAREO_DESTAJO) {
                nuevoTareo.setFkUnidadMedida(campos.getCodigoUnidadMedida());
            }

            String json = new Gson().toJson(nuevoTareo);
            Log.e(TAG, "guardarTareo json: " + json);

            getCompositeDisposable().add(interactor.registrarTareo(nuevoTareo)
                    .subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .doOnComplete(() -> {
                        Log.e(TAG, "guardarTareo doOnComplete: ");
                        getView().showWarningMessage("No se pudo guardar el Tareo");
                        getView().setResult(false);
                    })
                    .subscribe(respuesta -> {
                                Log.e(TAG, "guardarTareo subscribe: " + respuesta.toString());
                                getView().setResult(true);
                            }, error -> {
                                getView().setResult(false);
                                Log.e(TAG, "guardarTareo error: " + error);
                                Log.e(TAG, "guardarTareo error: " + error.toString());
                                Log.e(TAG, "guardarTareo error: " + error.getMessage());
                                Log.e(TAG, "guardarTareo error: " + error.getLocalizedMessage());
                                getView().showErrorMessage("Error al registrar el Tareo", error.getMessage());
                            }
                    ));
        }
    }

    private boolean validarCamposTareo(Tareo nuevoTareo, int niveles) {
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
        if (TextUtils.isEmpty(nuevoTareo.getFechaInicio()) || TextUtils.isEmpty(nuevoTareo.getHoraInicio())) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_feha_inicio_tareo));
        }
        if (nuevoTareo.getTipoTareo() == TypeTareo.TIPO_TAREO_DESTAJO && nuevoTareo.getFkUnidadMedida() < 1) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_unidad_medida));
        }

        //Validar unidad obligatoria
        if (preferenceManager.getAjustesUnidadMedida()) {
            if (nuevoTareo.getFkUnidadMedida() <= 0) {
                listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_unidad_medida));
            }
        }

        // Mostrar Errores
        if (listaErrores.size() > 0) {
            getView().showDetailedErrorDialog(listaErrores);
            getView().changePage(page);
        }

        return listaErrores.size() == 0;
    }
}