package com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos;

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
import com.dms.tareosoft.domain.tareo_reubicar.tareo_reubicar_list.ITareoReubicarListInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TareoReubicarListPresenter extends BasePresenter<ITareoReubicarListContract.View>
        implements ITareoReubicarListContract.Presenter {

    private final String TAG = TareoReubicarListPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    ITareoReubicarListInteractor interactor;
    @Inject
    DateTimeManager dateTimeManager;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private List<AllEmpleadoRow> listAllEmpleados = new ArrayList<>();
    List<DetalleTareo> listDetalleTareo = new ArrayList<>();

    private boolean correctly;

    @Inject
    public TareoReubicarListPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void obtenerTareosIniciados() {
        List<String> listCodTareo = new ArrayList<>();
        List<Integer> listCodClase = new ArrayList<>();

        for (AllEmpleadoRow allEmpleadoRow : listAllEmpleados) {
            listCodTareo.add(allEmpleadoRow.getCodigoTareo());
            listCodClase.add(allEmpleadoRow.getCodigoClase());
        }
        getCompositeDisposable().add(interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
                preferenceManager.getUsuario(),
                StatusDescargaServidor.PENDIENTE,
                listCodTareo,
                listCodClase)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "obtenerTareosIniciados doOnComplete: ");
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(tareos -> {
                    Log.e(TAG, "obtenerTareosIniciados subscribe: " + tareos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().mostrarTareos(tareos);
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error);
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getMessage());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.toString());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getLocalizedMessage());
                }));
    }

    @Override
    public void createNewListDetalleTareo(String codTareo) {
        Log.e(TAG, "codTareo: " + codTareo);
        for (AllEmpleadoRow allEmpleadoRow : listAllEmpleados) {
            DetalleTareo detalleTareo = new DetalleTareo();
            String fechaIngreso = appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO);
            String horaIngreso = (TareoReubicarListActivity.horainicio.isEmpty()) ? appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO) : TareoReubicarListActivity.horainicio ;//appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO);
            String codDetalleTareo = String.format("%s%s%s",
                    codTareo,
                    allEmpleadoRow.getCodigoEmpleado(),
                    fechaIngreso);
            detalleTareo.setCodDetalleTareo(codDetalleTareo);
            detalleTareo.setFkTareo(codTareo);
            detalleTareo.setFkEmpleado(allEmpleadoRow.getIdEmpleado());
            detalleTareo.setCodigoEmpleado(allEmpleadoRow.getCodigoEmpleado());
            detalleTareo.setFechaRegistro(fechaIngreso);
            detalleTareo.setHoraRegistroInicio(appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO));
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
                        getView().reubicarExitoso();
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

    @Override
    public int getTimeValidezTareo() {
        return preferenceManager.getVigenciaTareo();
    }

    @Override
    public String getcurrentDateTime() {
        return dateTimeManager.getFechaSincronizada(Constants.F_DATE_RESULTADO);
    }


    public void setListAllEmpleados(List<AllEmpleadoRow> listaAllEmpleados) {
        Log.e(TAG, "listaAllEmpleados: " + listaAllEmpleados);
        this.listAllEmpleados = listaAllEmpleados;
    }
}
