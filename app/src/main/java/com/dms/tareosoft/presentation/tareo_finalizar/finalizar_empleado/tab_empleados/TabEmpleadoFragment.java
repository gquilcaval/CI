package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_empleados;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.EstadoEmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TabEmpleadoFragment extends BaseFragment implements ITabEmpleadoContract.View{

    private List<EstadoEmpleadoRow> listaEmpleados;
    private EstadoEmpleadoSimpleAdapter adapterEmpleados;

    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvEmpleados;
    @BindView(R.id.tv_totalEmpTareo)
    TextView tvTotalEmpTareo;
    @BindView(R.id.tv_concepto1)
    TextView tvConcepto1;
    @BindView(R.id.tv_concepto2)
    TextView tvConcepto2;
    @BindView(R.id.tv_fechaInicio)
    TextView tvFechaInicio;

    @Inject
    TabEmpleadoPresenter presenter;

    private TareoRow tareoRow;

    public TabEmpleadoFragment(TareoRow tareoRow) {
        this.tareoRow = tareoRow;
        this.listaEmpleados = new ArrayList<>();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_finalizar_empleado;
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
        presenter.attachView(this);

        rvEmpleados.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EstadoEmpleadoSimpleAdapter(listaEmpleados, getContext());

        rvEmpleados.setAdapter(adapterEmpleados);
        presenter.obtenerEmpleados(tareoRow.getCodigo());

        tvConcepto1.setText(tareoRow.getConcepto1());
        tvConcepto2.setText(tareoRow.getConcepto2());
        tvFechaInicio.setText(DateUtil.changeStringDateTimeFormat(tareoRow.getFechaInicio(),
                Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
    }

    @Override
    public void mostrarEmpleados(List<EstadoEmpleadoRow> lista) {
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
        tvTotalEmpTareo.setText("Empleados " + lista.size());

    }


}
