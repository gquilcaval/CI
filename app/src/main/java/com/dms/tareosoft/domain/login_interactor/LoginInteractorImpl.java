package com.dms.tareosoft.domain.login_interactor;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.models.UsuarioPerfil;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.annotacion.ModoTrabajo;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class LoginInteractorImpl implements ILoginInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public LoginInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                               @NonNull DataSourceRemote dataSourceRemote,
                               @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<UsuarioPerfil> userLogin(String username, String password) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.userLogin(username, password);
            case ModoTrabajo.BATCH:
                return local.userLogin(username, password);
            default:
                return null;

        }
    }
}
