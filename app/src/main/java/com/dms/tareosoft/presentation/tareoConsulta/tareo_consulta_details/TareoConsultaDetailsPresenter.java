package com.dms.tareosoft.presentation.tareoConsulta.tareo_consulta_details;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_consultar.tareo_consultar_details.ITareoConsultaDetailsInteractor;

import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TareoConsultaDetailsPresenter extends BasePresenter<ITareoConsultaDetailsContract.View>
        implements ITareoConsultaDetailsContract.Presenter {
    private final String TAG = TareoConsultaDetailsPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    ITareoConsultaDetailsInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    private String codTareo;

    @Inject
    public TareoConsultaDetailsPresenter() {
    }

    @Override
    public void setCodTareo(String codTareo) {
        this.codTareo = codTareo;
    }

    @Override
    public void obtenerDetailTareo() {
        getCompositeDisposable().add(interactor.obtenerTareoDetail(codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(detalleTareo -> {
                    getView().mostrarDetalleTareo(detalleTareo);
                    Log.e(TAG, "subscribe: " + detalleTareo);
                }, throwable -> {
                    Log.e(TAG, "throwable: " + throwable.toString());
                    Log.e(TAG, "throwable: " + throwable.getMessage());
                    Log.e(TAG, "throwable: " + throwable.getLocalizedMessage());
                    Log.e(TAG, "throwable: " + throwable.getCause());
                })
        );
    }

    @Override
    public void obtenerAllEmpleados() {
        getCompositeDisposable().add(interactor.obtenerEmpleadosConsulta(codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleadoRows -> {
                    getView().mostrarEmpleados(empleadoRows);
                    Log.e(TAG, "obtenerAllEmpleados subscribe: " + empleadoRows);
                }, throwable -> {
                    Log.e(TAG, "obtenerAllEmpleados throwable: " + throwable.toString());
                    Log.e(TAG, "obtenerAllEmpleados throwable: " + throwable.getMessage());
                    Log.e(TAG, "obtenerAllEmpleados throwable: " + throwable.getLocalizedMessage());
                    Log.e(TAG, "obtenerAllEmpleados throwable: " + throwable.getCause());
                })
        );
    }

    @Override
    public void deleteTareo(String codTareo) {
        HashMap<String, Integer> body = new HashMap<>();
        body.put("pIdUsuarioRegistro", preferences.getUsuario());
        JSONObject jsonData = new JSONObject(body);
        Log.e(TAG, "deleteTareo codigoTareo: " + codTareo + ", body: " + body + ", jsonData: " + jsonData);
        getCompositeDisposable().add(interactor.deleteTareo(codTareo, body)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                    if (isViewAttached()) {
                        getView().showSuccessMessage("Se elimino el registro con exito");
                        getView().closed();
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    public boolean isAdmin() {
        return preferences.isAdmin();
    }
}
