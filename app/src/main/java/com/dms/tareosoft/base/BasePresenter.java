package com.dms.tareosoft.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<T extends IBaseContract.View> implements IBaseContract.Presenter<T> {
    private T view;
    private CompositeDisposable disposables = new CompositeDisposable();
    public Disposable disposable;

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;

        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        if (disposables != null) {
            disposables.dispose();
        }
      /*  if(disposable != null){
            disposable.dispose();
        }*/
    }

    @Override
    public boolean isViewAttached() {
        return view != null;
    }

    @Override
    public CompositeDisposable getCompositeDisposable() {
        return disposables;
    }

}