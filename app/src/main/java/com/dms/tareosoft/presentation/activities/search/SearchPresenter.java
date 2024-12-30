package com.dms.tareosoft.presentation.activities.search;

import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.search.ISearchInteractor;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class SearchPresenter extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {

    String TAG = SearchPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    ISearchInteractor interactor;
    private int mIdNivel, mIdPadre;

    @Inject
    public SearchPresenter() {
    }

    public void searchFilter(String filter) {
        if (!TextUtils.isEmpty(filter)) {/*
            if (mIdNivel > 0 && mIdPadre > 0) {*/
            obtenerConceptoTareoAndPadre("%" + filter + "%");
            /*} else if (mIdNivel > 0) {
                obtenerConceptoTareo("%" + filter + "%");
            }*/
        } else {/*
            if (mIdNivel > 0 && mIdPadre > 0) {*/
            obtenerConceptoTareoAndPadre();
           /* } else if (mIdNivel > 0) {
                obtenerConceptoTareo();
            }*/
        }
    }

    public void searchFilterCod(String filter) {
        if (!TextUtils.isEmpty(filter)) {/*
            if (mIdNivel > 0 && mIdPadre > 0) {*/
            obtenerConceptoTareoAndPadreCod("%" + filter + "%");
            /*} else if (mIdNivel > 0) {
                obtenerConceptoTareoCod("%" + filter + "%");
            }*/
        } else {/*
            if (mIdNivel > 0 && mIdPadre > 0) {*/
            obtenerConceptoTareoAndPadre();
            /*} else if (mIdNivel > 0) {
                obtenerConceptoTareo();
            }*/
        }
    }

    public void setIdNivel(int idNivel) {
        mIdNivel = idNivel;
    }

    public void setIdPadre(int idPadre) {
        mIdPadre = idPadre;
    }

    /*@Override
    public void obtenerConceptoTareo() {
        getCompositeDisposable().add(interactor.listarConceptosTareo(mIdNivel)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }*/

    @Override
    public void obtenerConceptoTareoAndPadre() {
        Log.e(TAG, "fkNivel: " + mIdNivel);
        Log.e(TAG, "fkPadre: " + mIdPadre);
        getCompositeDisposable().add(interactor.listarConceptosTareo(mIdNivel, mIdPadre)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    Log.e(TAG, "obtenerConceptoTareoAndPadre subscribe: " + lista);
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    Log.e(TAG, "obtenerConceptoTareoAndPadre error: " + error);
                    Log.e(TAG, "obtenerConceptoTareoAndPadre error: " + error.toString());
                    Log.e(TAG, "obtenerConceptoTareoAndPadre error: " + error.getMessage());
                    Log.e(TAG, "obtenerConceptoTareoAndPadre error: " + error.getLocalizedMessage());
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }

    /*@Override
    public void obtenerConceptoTareo(String search) {
        getCompositeDisposable().add(interactor.listarConceptosTareoLike(mIdNivel, search)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }*/

    @Override
    public void obtenerConceptoTareoAndPadre(String search) {
        getCompositeDisposable().add(interactor.listarConceptosTareoLike(mIdNivel, mIdPadre, search)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }

    /*@Override
    public void obtenerConceptoTareoCod(String search) {
        getCompositeDisposable().add(interactor.listarConceptosTareoLikeCod(mIdNivel, search)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }*/

    @Override
    public void obtenerConceptoTareoAndPadreCod(String search) {
        getCompositeDisposable().add(interactor.listarConceptosTareoLikeCod(mIdNivel, mIdPadre, search)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(lista -> {
                    getView().mostrarConceptoTareo(lista);
                }, error -> {
                    getView().showErrorMessage("No se pudo obtener las Clases de Tareo", error.getMessage());
                }));
    }

    @Override
    public String getUserName() {
        return preferences.getNombreUsuario();
    }

    @Override
    public String getPerfilUser() {
        return preferences.getNombrePerfil();
    }
}
