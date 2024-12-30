package com.dms.tareosoft.presentation.asistencia.registro;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.asistencia.registro.IRegistroAsistenciaInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.INuevoTareoInteractor;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.INuevoTareoContract;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.TextUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class RegistroAsistenciaPresenter extends BasePresenter<IRegistroAsistenciaContract.View>
        implements IRegistroAsistenciaContract.Presenter {

    String TAG = RegistroAsistenciaPresenter.class.getSimpleName();

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IRegistroAsistenciaInteractor interactor;

    @Inject
    public RegistroAsistenciaPresenter() {
        //TODO  getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void guardarTareo(Tareo nuevoTareo, int niveles, List<DetalleTareo> empleados) {
        // Completar campos tareo

    }

    private void guardarEmpleadosTareo(List<DetalleTareo> detalleTareo) {

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
        if (TextUtils.isEmpty(nuevoTareo.getFechaInicio()) || TextUtils.isEmpty(nuevoTareo.getHoraInicio())) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_feha_inicio_tareo));
        }
        if (nuevoTareo.getTipoTareo() == TypeTareo.TIPO_TAREO_DESTAJO && nuevoTareo.getFkUnidadMedida() < 1) {
            listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_unidad_medida));
        }
        //Validar Empleado
        page = 2;
        if (!preferenceManager.getContenidoAjustes().isRegistrarTareoNotEmpleado())
            if (empleados < 1)
                listaErrores.add(Constants.GUION + getView().getMessage(R.string.sin_trabajadores));

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