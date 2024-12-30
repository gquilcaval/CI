package com.dms.tareosoft.presentation.tareoConsulta;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.TareoConsultasAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.tareoConsulta.tareo_consulta_details.TareoConsultaDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TareoConsultaFragment extends BaseFragment implements ITareoConsultaContract.View {

    String TAG = TareoConsultaFragment.class.getSimpleName();

    @BindView(R.id.rv_empleados)
    RecyclerView rvAllTareos;
    @BindView(R.id.tv_total_empleados_x_tareo)
    TextView tvTotalEmpleadosXTareo;

    @Inject
    TareoConsultaPresenter presenter;

    private TareoConsultasAdapter mAdapter;

    public TareoConsultaFragment() {
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_tareo_consulta;
    }

    @Override
    protected void setupView() {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Consulta de tareos");
        setupRecyclerView();
        presenter.attachView(this);
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerAllTareos();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void initEvents() {
    }

    @Override
    public void mostrarAllTareos(List<AllTareoRow> allTareos) {
        mAdapter.setData(allTareos);

        tvTotalEmpleadosXTareo.setText(getResources().getString(R.string.total_tareo_x,
                getResources().getQuantityString(R.plurals.cant_tareos,
                        allTareos.size(), allTareos.size())));
    }

    private void setupRecyclerView() {
        mAdapter = new TareoConsultasAdapter(getContext());
        rvAllTareos.setHasFixedSize(true);
        rvAllTareos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllTareos.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        mAdapter.setListener(allTareoRow -> {
            startActivity(TareoConsultaDetailsActivity.newInstance(getContext(),
                    allTareoRow.getCodigoTareo()));
        });
        rvAllTareos.setAdapter(mAdapter);
    }
}
