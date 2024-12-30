package com.dms.tareosoft.presentation.tareo_resultado;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.ResultadoAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.GroupTareoDetailRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_resultado.resultadoPorEmpleado.ResultadoPorEmpleadoActivity;
import com.dms.tareosoft.presentation.tareo_resultado.resultadoPorTareo.ResultadoPorTareoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ListaResultadosFragment extends BaseFragment
        implements IListaResultadosContract.View {

    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;
    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;

    @Inject
    ListaResultadosPresenter presenter;

    private List<TareoRow> listaTareos;
    private ResultadoAdapter adapterTareo;

    public ListaResultadosFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tareos_resultado;
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
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Resultado Tareo");
        listaTareos = new ArrayList<>();
        setupRecyclerView();
        presenter.attachView(this);
    }

    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        listaTareos.clear();
        listaTareos.addAll(lista);

        HashMap<String, List<TareoRow>> groupHashMap = groupDataInHashMap(listaTareos);
        List<GroupTareoDetailRow> list = new ArrayList<>();
        for (String date : groupHashMap.keySet()) {
            GroupTareoDetailRow dateItem = new GroupTareoDetailRow();
            dateItem.setNivel1(date);
            dateItem.setTypeView(TypeView.HEADER);
            list.add(dateItem);
            for (TareoRow tareoRowDetail : groupHashMap.get(date)) {
                GroupTareoDetailRow generalItem = new GroupTareoDetailRow();
                generalItem.setData(tareoRowDetail);
                generalItem.setTypeView(TypeView.BODY);
                list.add(generalItem);
            }
        }

        adapterTareo.setData(list);
        tvTotalIniciados.setText(getString(R.string.total_fin_x, lista.size()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerTareosIniciados();
    }

    private void setupRecyclerView() {
        rvTareos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterTareo = new ResultadoAdapter();
        adapterTareo.setListener(item -> {
            switch (item.getTipoResultado()) {
                case TypeResultado.TIPO_RESULTADO_POR_TAREO:
                    startActivity(ResultadoPorTareoActivity.newInstance(getContext(), item));
                    break;
                case TypeResultado.TIPO_RESULTADO_POR_EMPLEADO:
                    startActivity(ResultadoPorEmpleadoActivity.newInstance(getContext(), item));
                    break;
            }
        });
        rvTareos.setAdapter(adapterTareo);
    }

    private HashMap<String, List<TareoRow>> groupDataInHashMap(List<TareoRow> dataList) {
        HashMap<String, List<TareoRow>> groupedHashMap = new HashMap<>();
        for (TareoRow tareoRow : dataList) {
            String hashMapKey = tareoRow.getConcepto1();
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(tareoRow);
            } else {
                List<TareoRow> list = new ArrayList<>();
                list.add(tareoRow);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }
}
