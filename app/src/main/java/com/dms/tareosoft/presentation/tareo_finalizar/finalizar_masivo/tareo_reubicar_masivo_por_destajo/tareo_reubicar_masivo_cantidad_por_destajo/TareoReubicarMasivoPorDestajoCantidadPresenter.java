package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_cantidad_por_destajo;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.finalizar_masivo_por_destajo_cantidad.ITareoReubicarMasivoPorDestajoCantidadInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class TareoReubicarMasivoPorDestajoCantidadPresenter extends BasePresenter<ITareoReubicarMasivoPorDestajoCantidadContract.View>
        implements ITareoReubicarMasivoPorDestajoCantidadContract.Presenter {

    String TAG = TareoReubicarMasivoPorDestajoCantidadPresenter.class.getSimpleName();

    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;
    @Inject
    ITareoReubicarMasivoPorDestajoCantidadInteractor interactor;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferences;

    private ArrayList<AllEmpleadoRow> mAllPorDestajol = new ArrayList<>();

    @Inject
    TareoReubicarMasivoPorDestajoCantidadPresenter() {

    }

    @Override
    public void validarEmpleados(AllEmpleadoRow porJornal, int position) {
        Log.e(TAG, "codTareo: " + porJornal);
        getCompositeDisposable().add(interactor.validarResultadoEmpleadoParaReubicar(
                porJornal.getCodigoTareo(), porJornal.getCodigoEmpleado())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(tareos -> {
                    getView().mostrarDialogCantidadParaDestajo(porJornal, position, tareos.getCantidadProducida());
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    @Override
    public void setListAllJornal(ArrayList<AllEmpleadoRow> allPorJornal) {
        mAllPorDestajol = allPorJornal;
    }

    public ArrayList<ResultadoTareo> listResultadoTareo() {
        ArrayList<ResultadoTareo> listResultadoTareo = new ArrayList<>();
        for (AllEmpleadoRow AllEmpleadoRow : mAllPorDestajol) {
            ResultadoTareo resultadoTareo = new ResultadoTareo();
            String codResultado = AllEmpleadoRow.getCodigoDetalleTareo() + appDateTime.getFechaSincronizada(Constants.F_DATE_RESULTADO);
            resultadoTareo.setCodResultado(codResultado);
            resultadoTareo.setFkDetalleTareo(AllEmpleadoRow.getCodigoDetalleTareo());
            resultadoTareo.setFechaRegistro(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));
            resultadoTareo.setHoraRegistro(appDateTime.getFechaSincronizada(Constants.F_TIME_TAREO));//TODO FORMATO?
            resultadoTareo.setCantidad(AllEmpleadoRow.getCantProducida());
            resultadoTareo.setFkUsuarioInsert(preferences.getUsuario());
            resultadoTareo.setFlgEnvio(StatusDescargaServidor.PENDIENTE);
            listResultadoTareo.add(resultadoTareo);
        }
        return listResultadoTareo;
    }

    public ArrayList<AllEmpleadoRow> listResultadoXTareo() {
        ArrayList<AllEmpleadoRow> listResultadoXTareo = new ArrayList<>();
        for (AllEmpleadoRow AllEmpleadoRow : mAllPorDestajol) {
            AllEmpleadoRow AllEmpleadoRow1 = new AllEmpleadoRow();
            AllEmpleadoRow1.setCodigoTareo(AllEmpleadoRow.getCodigoTareo());
            AllEmpleadoRow1.setCantProducida(AllEmpleadoRow.getCantProducida());
            listResultadoXTareo.add(AllEmpleadoRow1);
        }
        return listResultadoXTareo;
    }
}