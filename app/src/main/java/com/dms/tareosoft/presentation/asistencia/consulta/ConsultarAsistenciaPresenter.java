package com.dms.tareosoft.presentation.asistencia.consulta;

import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.asistencia.consulta.IConsultaAsistenciaInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ConsultarAsistenciaPresenter extends BasePresenter<IConsultaAsistenciaContract.View>
        implements IConsultaAsistenciaContract.Presenter {
    private final String TAG = ConsultarAsistenciaPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IConsultaAsistenciaInteractor interactor;
    @Inject
    IEmpleadosInteractor interactorEmple;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public ConsultarAsistenciaPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void searchMarcacionesEmpleado(String query) {

        String queryFiltro = query;
        if (queryFiltro.toLowerCase(Locale.ROOT).contains("cibarra")) {
            queryFiltro = query.substring(7,15);
        }
        getCompositeDisposable().add(interactor.searchAsistenciasEmpleado(queryFiltro)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareos -> {
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().displayMarcaciones(tareos);
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }



}
