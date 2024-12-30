package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_empleados;

import android.content.Context;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.tareo_finalizar.lista_empleados.IListadoEmpleadoInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TabEmpleadoPresenter extends BasePresenter<ITabEmpleadoContract.View> implements ITabEmpleadoContract.Presenter {
    private final String TAG = TabEmpleadoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    Context context;
    @Inject
    IListadoEmpleadoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public TabEmpleadoPresenter() {
    }


    public void obtenerEmpleados(String codTareo) {
        getCompositeDisposable().add(interactor.obtenerEmpleados(codTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleadoRows -> {
                    getView().mostrarEmpleados(empleadoRows);
                })
        );
    }

}
