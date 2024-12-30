package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porEmpleado;

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

public class ListaReubicarPorEmpleadoPresenter extends BasePresenter<IListaReubicarPorEmpleadoContract.View>
        implements IListaReubicarPorEmpleadoContract.Presenter {
    private final String TAG = ListaReubicarPorEmpleadoPresenter.class.getSimpleName();

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

    private List<AllEmpleadoRow> listEmpleadoPorFinalizar;
    private List<TareoRow> listaAllTareos;
    private List<String> listaFechaTareos;
    private List<String> listCodTareos;

    @Inject
    public ListaReubicarPorEmpleadoPresenter() {
        listEmpleadoPorFinalizar = new ArrayList<>();
        listaAllTareos = new ArrayList<>();
        listaFechaTareos = new ArrayList<>();
        listCodTareos = new ArrayList<>();
    }

    @Override
    public void obtenerAllEmpleadosWithTareo() {
        getCompositeDisposable().add(interactor.obtenerAllEmpleadosWithTareo(StatusTareo.TAREO_ACTIVO,
                StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO,
                preferenceManager.getUsuario())
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .doOnComplete(() -> {
                    //Log.e(TAG, "obtenerAllEmpleadosWithTareo doOnComplete: ");
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(allEmpleados -> {
                    //Log.e(TAG, "obtenerAllEmpleadosWithTareo subscribe: " + allEmpleados);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().mostrarAllEmpleados(allEmpleados);
                    }
                }, error -> {
                    /*Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error);
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.toString());
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.getMessage());
                    Log.e(TAG, "obtenerAllEmpleadosWithTareo error: " + error.getLocalizedMessage());*/
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
                    //Log.e(TAG, "obtenerTareosIniciados doOnComplete: ");
                    getView().hiddenProgressbar();
                    if (isViewAttached()) {
                        getView().showSuccessMessage("No se encontró");
                    }
                })
                .subscribe(tareos -> {
                    //Log.e(TAG, "obtenerTareosIniciados subscribe: " + tareos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        listaAllTareos.addAll(tareos);
                    }
                }, error -> {
                    /*Log.e(TAG, "obtenerTareosIniciados error: " + error);
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.toString());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getMessage());
                    Log.e(TAG, "obtenerTareosIniciados error: " + error.getLocalizedMessage());*/
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
    public List<AllEmpleadoRow> getListEmpleadoPorFinalizar() {
        return listEmpleadoPorFinalizar;
    }

    @Override
    public void addListEmpleadoPorFinalizar(AllEmpleadoRow allEmpleadoRow) {
        listEmpleadoPorFinalizar.add(allEmpleadoRow);
    }

    @Override
    public void removeListEmpleadoPorFinalizar(AllEmpleadoRow allEmpleadoRow) {
        listEmpleadoPorFinalizar.remove(allEmpleadoRow);
    }

    @Override
    public void clearListEmpleadoPorFinalizar() {
        listEmpleadoPorFinalizar.clear();
    }

    @Override
    public List<String> getListCodTareos() {
        return listCodTareos;
    }

    @Override
    public void addListCodTareos(String tareoRow) {
        listCodTareos.add(tareoRow);
    }

    @Override
    public void removeListTmpTareos(String tareoRow) {
        listCodTareos.remove(tareoRow);
    }

    @Override
    public void clearListTmpTareos() {
        listCodTareos.clear();
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
        int listTemp = listCodTareos.size();
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
    public boolean isMismaClase(AllEmpleadoRow item) {
        if (listEmpleadoPorFinalizar != null && listEmpleadoPorFinalizar.size() > 0) {
            for (AllEmpleadoRow data : listEmpleadoPorFinalizar) {
                /*Log.e(TAG, "mismaClase item: " + item);
                Log.e(TAG, "mismaClase data: " + data);*/
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
    public void moveToOneToOthers() {
        List<AllEmpleadoRow> listPorTareo = new ArrayList<>();
        for (AllEmpleadoRow allEmpleadoRow : listEmpleadoPorFinalizar) {
            switch (allEmpleadoRow.getTipoResultado()) {
                case TypeResultado.TIPO_RESULTADO_POR_TAREO:
                    listPorTareo.add(allEmpleadoRow);
                    break;
            }
        }

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

        Log.e(TAG, "listPorTareo: " + listPorTareo);
        Log.e(TAG, "listPorTareo hash: " + porTareo(listPorTareo));
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);
        Log.e(TAG, "listEmpleadoPorFinalizar: " + listEmpleadoPorFinalizar);
        Log.e(TAG, "listCodResulXTareo: " + listCodResulXTareo);

        if (listCodResulXTareo != null
                && listCodResulXTareo.size() > 0) {
            getView().moveToReubicarTipoTareoActivity(listEmpleadoPorFinalizar,
                    listCodResulXTareo,
                    listaFechaTareos);
        } else {
            getView().moveToReubicarTipoEmpleadoActivity(listEmpleadoPorFinalizar,
                    listaFechaTareos);
        }
    }

    @Override
    public void actionReubicar() {
        if (listEmpleadoPorFinalizar != null
                && listEmpleadoPorFinalizar.size() > 0) {
            int clase = claseTareoEmpleado(listEmpleadoPorFinalizar);
            if (crearNewTareo(clase))
                getView().mostrarMensajeCrearTareoNew(clase);
            else
                getView().mostrarMensajeOptionCrearTareoNew(clase);
        } else {
            getView().showWarningMessage("Debe Seleccionar al menos un empleado");
        }
    }

    @Override
    public boolean isMismoResultado(AllEmpleadoRow item) {
        if (listEmpleadoPorFinalizar != null && listEmpleadoPorFinalizar.size() > 0) {
            for (AllEmpleadoRow data : listEmpleadoPorFinalizar) {
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
