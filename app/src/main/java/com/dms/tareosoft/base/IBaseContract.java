package com.dms.tareosoft.base;

import io.reactivex.disposables.CompositeDisposable;

public interface IBaseContract {
    interface View {
        void setErroresDetallados(Boolean erroresDetallados);

        void showSuccessMessage(String mensaje);

        void showProgressPercentage(String titulo, String mensaje);

        void showWarningMessage(String mensaje);

        void showErrorMessage(String mensaje, String detalle);

        void updateProgressbar(String mensaje);

        String getMessage(int id);

        void hiddenProgressPercentage();

        void showProgressbar(String titulo, String mensaje);

        void hiddenProgressbar();

        void updatePercentage(int progress, String message);

    }

    interface Presenter<T> {
        T getView();

        void attachView(T view);

        void detachView();

        boolean isViewAttached();

        CompositeDisposable getCompositeDisposable();
    }
}