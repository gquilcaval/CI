package com.dms.tareosoft.presentation.tareo_finalizar;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_interactor.iniciados.IIniciadosInteractor;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class PorFinalizarPresenter extends BasePresenter<IPorFinalizarContract.View>
        implements IPorFinalizarContract.Presenter {
    private final String TAG = PorFinalizarPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IIniciadosInteractor interactor;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public PorFinalizarPresenter() {
        //TODO  getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerTareosIniciados() {
        disposable = interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
                preferenceManager.getCodigoEnvioUsuario(),
                StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(tareos -> {
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().mostrarTareos(tareos);
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                });
    }

    @Override
    public void deleteTareo(String codigoTareo) {
        HashMap<String, Integer> body = new HashMap<>();
        body.put("pIdUsuarioRegistro", preferenceManager.getUsuario());
        JSONObject jsonData = new JSONObject(body);
        Log.e(TAG, "deleteTareo codigoTareo: " + codigoTareo + ", body: " + body + ", jsonData: " + jsonData);
        disposable = interactor.deleteTareo(codigoTareo, body)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                    if (isViewAttached()) {
                        getView().showSuccessMessage("Se elimino el registro con exito");
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                });
    }

    @Override
    public boolean validarTiempoExcedido(String fechaTareo) {
        return DateUtil.tiempoLimiteExcedido(fechaTareo,
                appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO),
                preferenceManager.getVigenciaTareo());
    }

    @Override
    public int horasValidesTareo() {
        return preferenceManager.getVigenciaTareo();
    }
}
