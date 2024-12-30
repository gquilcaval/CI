package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_tareo;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class EmpleadoControlFragment extends BaseFragment implements IEmpleadoControlContract.View {
    private final String codigoTareo;
    private final String concepto1;
    private final String concepto2;
    private final String fechaInicio;

    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;

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
    EmpleadoControlPresenter presenter;

    public EmpleadoControlFragment(String codigoTareo, String concepto1, String concepto2, String fechaInicio){
        this.codigoTareo = codigoTareo;
        this.concepto1 = concepto1;
        this.concepto2 = concepto2;
        this.fechaInicio = fechaInicio;
        this.listaEmpleados = new ArrayList<>();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_control_empleados_pendientes;
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
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);

        rvEmpleados.setAdapter(adapterEmpleados);
        presenter.obtenerEmpleados(codigoTareo);

        tvConcepto1.setText(concepto1);
        tvConcepto2.setText(concepto2);
        tvFechaInicio.setText(DateUtil.changeStringDateTimeFormat(fechaInicio, Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));

        presenter.obtenerEmpleados(codigoTareo);
    }

    @Override
    public void mostrarEmpleados(List<EmpleadoRow> lista) {
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
        tvTotalEmpTareo.setText("Empleados pendientes "+lista.size());
    }

}
