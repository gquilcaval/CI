package com.dms.tareosoft.presentation.tareo_control;

import android.content.Context;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.SimpleTareoAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_control.nuevo.ControlEmpleadoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ListaTareoControlFragment extends BaseFragment implements IListaTareoControlContract.View {

    @Inject
    ListaTareoControlPresenter presenter;
    @Inject
    Context context;
    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;
    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;

    private List<GroupTareoRow> listaTareos;
    private SimpleTareoAdapter adapterTareo;

    public ListaTareoControlFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tareos_control;
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
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Control Tareo");

        rvTareos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaTareos = new ArrayList<>();
        adapterTareo = new SimpleTareoAdapter(getContext(), listaTareos);
        adapterTareo.setListener((view, item, position, longPress) -> {
            if (!longPress) {
                startActivity(ControlEmpleadoActivity.newInstance(getContext(), item.getCodigo(),
                        item.getConcepto1(), item.getConcepto2(), item.getFechaInicio()));
            }
        });

        rvTareos.setAdapter(adapterTareo);
        presenter.attachView(this);
    }


    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        listaTareos.clear();
        HashMap<String, List<TareoRow>> groupedHashMap = groupDataInHashMap(lista);
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
        listaTareos.addAll(list);
        adapterTareo.notifyDataSetChanged();
        tvTotalIniciados.setText(getString(R.string.total_ini_x,
                lista.size()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerTareosIniciados();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private HashMap<String, List<TareoRow>> groupDataInHashMap(List<TareoRow> dataList) {
        HashMap<String, List<TareoRow>> groupedHashMap = new HashMap<>();
        for (TareoRow tareoRow : dataList) {
            String hashMapKey = tareoRow.getConcepto1();
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(tareoRow);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<TareoRow> list = new ArrayList<>();
                list.add(tareoRow);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }
}
