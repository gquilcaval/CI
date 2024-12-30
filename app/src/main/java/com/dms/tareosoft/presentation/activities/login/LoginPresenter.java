package com.dms.tareosoft.presentation.activities.login;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.SyncExitosa;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.login_interactor.ILoginInteractor;
import com.dms.tareosoft.annotacion.ModoTrabajo;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class LoginPresenter extends BasePresenter<ILoginContract.View> implements ILoginContract.Presenter {
    private final String TAG = LoginPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    ILoginInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    public LoginPresenter() {
    }

    @Override
    public void attachView(ILoginContract.View view) {
        super.attachView(view);
        getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void validarViewOrNotMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (preferenceManager.getSyncExitosa()) {
                    case SyncExitosa.EXITOSA:
                        getView().viewMenuConfig(false);
                        break;
                    case SyncExitosa.NO_EXITOSA:
                    default:
                        getView().viewMenuConfig(true);
                        break;
                }
            }
        }, 100);
    }

    @Override
    public void login(String username, String password) {
        getView().showProgressbar("Conectando", "Validando usuario...");
        Log.d(TAG, "LOGIN -> " +  preferenceManager.getModoTrabajo() + " | " + username + " " + password);
        disposable = interactor.userLogin(username, password)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().showWarningMessage("Credenciales erróneas o usuario inactivo.");
                    }
                })
                .subscribe(res -> {
                    if (isViewAttached())
                        getView().hiddenProgressbar();
                    if (res != null && !TextUtils.isEmpty(res.toString())) {
                        boolean correctly;
                        if (res.usuario != null && !TextUtils.isEmpty(res.usuario.toString())) {
                            preferenceManager.setNombreUsuario(res.empleado.getDescripcionEmp());
                            preferenceManager.setUsuario(res.usuario.getIdUsuario());
                            correctly = true;
                        } else {
                            correctly = false;
                        }
                        if (res.perfil != null && !TextUtils.isEmpty(res.perfil.toString())) {
                            preferenceManager.setPerfilUsuario(res.perfil.getIdPerfil());
                            preferenceManager.setNombrePerfil(res.perfil.getDescripcion());
                            correctly = true;
                        } else {
                            correctly = false;
                        }
                        //guardar preferencias
                        if (correctly)
                            getView().goToMainMenu();
                        else
                            getView().showWarningMessage("no hay información que guardar");
                    } else {
                        getView().showWarningMessage("Credenciales erróneas o usuario inactivo");
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                });

    }

    @Override
    public void validarModoTrabajo() {
        getView().activarLogin();
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.BATCH:
                if (!preferenceManager.getServicioValidado()) {
                    getView().bloqueoLogin();
                    getView().showWarningMessage(getView().getMessage(R.string.sin_data));
                }
                break;
            case ModoTrabajo.LINEA:
                if (!preferenceManager.getServicioValidado()) {
                    getView().bloqueoLogin();
                    getView().showWarningMessage(getView().getMessage(R.string.sin_validar_conexion));
                }
                break;
            default:
                getView().bloqueoLogin();
                break;

        }
    }

    @Override
    public Boolean validaDescargaExitosa() {
        return preferenceManager.GetValidaDescargaExitosa();
    }


}