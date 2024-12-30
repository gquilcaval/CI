package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo;

import android.os.Handler;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.IReubicarTipoTareoInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class ReubicarTipoTareoPresenter extends BasePresenter<IReubicarTipoTareoContract.View>
        implements IReubicarTipoTareoContract.Presenter {

    private final String TAG = ReubicarTipoTareoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferences;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    IReubicarTipoTareoInteractor interactor;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private List<String> mListaFechaTareos;
    private List<AllEmpleadoRow> mListEmpleadoPorFinalizar;
    private List<TareoRow> mListaAllTareos;

    private String mFechaFinTareo = "";
    private String mHoraFinTareo = "";
    private String mFechaIniRefrigerio = "";
    private String mFechaFinRefrigerio = "";

    @StatusRefrigerio
    private int statusRefrigerio;

    @Inject
    public ReubicarTipoTareoPresenter() {
        mListaFechaTareos = new ArrayList<>();
        mListEmpleadoPorFinalizar = new ArrayList<>();
        mListaAllTareos = new ArrayList<>();
    }

    @Override
    public void getListAllTareos() {

        getCompositeDisposable().add(interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
                preferences.getCodigoEnvioUsuario(),
                StatusDescargaServidor.PENDIENTE)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "obtenerTareosIniciados doOnComplete: ");
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(tareos -> {
                    Log.e(TAG, "obtenerTareosIniciados subscribe: " + tareos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        mListaAllTareos.addAll(tareos);
                    }
                }, error -> {
                    Log.e(TAG, "obtenerTareosIniciados error: " + error);
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.toString());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getMessage());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));

    }

    @Override
    public List<TareoRow> getListAllTareo() {
        return mListaAllTareos;
    }

    @Override
    public void setListEmpleadoPorFinalizar(List<AllEmpleadoRow> listEmpleadoPorFinalizar) {
        mListEmpleadoPorFinalizar.addAll(listEmpleadoPorFinalizar);
    }

    @Override
    public List<AllEmpleadoRow> getListEmpleadoPorFinalizar() {
        return mListEmpleadoPorFinalizar;
    }

    @Override
    public void fechaFinTareo(String fechaFinTareo) {
        mFechaFinTareo = fechaFinTareo;
    }

    @Override
    public void horaFinTareo(String horaFinTareo) {
        mHoraFinTareo = horaFinTareo;
    }

    @Override
    public void fechaIniRefrigerio(String fechaIniRefrigerio) {
        mFechaIniRefrigerio = fechaIniRefrigerio;
    }

    @Override
    public void fechaFinRefrigerio(String fechaFinRefrigerio) {
        mFechaFinRefrigerio = fechaFinRefrigerio;
    }

    @Override
    public void setListaFechaTareos(List<String> listaFechaTareos) {
        mListaFechaTareos = listaFechaTareos;
    }

    @Override
    public void validarDatosParaFinalizar(boolean refrigerio,
                                          List<ResultadoTareo> listaResultado,
                                          List<AllEmpleadoRow> listResultadoXTareo) {

        boolean validaciones;
        if (refrigerio) {
            statusRefrigerio = StatusRefrigerio.SI_REFRIGERIO;
            validaciones = validarRefrigerio();
        } else {
            statusRefrigerio = StatusRefrigerio.NO_REFRIGERIO;
            validaciones = true;
            mFechaFinRefrigerio = "";
            mFechaIniRefrigerio = "";
        }

        if (validaciones) {
            validaciones = validarFechaFin();
        } else {
            getView().showErrorMessage("La hora de refrigerio no es correcta",
                    "La hora de refrigerio no es correcta");
            return;
        }

        if (validaciones) {
            crearResultadosEmpleado(listaResultado, listResultadoXTareo);
        } else {
            getView().showErrorMessage("La hora de refrigerio no es correcta",
                    "La hora de refrigerio no es correcta");
            return;
        }
    }

    @Override
    public boolean validarRefrigerio() {
        String fechaHoraFinTareo = mFechaFinTareo + " " + mHoraFinTareo;
        if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, mFechaFinRefrigerio,
                Constants.F_LECTURA, mFechaIniRefrigerio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_refrig_menor_ini_refrig));
            return false;
        } else if (DateUtil.esMenorQue(Constants.F_DATE_TIME_TAREO, fechaHoraFinTareo,
                Constants.F_DATE_TIME_TAREO, mFechaFinRefrigerio)) {
            getView().showWarningMessage(getView().getMessage(R.string.fin_tareo_menor_fin_refrig));
            return false;
        }

        int rangoRefrigerio = DateUtil.validarRangoMinutos(Constants.F_DATE_TIME_TAREO,
                mFechaIniRefrigerio, Constants.F_DATE_TIME_TAREO, mFechaFinRefrigerio,
                preferences.getMinimoRefrigerio(), preferences.getMaximoRefrigerio());
        if (rangoRefrigerio == Constants.ERROR_MENOR_MINIMO) {
            getView().showWarningMessage(String.format("Refrigerio debe de ser mayor o igual a %s minutos ",
                    preferences.getMinimoRefrigerio()));
            return false;
        } else if (rangoRefrigerio == Constants.ERROR_MAYOR_MAXIMO) {
            String unidadTiempo = "minutos";
            int tiempo = preferences.getMaximoRefrigerio();

            if (tiempo > 60) {
                tiempo = DateUtil.minutesToHour(tiempo);
                unidadTiempo = "hora(s)";
            }
            getView().showWarningMessage(String.format("Refrigerio debe de ser menor o igual a %s %s",
                    tiempo, unidadTiempo));
            return false;
        }
        return true;
    }

    @Override
    public boolean validarFechaFin() {
        String fechaHoraFinTareo = mFechaFinTareo + " " + mHoraFinTareo;
        for (String fechaInicioTareo : mListaFechaTareos) {
            if (DateUtil.esMenorQue(Constants.F_LECTURA, fechaHoraFinTareo,
                    Constants.F_LECTURA, fechaInicioTareo)) {
                getView().showWarningMessage(getView().getMessage(R.string.fin_menor_inicio));
                return false;
            }
        }
        return true;
    }

    @Override
    public void crearResultadosEmpleado(List<ResultadoTareo> listaResultado,
                                        List<AllEmpleadoRow> listResultadoXTareo) {

        List<String> listTareosFin = new ArrayList<>();
        List<String> listEmpleadosFin = new ArrayList<>();
        HashMap<String, List<AllEmpleadoRow>> porTareo = hashEmpleados(mListEmpleadoPorFinalizar);
        for (String codTareo : porTareo.keySet()) {
            for (TareoRow tareo : mListaAllTareos) {
                if (tareo.getCodigo().equalsIgnoreCase(codTareo)) {
                    if (porTareo.get(codTareo).size() == tareo.getCantTrabajadores()) {
                        listTareosFin.add(codTareo);
                    } else {
                        for (AllEmpleadoRow item : porTareo.get(codTareo)) {
                            listEmpleadosFin.add(item.getCodigoTareo());
                        }
                    }
                }
            }
        }

        Log.e(TAG, "crearResultados listEmpleadosFin: " + listEmpleadosFin);
        Log.e(TAG, "crearResultados listTareosFin: " + listTareosFin);
        Log.e(TAG, "crearResultados mListEmpleadoPorFinalizar: " + mListEmpleadoPorFinalizar);

        getCompositeDisposable().add(interactor.crearResultadoParaReubicarEmpleado(
                listaResultado, listResultadoXTareo)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(flag -> {
                    Log.e(TAG, "subscribe");
                    if (listEmpleadosFin != null && listEmpleadosFin.size() > 0
                            && listTareosFin != null && listTareosFin.size() > 0) {
                        finalizarTareosAndEmpleados(listTareosFin, listEmpleadosFin);
                        return;
                    }
                    if (listTareosFin != null && listTareosFin.size() > 0) {
                        finalizarTareos(listTareosFin);
                        return;
                    }
                    if (listEmpleadosFin != null && listEmpleadosFin.size() > 0) {
                        finalizarEmpleados(listEmpleadosFin);
                        return;
                    }
                }, error -> {
                    Log.e(TAG, "error: " + error.toString());
                    Log.e(TAG, "error: " + error.getLocalizedMessage());
                    Log.e(TAG, "error: " + error.getMessage());
                    Log.e(TAG, "error: " + error.getCause());
                }));
    }

    private void finalizarTareosAndEmpleados(List<String> listFinTareos,
                                             List<String> listFinEmpleados) {
        Log.e(TAG, "finalizarTareosAndEmpleados vas a finalizar " + listFinTareos.size() + " tareos");
        Log.e(TAG, "finalizarTareosAndEmpleados vas a finalizar " + listFinTareos);
        Log.e(TAG, "finalizarTareosAndEmpleados vas a finalizar " + listFinEmpleados.size() + " empleados");
        Log.e(TAG, "finalizarTareosAndEmpleados vas a finalizar " + listFinEmpleados);

        HashMap<String, List<String>> empleadosPorFinalizar = hashEmpleadosPorFinalizar(listFinEmpleados);
        Log.e(TAG, "finalizarTareosAndEmpleados  empleadosPorFinalizar: " + empleadosPorFinalizar);

        List<CantEmpleados> lisCantEmpleados = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : empleadosPorFinalizar.entrySet()) {
            Log.e(TAG, "finalizarTareosAndEmpleados clave: " + entry.getKey() + ", valor: " + entry.getValue().size());
            lisCantEmpleados.add(new CantEmpleados(entry.getKey(), entry.getValue().size()));
        }

        Log.e(TAG, "finalizarTareosAndEmpleados  lisCantEmpleados: " + lisCantEmpleados);

        List<String> listCodDetalleTareoEmpleado = new ArrayList<>();
        List<String> listCodEmpleadoEmpleado = new ArrayList<>();
        for (String cod : listFinEmpleados) {
            for (AllEmpleadoRow e : mListEmpleadoPorFinalizar) {
                if (cod.equals(e.getCodigoTareo())) {
                    if (!listCodDetalleTareoEmpleado.contains(e.getCodigoEmpleado()))
                        listCodDetalleTareoEmpleado.add(e.getCodigoEmpleado());
                    if (!listCodEmpleadoEmpleado.contains(e.getCodigoDetalleTareo()))
                        listCodEmpleadoEmpleado.add(e.getCodigoDetalleTareo());
                }
            }
        }

        HashMap<String, List<String>> tareosPorFinalizar = hashTareosPorFinalizar(listFinTareos);
        Log.e(TAG, "finalizarTareosAndEmpleados  tareosPorFinalizar: " + tareosPorFinalizar);

        List<String> listCodDetalleTareoTareo = new ArrayList<>();
        List<String> listCodTareoTareo = new ArrayList<>();

        for (String cod : listFinTareos) {
            for (AllEmpleadoRow e : mListEmpleadoPorFinalizar) {
                if (cod.equals(e.getCodigoTareo())) {
                    if (!listCodDetalleTareoTareo.contains(e.getCodigoDetalleTareo()))
                        listCodDetalleTareoTareo.add(e.getCodigoDetalleTareo());
                    if (!listCodTareoTareo.contains(e.getCodigoTareo()))
                        listCodTareoTareo.add(e.getCodigoTareo());
                }
            }
        }

        List<Single> observableList = new ArrayList<>(Arrays.asList(
                interactor.finalizarTareosParaReubicar(listCodDetalleTareoTareo, listCodTareoTareo,
                        mFechaFinTareo, mHoraFinTareo, statusRefrigerio, mFechaIniRefrigerio,
                        mFechaFinRefrigerio, StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO,
                        StatusEmpleado.EMPLEADO_NO_LIBRE, StatusTareo.TAREO_FINALIZADO),
                interactor.finalizarEmpleadoParaReubicar(listCodDetalleTareoEmpleado,
                        listCodEmpleadoEmpleado, statusRefrigerio, mFechaFinTareo, mHoraFinTareo, mFechaIniRefrigerio, mFechaFinRefrigerio,
                        StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO, StatusEmpleado.EMPLEADO_NO_LIBRE, lisCantEmpleados)
        ));

        getCompositeDisposable().add(Observable.fromIterable(observableList)
                .subscribeOn(ExecutorThread)
                .flatMap((Function<Single, ObservableSource<?>>) observable -> {
                    observable.observeOn(UiThread);
                    return observable.toObservable();
                })
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    getView().showSuccessMessage("Se finalizo los tareos y empleados  de manera correcta");
                    getView().moveToTareoReubicarListActivity(true);
                })
                .subscribe(list -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().showErrorMessage("No se pudo  finalizar los tareos y empleados.", throwable.getMessage());
                    }
                })
        );
    }

    @Override
    public void finalizarTareos(List<String> listFinTareos) {
        Log.e(TAG, "finalizarTareos vas a finalizar " + listFinTareos.size() + " tareos");
        Log.e(TAG, "finalizarTareos vas a finalizar " + listFinTareos);

        HashMap<String, List<String>> tareosPorFinalizar = hashTareosPorFinalizar(listFinTareos);
        Log.e(TAG, "finalizarTareos  tareosPorFinalizar: " + tareosPorFinalizar);

        List<String> listCodDetalleTareoTareo = new ArrayList<>();
        List<String> listCodTareoTareo = new ArrayList<>();

        for (String cod : listFinTareos) {
            for (AllEmpleadoRow e : mListEmpleadoPorFinalizar) {
                if (cod.equals(e.getCodigoTareo())) {
                    if (!listCodDetalleTareoTareo.contains(e.getCodigoDetalleTareo()))
                        listCodDetalleTareoTareo.add(e.getCodigoDetalleTareo());
                    if (!listCodTareoTareo.contains(e.getCodigoTareo()))
                        listCodTareoTareo.add(e.getCodigoTareo());
                }
            }
        }
        Log.e(TAG, "finalizarTareos listCodDetalleTareoTareo: " + listCodDetalleTareoTareo);
        Log.e(TAG, "finalizarTareos listCodTareoTareo: " + listCodTareoTareo);

        getCompositeDisposable().add(interactor.finalizarTareosParaReubicar(listCodDetalleTareoTareo,
                listCodTareoTareo, mFechaFinTareo, mHoraFinTareo, statusRefrigerio, mFechaIniRefrigerio,
                mFechaFinRefrigerio, StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO,
                StatusEmpleado.EMPLEADO_NO_LIBRE, StatusTareo.TAREO_FINALIZADO)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(flag -> {
                    getView().showSuccessMessage("Se finalizo los empleados  de manera correcta");
                    getView().moveToTareoReubicarListActivity(true);
                }, error -> {
                    getView().showErrorMessage(error.getMessage(), "");
                    Log.e(TAG, "Error: " + error.getMessage());
                    Log.e(TAG, "Error: " + error.toString());
                }));
    }

    @Override
    public void finalizarEmpleados(List<String> listFinEmpleados) {
        Log.e(TAG, "vas a finalizar " + listFinEmpleados.size() + " empleados");

        HashMap<String, List<String>> porFinalizar = hashEmpleadosPorFinalizar(listFinEmpleados);
        Log.e(TAG, "finalizarEmpleados  porFinalizar: " + porFinalizar);

        List<CantEmpleados> lisCantEmpleados = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : porFinalizar.entrySet()) {
            Log.e(TAG, "finalizarEmpleados clave: " + entry.getKey() + ", valor: " + entry.getValue().size());
            lisCantEmpleados.add(new CantEmpleados(entry.getKey(), entry.getValue().size()));
        }

        Log.e(TAG, "finalizarEmpleados  lisCantEmpleados: " + lisCantEmpleados);

        List<String> listCodDetalleTareo = new ArrayList<>();
        List<String> listCodEmpleado = new ArrayList<>();

        for (String cod : listFinEmpleados) {
            for (AllEmpleadoRow e : mListEmpleadoPorFinalizar) {
                if (cod.equals(e.getCodigoTareo())) {
                    if (!listCodEmpleado.contains(e.getCodigoEmpleado()))
                        listCodEmpleado.add(e.getCodigoEmpleado());
                    if (!listCodDetalleTareo.contains(e.getCodigoDetalleTareo()))
                        listCodDetalleTareo.add(e.getCodigoDetalleTareo());
                }
            }
        }

        Log.e(TAG, "finalizarEmpleados  listCodDetalleTareo: " + listCodDetalleTareo);
        Log.e(TAG, "finalizarEmpleados  listCodTareo: " + listCodEmpleado);

        getCompositeDisposable().add(interactor.finalizarEmpleadoParaReubicar(listCodDetalleTareo,
                listCodEmpleado, statusRefrigerio, mFechaFinTareo, mHoraFinTareo, mFechaIniRefrigerio, mFechaFinRefrigerio,
                StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO, StatusEmpleado.EMPLEADO_NO_LIBRE, lisCantEmpleados)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(flag -> {
                    getView().showSuccessMessage("Se finalizo los empleados  de manera correcta");
                    getView().moveToTareoReubicarListActivity(true);
                }, error -> {
                    getView().showErrorMessage(error.getMessage(), "");
                    Log.e(TAG, "Error: " + error.getMessage());
                    Log.e(TAG, "Error: " + error.toString());
                }));
    }

    private HashMap<String, List<AllEmpleadoRow>> hashEmpleados(List<AllEmpleadoRow> listAllEmpleados) {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoRow : listAllEmpleados) {
            String hashMapKey = allEmpleadoRow.getCodigoTareo();
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(allEmpleadoRow);
            } else {
                List<AllEmpleadoRow> list = new ArrayList<>();
                list.add(allEmpleadoRow);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    private HashMap<String, List<String>> hashEmpleadosPorFinalizar(List<String> listEmpleadoPorFinalizar) {
        HashMap<String, List<String>> groupedHashMap = new HashMap<>();
        for (String codTareo : listEmpleadoPorFinalizar) {
            if (groupedHashMap.containsKey(codTareo)) {
                groupedHashMap.get(codTareo).add(codTareo);
            } else {
                List<String> list = new ArrayList<>();
                list.add(codTareo);
                groupedHashMap.put(codTareo, list);
            }
        }
        return groupedHashMap;
    }

    private HashMap<String, List<String>> hashTareosPorFinalizar(List<String> listTareosPorFinalizar) {
        HashMap<String, List<String>> groupedHashMap = new HashMap<>();
        for (String codTareo : listTareosPorFinalizar) {
            if (groupedHashMap.containsKey(codTareo)) {
                groupedHashMap.get(codTareo).add(codTareo);
            } else {
                List<String> list = new ArrayList<>();
                list.add(codTareo);
                groupedHashMap.put(codTareo, list);
            }
        }
        return groupedHashMap;
    }
}