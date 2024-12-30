package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.definicion;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.domain.tareo_editar.editar_definicion.IEditarDefinicionInteractor;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class DefinicionEditarPresenter extends BasePresenter<IDefinicionEditarContract.View>
        implements IDefinicionEditarContract.Presenter {
    private final String TAG = DefinicionEditarPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IEditarDefinicionInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private Tareo consultaTareo;
    private String mCodTareo;

    @Inject
    public DefinicionEditarPresenter() {
        //TODO   getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void setCodTareo(String codTareo) {
        mCodTareo = codTareo;
    }

    @Override
    public void cargarTareo() {
        Log.e(TAG, "cargarTareo: " + mCodTareo);
        getView().showProgressbar("Cargando", "Consultando tareo");
        disposable = interactor.obtenerTareo(mCodTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareo -> {
                    Log.e(TAG, "cargarTareo subscribe: " + tareo);
                    if (tareo != null) {
                        consultaTareo = tareo;
                        if (consultaTareo.getCodigoClase() > 0)
                            cargarClaseTareo(consultaTareo.getCodigoClase());
                        else
                            getView().showErrorMessage("El codigo de la clase esta vacio", "");
                    } else {
                        getView().showErrorMessage("Sin informacion para mostrar", "");
                    }
                }, error -> {
                    Log.e(TAG, "cargarTareo error: " + error);
                    Log.e(TAG, "cargarTareo error: " + error.toString());
                    Log.e(TAG, "cargarTareo error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void mostrarNiveles(int idClaseTareo) {
        Log.e(TAG, "mostrarNiveles: " + idClaseTareo);
        disposable = interactor.obtenerClaseTareo(idClaseTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(claseTareo -> {
                    Log.e(TAG, "mostrarNiveles subscribe: " + claseTareo);
                    getView().actualizarClaseTareo(claseTareo.getDescripcion());
                    obtenerNivelesTareo(claseTareo.getId());
                }, error -> {
                    Log.e(TAG, "mostrarNiveles error: " + error);
                    Log.e(TAG, "mostrarNiveles error: " + error.toString());
                    Log.e(TAG, "mostrarNiveles error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });

    }

    @Override
    public void obtenerConceptoTareo1(int idNivel) {
        disposable = interactor.listarConceptosTareo(idNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    Log.e(TAG, "obtenerConceptoTareo1 subscribe: " + lista);
                    getView().listarConceptoTareo1(lista,
                            obtenerDescripcionConcepto(lista, consultaTareo.getFkConcepto1()));
                }, error -> {
                    Log.e(TAG, "obtenerConceptoTareo1 error: " + error);
                    Log.e(TAG, "obtenerConceptoTareo1 error: " + error.toString());
                    Log.e(TAG, "obtenerConceptoTareo1 error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void obtenerConceptoTareo2(int idNivel) {
        disposable = interactor.listarConceptosTareo(idNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().listarConceptoTareo2(lista,
                            obtenerDescripcionConcepto(lista, consultaTareo.getFkConcepto2()));
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void obtenerConceptoTareo3(int idNivel) {
        disposable = interactor.listarConceptosTareo(idNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().listarConceptoTareo3(lista,
                            obtenerDescripcionConcepto(lista, consultaTareo.getFkConcepto3()));
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void obtenerConceptoTareo4(int idNivel) {
        disposable = interactor.listarConceptosTareo(idNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().listarConceptoTareo4(lista,
                            obtenerDescripcionConcepto(lista, consultaTareo.getFkConcepto4()));
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void obtenerConceptoTareo5(int idNivel) {
        disposable = interactor.listarConceptosTareo(idNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().listarConceptoTareo5(lista,
                            obtenerDescripcionConcepto(lista, consultaTareo.getFkConcepto5()));
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    @Override
    public void setNivel1(int idConceptoTareo) {
        consultaTareo.setFkConcepto1(idConceptoTareo);
    }

    @Override
    public void setNivel2(int idConceptoTareo) {
        consultaTareo.setFkConcepto2(idConceptoTareo);
    }

    @Override
    public void setNivel3(int idConceptoTareo) {
        consultaTareo.setFkConcepto3(idConceptoTareo);
    }

    @Override
    public void setNivel4(int idConceptoTareo) {
        consultaTareo.setFkConcepto4(idConceptoTareo);
    }

    @Override
    public void setNivel5(int idConceptoTareo) {
        consultaTareo.setFkConcepto5(idConceptoTareo);
    }

    @Override
    public String obtenerNomina() {
        if (!TextUtils.isEmpty(consultaTareo.getTipoPlanilla()))
            return consultaTareo.getTipoPlanilla();
        else
            return null;
    }

    @Override
    public Tareo obtenerTareo() {
        return consultaTareo;
    }

    private void cargarClaseTareo(int idClaseTareo) {
        Log.e(TAG, "cargarClaseTareo: " + idClaseTareo);
        disposable = interactor.obtenerClaseTareo(idClaseTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(claseTareo -> {
                    Log.e(TAG, "cargarClaseTareo subscribe: " + claseTareo);
                    getView().actualizarClaseTareo(claseTareo.getDescripcion());
                    obtenerNivelesTareo(claseTareo.getId());
                }, error -> {
                    Log.e(TAG, "cargarClaseTareo error: " + error);
                    Log.e(TAG, "cargarClaseTareo error: " + error.toString());
                    Log.e(TAG, "cargarClaseTareo error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    private void obtenerNivelesTareo(int idClaseTareo) {
        Log.e(TAG, "obtenerNivelesTareo: " + idClaseTareo);
        disposable = interactor.obtenerNivelesTareo(idClaseTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    Log.e(TAG, "obtenerNivelesTareo subscribe: " + lista);
                    getView().actualizarSeccionNiveles(lista);
                }, error -> {
                    Log.e(TAG, "obtenerNivelesTareo error: " + error);
                    Log.e(TAG, "obtenerNivelesTareo error: " + error.toString());
                    Log.e(TAG, "obtenerNivelesTareo error: " + error.getMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                });
    }

    private String obtenerDescripcionConcepto(List<ConceptoTareo> lista, int codigoConcepto) {
        String descripcion = "Sin concepto";
        for (ConceptoTareo item : lista) {
            if (item.getIdConceptoTareo() == codigoConcepto) {
                descripcion = item.getDescripcion();
                break;
            }
        }
        return descripcion;
    }

}
