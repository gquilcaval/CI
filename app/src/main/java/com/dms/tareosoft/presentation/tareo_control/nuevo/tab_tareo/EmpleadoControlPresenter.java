package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_tareo;

import android.content.Context;

import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class EmpleadoControlPresenter extends BasePresenter<IEmpleadoControlContract.View>
        implements IEmpleadoControlContract.Presenter {

    private final String TAG = EmpleadoControlPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    Context context;
    @Inject
    IEmpleadosInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;


    @Inject
    public EmpleadoControlPresenter() {
    }


    public void obtenerEmpleados(String codTareo) {
        getCompositeDisposable().add(interactor.obtenerEmpleadosSinControl(codTareo,
                StatusIniDetalleTareo.TAREO_DETALLE_INICIADO)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(empleadoRows -> {
                    getView().mostrarEmpleados(empleadoRows);
                })
        );
    }

}
