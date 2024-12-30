package com.dms.tareosoft.presentation.incidencia.consulta;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ConsultarIncidenciaFragment extends BaseFragment implements IConsultaIncidenciaContract.View, View.OnClickListener {

    @Inject
    ConsultarIncidenciaPresenter presenter;
    @Inject
    Context context;
    @BindView(R.id.rv_incidencias)
    RecyclerView rvIncidencias;
    @BindView(R.id.et_fechaInicioIncidencia)
    TextInputEditText etFechaIncioIncidencia;
    @BindView(R.id.et_fechaFinIncidencia)
    TextInputEditText etFechaFinIncidencia;
    @BindView(R.id.btn_search)
    ImageView btnSearch;

    private IncidenciaListAdapter adapter;
    private List<Incidencia> resultList = new ArrayList<>();
    public ConsultarIncidenciaFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_consulta_incidencia;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        etFechaIncioIncidencia.setOnClickListener(this);
        etFechaFinIncidencia.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    protected void setupView() {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Consultar Incidencias");
        rvIncidencias.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvIncidencias.addItemDecoration(new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(getContext(), R.drawable.line_divider_gray));
        adapter = new IncidenciaListAdapter(resultList);
        rvIncidencias.setAdapter(adapter);
        presenter.attachView(this);
    }


    @Override
    public void displayIncidencias(List<Incidencia> lista) {
        limpiarRecyclerview();
        this.resultList.addAll(lista);
        adapter.filterList((ArrayList<Incidencia>) lista);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void limpiarRecyclerview() {
        this.resultList.clear();
        adapter.filterList(new ArrayList<>());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_fechaInicioIncidencia:
                presenter.showDatePickerDialog(getFragmentManager(), etFechaIncioIncidencia, null, false);
                break;
            case R.id.et_fechaFinIncidencia:
                presenter.showDatePickerDialog(getFragmentManager(), etFechaFinIncidencia, null, true);
                break;
            case  R.id.btn_search:
                Log.d("AQUI", "" +etFechaFinIncidencia.getText().toString().replace("/",""));
                presenter.searchIncidenciasByDate(etFechaIncioIncidencia.getText().toString().replace("/",""), etFechaFinIncidencia.getText().toString().replace("/",""));
                break;
        }
    }
}
