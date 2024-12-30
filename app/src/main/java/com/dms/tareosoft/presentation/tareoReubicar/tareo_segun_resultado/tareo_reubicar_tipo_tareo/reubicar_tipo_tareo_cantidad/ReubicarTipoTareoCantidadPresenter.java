package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad;

import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.IReubicarTipoEmpleadoCantidadInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ReubicarTipoTareoCantidadPresenter extends BasePresenter<IReubicarTipoTareoCantidadContract.View>
        implements IReubicarTipoTareoCantidadContract.Presenter {

    String TAG = ReubicarTipoTareoCantidadPresenter.class.getSimpleName();

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;

    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    @Inject
    IReubicarTipoEmpleadoCantidadInteractor interactor;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferences;

    private ArrayList<AllEmpleadoRow> mListAllResulPorEmpleado = new ArrayList<>();
    private ArrayList<ResultadoPorTareo> mListAllResulPorTareo = new ArrayList<>();

    @Inject
    ReubicarTipoTareoCantidadPresenter() {

    }

    @Override
    public void obtenerTareoConResultados(List<String> allTareoEnd) {
        Log.e(TAG, "obtenerTareoConResultados: " + allTareoEnd);
        getCompositeDisposable().add(interactor.obtenerTareoWithResult(allTareoEnd)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareos -> {
                    Log.e(TAG, "obtenerTareoConResultados subscribe: " + tareos);
                    getView().mostrarListAllTareoEnd(tareos);
                }, error -> {
                    Log.e(TAG, "obtenerTareoConResultados error: " + error);
                    Log.e(TAG, "obtenerTareoConResultados error: " + error.toString());
                    Log.e(TAG, "obtenerTareoConResultados error: " + error.getMessage());
                    Log.e(TAG, "obtenerTareoConResultados error: " + error.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    @Override
    public void validarEmpleados(AllEmpleadoRow porJornal, int position) {
        Log.e(TAG, "codTareo: " + porJornal);
        getCompositeDisposable().add(interactor.validarResultadoEmpleadoParaReubicar(
                porJornal.getCodigoTareo(), porJornal.getCodigoEmpleado())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareos -> {
                    getView().mostrarDialogCantidadTipoEmpleado(porJornal, position);
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    @Override
    public void validarTareo(TareoRow tareoRow, int position) {
        getView().mostrarDialogCantidadTipoTareo(tareoRow, position);
    }

    @Override
    public void setListAllEmpleados(ArrayList<AllEmpleadoRow> allPorJornal) {
        mListAllResulPorEmpleado = allPorJornal;
    }

    @Override
    public void guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo) {
        getCompositeDisposable().add(interactor.guardarResultadoPorTareo(resultadoPorTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(listResult -> {
                            getView().actualizarResultado();
                        }, error -> {
                            getView().showErrorMessage(getView().getMessage(R.string.error_empleado_nomina), error.getMessage());
                        }
                ));
    }

    public ArrayList<ResultadoTareo> listResultPorTareo() {
        ArrayList<ResultadoTareo> listResultPorTareo = new ArrayList<>();
        for (AllEmpleadoRow AllEmpleadoRow : mListAllResulPorEmpleado) {
            ResultadoTareo resultadoTareo = new ResultadoTareo();
            String codResultado = AllEmpleadoRow.getCodigoDetalleTareo() + appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO);
            resultadoTareo.setCodResultado(codResultado);
            resultadoTareo.setFkDetalleTareo(AllEmpleadoRow.getCodigoDetalleTareo());
            resultadoTareo.setFechaRegistro(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));
            resultadoTareo.setHoraRegistro(appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO));//TODO FORMATO?
            resultadoTareo.setCantidad(AllEmpleadoRow.getCantProducida());
            resultadoTareo.setFkUsuarioInsert(preferences.getUsuario());
            resultadoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
            listResultPorTareo.add(resultadoTareo);
        }
        return listResultPorTareo;
    }

    public ArrayList<AllEmpleadoRow> listResultPorEmpleado() {
        ArrayList<AllEmpleadoRow> listResultPorEmpleado = new ArrayList<>();
        for (AllEmpleadoRow AllEmpleadoRow : mListAllResulPorEmpleado) {
            AllEmpleadoRow AllEmpleadoRow1 = new AllEmpleadoRow();
            AllEmpleadoRow1.setCodigoTareo(AllEmpleadoRow.getCodigoTareo());
            AllEmpleadoRow1.setCantProducida(AllEmpleadoRow.getCantProducida());
            listResultPorEmpleado.add(AllEmpleadoRow1);
        }
        return listResultPorEmpleado;
    }
    public ArrayList<ResultadoPorTareo> listResultadoPorTareo() {
        return mListAllResulPorTareo;
    }

    public ResultadoPorTareo resultadoPorTareo(String codTareo, int cantidad) {
        String codResultado = codTareo + appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO);
        ResultadoPorTareo nuevo = new ResultadoPorTareo();
        nuevo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
        nuevo.setCodResultado(codResultado);
        nuevo.setFkTareo(codTareo);
        nuevo.setFechaRegistro(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
        nuevo.setHoraRegistro(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));
        nuevo.setCantidad(cantidad);
        nuevo.setFkUsuarioInsert(preferences.getUsuario());
        mListAllResulPorTareo.add(nuevo);
        return nuevo;
    }
}