package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.definicion;

import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion.IDefinicionInteractor;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class DefNewTareoNotEmployerPresenter extends BasePresenter<IDefNewTareoNotEmployerContract.View>
        implements IDefNewTareoNotEmployerContract.Presenter {

    private final String TAG = DefNewTareoNotEmployerPresenter.class.getSimpleName();
    private List<ClaseTareo> listaClaseTareos;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IDefinicionInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    private Tareo nuevoTareo;

    @Inject
    public DefNewTareoNotEmployerPresenter() {
        listaClaseTareos = new ArrayList<>();
        nuevoTareo = new Tareo();
        // Valores por defecto
        nuevoTareo.setEstado(StatusTareo.TAREO_ACTIVO);
        nuevoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void setClaseTareo(int pos) {
        if (pos > 0) {
            Log.e(TAG, "setClaseTareo:" + pos);
            ClaseTareo claseTareo = listaClaseTareos.get(pos - 1);
            Log.e(TAG, "setClaseTareo:" + claseTareo);
            nuevoTareo.setCodigoClase(claseTareo.getId());
            nuevoTareo.setTipoPlanilla(claseTareo.getCod_planilla());
            disposable = interactor.obtenerNivelesTareo(claseTareo.getId()).subscribeOn(ExecutorThread)
                    .observeOn(UiThread)
                    .subscribe(lista -> {
                        if (lista != null && lista.size() > 0) {
                            Log.e(TAG, "setClaseTareo subscribe:" + lista);
                            getView().actualizarVistaNiveles(lista);
                        } else {
                            getView().showErrorMessage("No se pudo obtener las Clases de Tareo", "");
                        }
                    }, error -> {
                        Log.e(TAG, "setClaseTareo error:" + error);
                        Log.e(TAG, "setClaseTareo error:" + error.toString());
                        Log.e(TAG, "setClaseTareo error:" + error.getMessage());
                        getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                    });
        } else {
            nuevoTareo.setCodigoClase(0);
            nuevoTareo.setTipoPlanilla(null);
            nuevoTareo.setFkConcepto1(0);
            nuevoTareo.setFkConcepto2(0);
            nuevoTareo.setFkConcepto3(0);
            nuevoTareo.setFkConcepto4(0);
            nuevoTareo.setFkConcepto5(0);
        }
    }

    @Override
    public void obtenerClasesTareo(int clase) {
        disposable = interactor.listarClaseTareo().subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    listaClaseTareos.clear();
                    listaClaseTareos.addAll(lista);
                    int index = 1;
                    int posicion = 0;
                    List<String> listaSpinner = new ArrayList<>();
                    listaSpinner.add("Seleccione");

                    for (ClaseTareo item : listaClaseTareos) {
                        listaSpinner.add(item.getDescripcion());
                        if (item.getId() == clase) {
                            posicion = index;
                        }
                        index++;
                    }

                    getView().listarClaseTareos(listaSpinner, posicion);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });

    }

    @Override
    public void setNivel1(int idConceptoTareo) {
        nuevoTareo.setFkConcepto1(idConceptoTareo);
    }

    @Override
    public void setNivel2(int idConceptoTareo) {
        nuevoTareo.setFkConcepto2(idConceptoTareo);
    }

    @Override
    public void setNivel3(int idConceptoTareo) {
        nuevoTareo.setFkConcepto3(idConceptoTareo);
    }

    @Override
    public void setNivel4(int idConceptoTareo) {
        nuevoTareo.setFkConcepto4(idConceptoTareo);
    }

    @Override
    public void setNivel5(int idConceptoTareo) {
        nuevoTareo.setFkConcepto5(idConceptoTareo);
    }

    @Override
    public String obtenerNomina() {
        return nuevoTareo.getTipoPlanilla();
    }

    @Override
    public Tareo obtenerTareo() {
        return nuevoTareo;
    }
}
