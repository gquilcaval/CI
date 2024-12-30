package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.IReubicarTipoEmpleadoCantidadInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ReubicarTipoEmpleadoCantidadPresenter extends BasePresenter<IReubicarTipoEmpleadoCantidadContract.View>
        implements IReubicarTipoEmpleadoCantidadContract.Presenter {

    String TAG = ReubicarTipoEmpleadoCantidadPresenter.class.getSimpleName();

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

    @Inject
    ReubicarTipoEmpleadoCantidadPresenter() {

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
    public void setListAllEmpleados(ArrayList<AllEmpleadoRow> allPorJornal) {
        mListAllResulPorEmpleado = allPorJornal;
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
}