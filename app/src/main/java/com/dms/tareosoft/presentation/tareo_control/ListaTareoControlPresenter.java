package com.dms.tareosoft.presentation.tareo_control;

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

public class ListaTareoControlPresenter extends BasePresenter<IListaTareoControlContract.View>
        implements IListaTareoControlContract.Presenter {
    private final String TAG = ListaTareoControlPresenter.class.getSimpleName();

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
    public ListaTareoControlPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerTareosIniciados() {
        getCompositeDisposable().add(interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
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
                }));
    }

    @Override
    public void deleteTareo(String codigoTareo) {
        HashMap<String, Integer> body = new HashMap<>();
        body.put("pIdUsuarioRegistro", preferenceManager.getUsuario());
        JSONObject jsonData = new JSONObject(body);
        Log.e(TAG, "deleteTareo codigoTareo: " + codigoTareo + ", body: " + body + ", jsonData: " + jsonData);
        getCompositeDisposable().add(interactor.deleteTareo(codigoTareo, body)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                    if (isViewAttached()) {
                        getView().showSuccessMessage("Se elimino el registro con exito");
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }
}
