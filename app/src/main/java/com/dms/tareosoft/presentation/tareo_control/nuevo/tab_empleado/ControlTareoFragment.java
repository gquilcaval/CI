package com.dms.tareosoft.presentation.tareo_control.nuevo.tab_empleado;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ControlTareoFragment extends BaseFragment implements IControlTareoContract.View {
    private final String codTareo;

    @BindView(R.id.et_codNuevoEmpTareo)
    TextInputEditText etCodNuevoEmpTareo;

    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvEmpleadosTareo;

    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;

    @Inject
    ControlTareoPresenter presenter;

    public ControlTareoFragment(String codTareo) {
        this.codTareo = codTareo;
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String codEmpleado = intent.getStringExtra(Constants.SCANNER_STRING);
            if (!TextUtils.isEmpty(codEmpleado))
                presenter.validarTrabajador(codTareo, codEmpleado);
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.fragment_control_nuevo;
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

        rvEmpleadosTareo.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);
        adapterEmpleados.setNew_icon(R.drawable.ic_visibility);

        rvEmpleadosTareo.setAdapter(adapterEmpleados);
        presenter.obtenerControles(codTareo);

        etCodNuevoEmpTareo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String codEmpleado = etCodNuevoEmpTareo.getText().toString();
                if (!TextUtils.isEmpty(codEmpleado)) {
                    presenter.validarTrabajador(codTareo, codEmpleado);
                    etCodNuevoEmpTareo.setText("");
                }
            }
            return true;
        });
    }

    @Override
    public void mostrarEmpleados(List<EmpleadoRow> lista) {
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SCANNER_ACTION);
        getContext().registerReceiver(this.mScanReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(this.mScanReceiver);
    }
}
