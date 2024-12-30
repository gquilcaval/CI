package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.ITareoReubicarMasivoListInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TareoReubicarMasivoListPresenter extends BasePresenter<ITareoReubicarMasivoListContract.View>
        implements ITareoReubicarMasivoListContract.Presenter {

    private final String TAG = TareoReubicarMasivoListPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    ITareoReubicarMasivoListInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private ArrayList<AllEmpleadoRow> listAllEmpleados = new ArrayList<>();
    private ArrayList<DetalleTareo> listDetalleTareo = new ArrayList<>();

    private boolean correctly;

    @Inject
    public TareoReubicarMasivoListPresenter() {
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
    public void createNewListDetalleTareo(String codTareo) {
        Log.e(TAG, "codTareo: " + codTareo);
        for (AllEmpleadoRow allEmpleadoRow : listAllEmpleados) {
            DetalleTareo detalleTareo = new DetalleTareo();
            String fechaIngreso = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO);
            String horaIngreso = appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO);
            String codDetalleTareo = String.format("%s%s%s",
                    codTareo,
                    allEmpleadoRow.getCodigoEmpleado(),
                    fechaIngreso);
            detalleTareo.setCodDetalleTareo(codDetalleTareo);
            detalleTareo.setFkTareo(codTareo);
            detalleTareo.setFkEmpleado(allEmpleadoRow.getIdEmpleado());
            detalleTareo.setCodigoEmpleado(allEmpleadoRow.getCodigoEmpleado());
            detalleTareo.setFechaRegistro(fechaIngreso);
            detalleTareo.setHoraRegistroInicio(horaIngreso);
            detalleTareo.setFechaIngreso(fechaIngreso);
            detalleTareo.setHoraIngreso(horaIngreso);
            detalleTareo.setFlgEstadoIniTareo(StatusIniDetalleTareo.TAREO_DETALLE_INICIADO);
            detalleTareo.setFlgEstadoFinTareo(StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO);
            detalleTareo.setInt_FlgHoraRefrigerio(StatusRefrigerio.NO_REFRIGERIO);
            detalleTareo.setInsUsuarioDetalle(0);
            listDetalleTareo.add(detalleTareo);
        }
        Log.e(TAG, "listDetalleTareo: " + listDetalleTareo);
        if (listDetalleTareo != null && listDetalleTareo.size() > 0)
            crearNuevoDetalleTareo();
        else
            getView().showErrorMessage("Error al crear nuevo tareo", "");
    }

    @Override
    public void crearNuevoDetalleTareo() {
        getView().showProgressbar("Atención", "Creando nuevo tareo");
        getCompositeDisposable().add(interactor.createNewDetalleTareo(listDetalleTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doFinally(() -> {
                    if (correctly) {
                        getView().hiddenProgressbar();
                        getView().closeWindows();
                    } else {
                        getView().showErrorMessage("Error al realizar la consulta.", "");
                    }
                })
                .subscribe(tareos -> {
                    correctly = true;
                }, error -> {
                    correctly = false;
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    public void setListAllEmpleados(ArrayList<AllEmpleadoRow> listAllEmpleados) {
        this.listAllEmpleados = listAllEmpleados;
    }
}
