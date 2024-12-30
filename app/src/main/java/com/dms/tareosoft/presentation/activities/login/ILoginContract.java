package com.dms.tareosoft.presentation.activities.login;

import com.dms.tareosoft.base.IBaseContract;

interface ILoginContract {

    interface View extends IBaseContract.View {

        void goToMainMenu();

        void bloqueoLogin();

        void activarLogin();

        void viewMenuConfig(boolean viewMenu);
    }

    interface Presenter extends IBaseContract.Presenter<ILoginContract.View> {

        void validarViewOrNotMenu();

        void login(String username, String password);

        void validarModoTrabajo();

        Boolean validaDescargaExitosa();
    }
}
