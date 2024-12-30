package com.dms.tareosoft.domain.login_interactor;

import com.dms.tareosoft.data.models.UsuarioPerfil;

import io.reactivex.Maybe;

public interface ILoginInteractor {
    Maybe<UsuarioPerfil> userLogin(String username, String password);
}
