package com.dms.tareosoft.presentation.tareoConsulta;

import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_consultar.ITareoConsultarInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TareoConsultaPresenter extends BasePresenter<ITareoConsultaContract.View>
        implements ITareoConsultaContract.Presenter {
    private final String TAG = TareoConsultaPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    ITareoConsultarInteractor interactor;

    @Inject
    public TareoConsultaPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerAllTareos() {
        getCompositeDisposable().add(interactor.obtenerAllTareos(preferenceManager.getUsuario())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(allTareos -> {
                    Log.e(TAG, "allTareos: " + allTareos);
                    getView().mostrarAllTareos(allTareos);
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }
}
