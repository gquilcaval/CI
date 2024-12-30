package com.dms.tareosoft.presentation.activities.splash;


import com.dms.tareosoft.base.IBaseContract;


interface ISplashContract {

    interface View extends IBaseContract.View {

        void showLogin(long time);
        void viewMessage(String message);
    }

    interface Presenter extends IBaseContract.Presenter<ISplashContract.View>{
        void setImei(String imei);
    }
}
