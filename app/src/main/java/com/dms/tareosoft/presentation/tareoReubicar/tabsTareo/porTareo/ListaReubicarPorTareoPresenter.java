package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porTareo;

import android.util.Log;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.domain.tareo_reubicar.IReubicarInteractor;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ListaReubicarPorTareoPresenter extends BasePresenter<IListaReubicarPorTareoContract.View>
        implements IListaReubicarPorTareoContract.Presenter {

    private final String TAG = ListaReubicarPorTareoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IReubicarInteractor interactor;
    @Inject
    DateTimeManager dateTimeManager;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private List<AllEmpleadoRow> listaAllEmpleados;
    private List<TareoRow> listaAllTareos;
    private List<TareoRow> listTmpTareos;
    private List<String> listaFechaTareos;

    @Inject
    public ListaReubicarPorTareoPresenter() {
        listaAllEmpleados = new ArrayList<>();
        listaAllTareos = new ArrayList<>();
        listTmpTareos = new ArrayList<>();
        listaFechaTareos = new ArrayList<>();
    }

    @Override
    public void obtenerAllEmpleadosWithTareo() {
        getCompositeDisposable().add(interactor.obtenerAllEmpleadosWithTareo(StatusTareo.TAREO_ACTIVO,
                StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO,
                preferenceManager.getUsuario())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo doOnComplete: ");
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(allEmpleados -> {
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo subscribe: " + allEmpleados);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().mostrarAllEmpleados(allEmpleados);
                    }
                }, error -> {
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error);
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.toString());
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.getMessage());
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.getLocalizedMessage());
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    @Override
    public void obtenerTareosIniciados() {
        getCompositeDisposable().add(interactor.obtenerTareosIniciados(StatusTareo.TAREO_ACTIVO,
                preferenceManager.getCodigoEnvioUsuario(),
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
                    listaAllTareos.clear();
                    Log.e(TAG, "obtenerTareosIniciados subscribe: " + tareos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        listaAllTareos.addAll(tareos);
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
    public int getTimeValidezTareo() {
        return preferenceManager.getVigenciaTareo();
    }

    @Override
    public String getcurrentDateTime() {
        return dateTimeManager.getFechaSincronizada(Constants.F_DATE_RESULTADO);
    }

    @Override
    public List<TareoRow> getListaAllTareos() {
        return listaAllTareos;
    }

    @Override
    public List<String> getListaFechaTareos() {
        return listaFechaTareos;
    }

    @Override
    public void addListaFechaTareos(String fechaTareo) {
        listaFechaTareos.add(fechaTareo);
    }

    @Override
    public void removeListaFechaTareos(String fechaTareo) {
        listaFechaTareos.remove(fechaTareo);
    }

    @Override
    public void clearListaFechaTareos() {
        listaFechaTareos.clear();
    }

    @Override
    public List<AllEmpleadoRow> getListaAllEmpleados() {
        return listaAllEmpleados;
    }

    @Override
    public void addListaAllEmpleados(AllEmpleadoRow allEmpleadoRow) {
        listaAllEmpleados.add(allEmpleadoRow);
    }

    @Override
    public void removeListaAllEmpleados(AllEmpleadoRow allEmpleadoRow) {
        listaAllEmpleados.remove(allEmpleadoRow);
    }

    @Override
    public void clearListaAllEmpleados() {
        listaAllEmpleados.clear();
    }

    @Override
    public List<TareoRow> getListaTmpTareos() {
        return listTmpTareos;
    }

    @Override
    public void addListaTmpTareos(TareoRow tareoRow) {
        listTmpTareos.add(tareoRow);
    }

    @Override
    public void removeListTmpTareos(TareoRow tareoRow) {
        listTmpTareos.remove(tareoRow);
    }

    @Override
    public void clearListTmpTareos() {
        listTmpTareos.clear();
    }

    @Override
    public int claseTareoEmpleado(List<AllEmpleadoRow> listaAllEmpleados) {
        int clase = 0;
        for (AllEmpleadoRow data : listaAllEmpleados) {
            clase = data.getCodigoClase();
            break;
        }
        return clase;
    }


    @Override
    public boolean crearNewTareo(int clase) {
        int listTemp = listTmpTareos.size();
        int listTotal = 0;
        for (TareoRow tareoRow : listaAllTareos) {
            if (tareoRow.getCodigoClase() == clase) {
                listTotal = listTotal + 1;
            }
        }
        if (listTotal > listTemp)
            return false;
        else
            return true;
    }

    @Override
    public boolean isMismaClase(TareoRow item) {
        if (listTmpTareos != null && listTmpTareos.size() > 0) {
            for (TareoRow data : listTmpTareos) {
                Log.e(TAG, "isMismaClase item: " + item);
                Log.e(TAG, "isMismaClase data: " + data);
                if (data.getCodigoClase() == item.getCodigoClase()) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isMismoResultado(TareoRow item) {
        if (listTmpTareos != null && listTmpTareos.size() > 0) {
            for (TareoRow data : listTmpTareos) {
                Log.e(TAG, "isMismoResultado item: " + item);
                Log.e(TAG, "isMismoResultado data: " + data);
                if (data.getTipoResultado() == item.getTipoResultado()) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void moveToOneToOthers() {
        List<AllEmpleadoRow> listPorTareo = new ArrayList<>();
        for (AllEmpleadoRow allEmpleadoRow : listaAllEmpleados) {
            switch (allEmpleadoRow.getTipoResultado()) {
                case TypeResultado.TIPO_RESULTADO_POR_TAREO:
                    listPorTareo.add(allEmpleadoRow);
                    break;
            }
        }
        Log.e(TAG, "listPorTareo: " + listPorTareo);
        Log.e(TAG, "listPorTareo hash: " + porTareo(listPorTareo));
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);

        HashMap<String, List<AllEmpleadoRow>> porTareo = porTareo(listPorTareo);

        List<String> listCodResulXTareo = new ArrayList<>();
        for (String codTareo : porTareo.keySet()) {
            for (TareoRow tareo : listaAllTareos) {
                if (tareo.getCodigo().equalsIgnoreCase(codTareo)) {
                    int total = porTareo.get(codTareo).size();
                    Log.e(TAG, "total: " + total);
                    Log.e(TAG, "tareo.getCantTrabajadores(): " + total);
                    if (total == tareo.getCantTrabajadores()) {
                        listCodResulXTareo.add(codTareo);
                        Log.e(TAG, "tareo.getCantTrabajadores(): " + tareo.getCantTrabajadores());
                    }
                }
            }
        }

        if (listCodResulXTareo != null
                && listCodResulXTareo.size() > 0) {
            getView().moveToReubicarTipoTareoActivity(listaAllEmpleados,
                    listCodResulXTareo,
                    listaFechaTareos);
        } else {
            getView().moveToReubicarTipoEmpleadoActivity(listaAllEmpleados,
                    listaFechaTareos);
        }
    }

    @Override
    public void actionReubicar() {
        if (listaAllEmpleados.size() > 0) {
            int clase = claseTareoEmpleado(listaAllEmpleados);
            if (crearNewTareo(clase))
                getView().mostrarMensajeCrearTareoNew(clase);
            else
                getView().mostrarMensajeOptionCrearTareoNew(clase);
        } else {
            getView().showWarningMessage("Debe Seleccionar al menos un tareo para reubicar");
        }
    }

    private HashMap<String, List<AllEmpleadoRow>> porTareo(List<AllEmpleadoRow> listAllEmpleados) {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoNewRow : listAllEmpleados) {
            String hashMapKey = allEmpleadoNewRow.getCodigoTareo();
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(allEmpleadoNewRow);
            } else {
                List<AllEmpleadoRow> list = new ArrayList<>();
                list.add(allEmpleadoNewRow);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }
}
