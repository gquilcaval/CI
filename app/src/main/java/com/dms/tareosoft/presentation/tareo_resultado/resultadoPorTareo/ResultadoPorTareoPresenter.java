package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorTareo;

import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_resultado.tareo_resultado_por_tareo.IResultadoPorTareoInteractor;
import com.dms.tareosoft.util.Constants;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ResultadoPorTareoPresenter extends BasePresenter<IResultadoPorTareoContract.View>
        implements IResultadoPorTareoContract.Presenter {

    String TAG = ResultadoPorTareoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    IResultadoPorTareoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    DateTimeManager appDateTime;

    private ResultadoPorTareo nuevo;
    private String mCodTareo;
    private double mCantidad;

    @Inject
    ResultadoPorTareoPresenter() {
        nuevo = new ResultadoPorTareo();
        nuevo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
        //TODO getView().setErroresDetallados(preferenceManager.getFlgErrores());
    }

    @Override
    public void setCodTareo(String codTareo) {
        mCodTareo = codTareo;
    }

    @Override
    public void obtenerListResult() {
        getCompositeDisposable().add(interactor.obtenerListResultforTareo(mCodTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(listResult -> {
                            if (listResult != null && listResult.size() > 0) {
                                getView().mostrarListResult(listResult);
                                Log.e(TAG, "listResult: " + listResult);
                            } else {
                                getView().showSuccessMessage("Sin informacion para mostrar");
                            }
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

    @Override
    public void setCantProd(double cantidad) {
        mCantidad = cantidad;
    }

    @Override
    public void guardarResultadoPorTareo() {
        getCompositeDisposable().add(interactor.guardarResultadoPorTareo(resultadoPorTareo())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(listResult -> {
                            obtenerListResult();
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

    private ResultadoPorTareo resultadoPorTareo() {
        String codResultado = mCodTareo + appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO);
        nuevo.setCodResultado(codResultado);
        nuevo.setFkTareo(mCodTareo);
        nuevo.setFechaRegistro(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
        nuevo.setHoraRegistro(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));
        nuevo.setCantidad(mCantidad);
        nuevo.setFkUsuarioInsert(preferences.getUsuario());
        return nuevo;
    }

    public void liquidarTareo(String codigoTareo) {
        //"El tareo no se encuentra finalizado."
        getCompositeDisposable().add(interactor.liquidarTareo(codigoTareo,
                appDateTime.getFechaSincronizada(Constants.F_DATE_TIME_TAREO),
                preferences.getUsuario())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(() -> {
                            getView().showSuccessMessage("El tareo se liquidó correctamente");
                            getView().salir();
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.empleado_noencontrado_tareo),
                                    error.getMessage());
                        }
                ));

    }
}
