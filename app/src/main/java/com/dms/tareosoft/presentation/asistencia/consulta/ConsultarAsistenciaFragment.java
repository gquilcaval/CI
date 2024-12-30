package com.dms.tareosoft.presentation.asistencia.consulta;

import android.content.Context;
import android.util.Log;

import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.util.UtilsMethods;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;

public class ConsultarAsistenciaFragment extends BaseFragment implements IConsultaAsistenciaContract.View {

    @Inject
    ConsultarAsistenciaPresenter presenter;
    @Inject
    Context context;
    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvMarcaciones;
    @BindView(R.id.search_bar)
    SearchView searchView;


    private MarcacionesListAdapter adapter;
    private List<Marcacion> resultList = new ArrayList<>();
    public ConsultarAsistenciaFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_consulta_asistencia;
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
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Consultar Asistencia");
        rvMarcaciones.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvMarcaciones.addItemDecoration(new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(getContext(), R.drawable.line_divider_gray));
        adapter = new MarcacionesListAdapter(resultList);
        rvMarcaciones.setAdapter(adapter);
        presenter.attachView(this);

        onSearchViewListener();
    }


    @Override
    public void displayMarcaciones(List<Marcacion> lista) {
        limpiarRecyclerview();
        Log.d("AQUI", "" + lista);
        this.resultList.addAll(lista);
        adapter.filterList((ArrayList<Marcacion>) lista);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //showProgressbar("Busqueda","Validando asistente..");
                if (resultList.isEmpty()) {
                   // showErrorMessage("Personal no existente", "");

                } else {
                    presenter.searchMarcacionesEmpleado(query);
                }

                searchView.setQuery("",false);
                UtilsMethods.hideKeyboard((AppCompatActivity) getActivity());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (!query.isEmpty()) {
                    Log.d("AQUI", " BUSQUEDA -> " + query );
                    presenter.searchMarcacionesEmpleado(query);
                } else {
                    limpiarRecyclerview();
                }
                return false;
            }

        });
    }

    @Override
    public void limpiarRecyclerview() {
        this.resultList.clear();
        adapter.filterList(new ArrayList<Marcacion>());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
