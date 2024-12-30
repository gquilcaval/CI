package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_finalizar.IFinalizadoMasivoInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class FinalizarEmpleadoPresenter extends BasePresenter<IFinalizarEmpleadoContract.View>
        implements IFinalizarEmpleadoContract.Presenter {

    @Inject
    PreferenceManager preferences;
    @Inject
    IFinalizadoMasivoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    FinalizarEmpleadoPresenter() {
    }

}
