package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.opciones;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_editar.editar_opciones.IEditarOpcionesInteractor;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class OpcionesEditarPresenter extends BasePresenter<IOpcionesEditarContract.View>
        implements IOpcionesEditarContract.Presenter {
    private final String TAG = OpcionesEditarPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IEditarOpcionesInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public OpcionesEditarPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerCamposTareo(String idTareo) {
        Log.e(TAG, "obtenerCamposTareo: " + idTareo);
        disposable = interactor.obtenerTareo(idTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareo -> {
                    Log.e(TAG, "obtenerCamposTareo subscribe: " + tareo);
                    String tipo = "";
                    switch (tareo.getTipoTareo()) {
                        case TypeTareo.TIPO_TAREO_DESTAJO:
                            tipo = "Destajo";
                            break;
                        case TypeTareo.TIPO_TAREO_JORNAL:
                            tipo = "Jornal";
                            break;
                    }

                    if (!TextUtils.isEmpty(tareo.getFechaInicio())
                            && !TextUtils.isEmpty(tareo.getHoraInicio())) {
                        getView().mostrarCampos(tipo,
                                DateUtil.changeStringDateTimeFormat(tareo.getFechaInicio(),
                                        Constants.F_DATE_TAREO,
                                        Constants.F_DATE_LECTURA),
                                tareo.getHoraInicio());
                    }

                    obtenerTurno(tareo.getFkTurno());

                    if (tareo.getFkUnidadMedida() > 0)
                        obtenerUnidadMedida(tareo.getFkUnidadMedida());

                }, error -> {
                    Log.e(TAG, "obtenerCamposTareo error: " + error);
                    Log.e(TAG, "obtenerCamposTareo error: " + error.toString());
                    Log.e(TAG, "obtenerCamposTareo error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener los campos del tareo", error.getMessage());
                });
    }

    private void obtenerTurno(int fkTurno) {
        Log.e(TAG, "obtenerTurno: " + fkTurno);
        disposable = interactor.obtenerTurno(fkTurno)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(turno -> {
                    Log.e(TAG, "obtenerTurno subscribe: " + turno);
                    getView().mostrarTurno(turno.getDescripcion());
                }, error -> {
                    Log.e(TAG, "obtenerTurno error: " + error);
                    Log.e(TAG, "obtenerTurno error: " + error.toString());
                    Log.e(TAG, "obtenerTurno error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener el turno del tareo", error.getMessage());
                });
    }

    private void obtenerUnidadMedida(int fkUnidadMedida) {
        Log.e(TAG, "obtenerUnidadMedida: " + fkUnidadMedida);
        disposable = interactor.obtenerUnidadMedida(fkUnidadMedida)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(unidadMedida -> {
                    Log.e(TAG, "obtenerUnidadMedida subscribe: " + unidadMedida);
                    getView().mostrarUnidadMedida(unidadMedida.getDescripcion());
                }, error -> {
                    Log.e(TAG, "obtenerUnidadMedida error: " + error);
                    Log.e(TAG, "obtenerUnidadMedida error: " + error.toString());
                    Log.e(TAG, "obtenerUnidadMedida error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public boolean validarFechaInicio(Date date) {
        int cantDias = preferenceManager.getCantDiasFechaIniTareo();
        if (DateUtil.obtenerRestoFechaHora(cantDias) >= date.getTime()) {
            String mensaje = getView().getMessage(R.string.fecha_menor).replace("x", "" + cantDias);
            getView().showWarningMessage(mensaje);
            return false;
        }
        return true;
    }
}
