package com.dms.tareosoft.presentation.acceso.consulta;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.presentation.incidencia.consulta.ConsultarIncidenciaPresenter;
import com.dms.tareosoft.presentation.incidencia.consulta.IConsultaIncidenciaContract;
import com.dms.tareosoft.presentation.incidencia.consulta.IncidenciaListAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ConsultarAccesoFragment extends BaseFragment implements IConsultaAccesoContract.View, View.OnClickListener {

    @Inject
    ConsultarAccesoPresenter presenter;
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

    private AccesoListAdapter adapter;
    private List<Acceso> resultList = new ArrayList<>();
    public ConsultarAccesoFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_consulta_acceso;
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
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Consultar Accesos");
        rvIncidencias.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvIncidencias.addItemDecoration(new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(getContext(), R.drawable.line_divider_gray));
        adapter = new AccesoListAdapter(resultList);
        rvIncidencias.setAdapter(adapter);
        presenter.attachView(this);

        // obtenes lista de acceso del dia de hoy
        obtenerListaAccesosToday();
    }



    @Override
    public void displayAccesos(List<Acceso> lista) {
        limpiarRecyclerview();
        this.resultList.addAll(lista);
        adapter.filterList((ArrayList<Acceso>) lista);
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

                presenter.searchAccesosByDate(etFechaIncioIncidencia.getText().toString().replace("/",""), etFechaFinIncidencia.getText().toString().replace("/",""));
                break;
        }
    }

    public void obtenerListaAccesosToday() {
        etFechaIncioIncidencia.setText(DateUtil.dateToStringFormat(new Date(), "YYYY/MM/dd"));
        etFechaFinIncidencia.setText(DateUtil.dateToStringFormat(new Date(), "YYYY/MM/dd"));
        presenter.searchAccesosByDate(etFechaIncioIncidencia.getText().toString().replace("/",""), etFechaFinIncidencia.getText().toString().replace("/",""));
    }
}
