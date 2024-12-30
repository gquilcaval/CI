package com.dms.tareosoft.presentation.tareo_resultado;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_interactor.iniciados.IIniciadosInteractor;

import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ListaResultadosPresenter extends BasePresenter<IListaResultadosContract.View>
        implements IListaResultadosContract.Presenter {
    private final String TAG = ListaResultadosPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    IIniciadosInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;

    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public ListaResultadosPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerTareosIniciados() {
        disposable = interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
                StatusTareo.TAREO_FINALIZADO,
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
                    Log.e(TAG, "tareos: " + tareos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().mostrarTareos(tareos);
                    }
                }, error -> {
                    Log.e(TAG, "obtenerTareosIniciados error: " + error);
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.toString());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getMessage());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getLocalizedMessage());
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
                    Log.e(TAG, "deleteTareo error: " + error);
                    Log.e(TAG, "deleteTareo error: " + error.toString());
                    Log.e(TAG, "deleteTareo error: " + error.getMessage());
                    Log.e(TAG, "deleteTareo error: " + error.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                });
    }
}
