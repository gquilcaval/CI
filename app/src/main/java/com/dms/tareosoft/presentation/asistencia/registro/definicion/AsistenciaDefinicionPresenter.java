package com.dms.tareosoft.presentation.asistencia.registro.definicion;

import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.asistencia.registro.registro_definicion.IRegistroDefinicionInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion.IDefinicionInteractor;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.asistencia.registro.RegistroAsistenciaActivity;
import com.dms.tareosoft.presentation.asistencia.registro.definicion.IAsistenciaDefinicionContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class AsistenciaDefinicionPresenter extends BasePresenter<IAsistenciaDefinicionContract.View>
        implements IAsistenciaDefinicionContract.Presenter {
    private final String TAG = AsistenciaDefinicionPresenter.class.getSimpleName();
    private List<ClaseTareo> listaClaseTareos;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IRegistroDefinicionInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    private Marcacion nuevoTareo;

    @Inject
    public AsistenciaDefinicionPresenter() {
        listaClaseTareos = new ArrayList<>();
        nuevoTareo = new Marcacion();
        // Valores por defecto

        nuevoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void setClaseTareo(int pos) {
        if (pos > 0) {
            Log.e(TAG, "setClaseTareo:" + pos);
            ClaseTareo claseTareo = listaClaseTareos.get(pos - 1);
            Log.e(TAG, "setClaseTareo:" + claseTareo);
            nuevoTareo.setClaseTareo(claseTareo.getDescripcion());
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
            nuevoTareo.setActividad("");
        }
    }

    @Override
    public void obtenerClasesTareo() {
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
                        if (item.getId() == preferenceManager.getClaseTareo()) {
                            posicion = index;
                        }
                        index += 1;
                    }

                    getView().listarClaseTareos(listaSpinner, posicion);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });

    }

    @Override
    public void setNivel1(String idConceptoTareo) {
        nuevoTareo.setActividad(idConceptoTareo);
        RegistroAsistenciaActivity.m = nuevoTareo;
        Log.d(TAG, "SETNIVEL 1 -> " +nuevoTareo);
    }

    @Override
    public void setNivel2(String idConceptoTareo) {
        nuevoTareo.setTarea(idConceptoTareo);
    }

    @Override
    public Marcacion obtenerTareo() {

        return new Marcacion();
    }

    @Override
    public String getPerfilUser() {
        return preferenceManager.getNombrePerfil();
    }
}
