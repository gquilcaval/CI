package com.dms.tareosoft.presentation.tareo_finalizar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.TareoPorFinalizarAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.FinalizarEmpleadoActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.FinalizarMasivoActivity;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PorFinalizarFragment extends BaseFragment
        implements IPorFinalizarContract.View {

    String TAG = PorFinalizarFragment.class.getSimpleName();

    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;
    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;

    private ArrayList<TareoRow> listaAllTareos;
    private ArrayList<String> listaCodigosTareos;
    private ArrayList<String> listaFechasTareos;
    private TareoPorFinalizarAdapter adapterTareo;

    @Inject
    PorFinalizarPresenter presenter;

    public PorFinalizarFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tareos_pendientes;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void setupView() {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Finalizar Tareo");

        listaAllTareos = new ArrayList<>();
        listaCodigosTareos = new ArrayList<>();
        listaFechasTareos = new ArrayList<>();

        setupRecycler();

        presenter.attachView(this);
    }


    @OnClick(R.id.fab_finalizar_masivo)
    public void submit(View view) {
        if (listaCodigosTareos.size() > 0) {
            startActivity(FinalizarMasivoActivity.newInstance(getContext(),
                    listaCodigosTareos,
                    listaAllTareos,
                    listaFechasTareos));
        } else {
            showWarningMessage(getString(R.string.minimo_seleccion));
        }
    }

    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        listaAllTareos.clear();
        listaAllTareos.addAll(lista);
        HashMap<String, List<TareoRow>> groupedHashMap = groupDataInHashMap(listaAllTareos);

        List<GroupTareoRow> list = new ArrayList<>();
        for (String date : groupedHashMap.keySet()) {
            GroupTareoRow dateItem = new GroupTareoRow();
            dateItem.setNivel1(date);
            dateItem.setTypeView(TypeView.HEADER);
            list.add(dateItem);
            for (TareoRow pojoOfJsonArray : groupedHashMap.get(date)) {
                GroupTareoRow generalItem = new GroupTareoRow();
                generalItem.setData(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
                generalItem.setTypeView(TypeView.BODY);
                list.add(generalItem);
            }
        }
        adapterTareo.setData(list);
        tvTotalIniciados.setText(getString(R.string.total_por_fin) + " " + lista.size());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerTareosIniciados();
        listaCodigosTareos.clear();
        listaFechasTareos.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void setupRecycler() {
        rvTareos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adapterTareo = new TareoPorFinalizarAdapter(getContext());
        adapterTareo.setListener((view, item, position, longPress) -> {
            Log.e(TAG, "Fecha del tareo " + item.getFechaInicio());
            if (presenter.validarTiempoExcedido(item.getFechaInicio())) {
                mostrarMensajeTareoVencido();
            } else {
                if (!longPress) {
                    if (listaCodigosTareos != null && listaCodigosTareos.size() > 0) {
                        listaCodigosTareos.remove(item.getCodigo());
                    } else {
                        startActivity(FinalizarEmpleadoActivity.newInstance(getContext(), item));
                    }
                } else {
                    int pos = listaCodigosTareos.indexOf(item.getCodigo());
                    if (pos < 0) { //validar que no se encuentre
                        listaCodigosTareos.add(item.getCodigo());
                        listaFechasTareos.add(item.getFechaInicio());
                        adapterTareo.updateItem(true, position);
                    } else {
                        adapterTareo.updateItem(false, position);
                    }
                }
            }
        });
        rvTareos.setAdapter(adapterTareo);
    }

   /* private boolean tiempoLimiteExcedido(String fechaActual) {
        boolean excedio = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = sdf.parse(fechaActual);
        } catch (ParseException ex) {

            ex.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 20);
        Date tempDate = cal.getTime();
        int resultDate = tempDate.compareTo(new Date());
        */

    /**
     * si resultDate es igual a cero son iguales
     * si resultDate es menor a cero tempDate es menor a new Date
     * si resultDate es mayor a cero tempdate es mayor a new date
     *//*
        Log.e(TAG, "valorcito: " + resultDate);
        switch (resultDate) {
            case -1:
                excedio = false;
                break;
            case 0:
                excedio = true;
                break;
            case 1:
                excedio = true;
                break;
        }
        return excedio;
    }*/
    private HashMap<String, List<TareoRow>> groupDataInHashMap(List<TareoRow> listOfPojosOfJsonArray) {
        HashMap<String, List<TareoRow>> groupedHashMap = new HashMap<>();
        for (TareoRow pojoOfJsonArray : listOfPojosOfJsonArray) {
            String hashMapKey = pojoOfJsonArray.getConcepto1();
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<TareoRow> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    private void mostrarMensajeTareoVencido() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("El tareo seleccionado tiene mas de " + presenter.horasValidesTareo() +
                        " horas y no se puede finalizar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }
}
