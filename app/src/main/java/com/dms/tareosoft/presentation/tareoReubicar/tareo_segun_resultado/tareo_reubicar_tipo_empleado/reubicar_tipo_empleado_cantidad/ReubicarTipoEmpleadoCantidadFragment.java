package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.adapter.ReubicarTipoEmpleadoCantidadAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.util.TextUtil;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ReubicarTipoEmpleadoCantidadFragment extends BaseFragment
        implements IReubicarTipoEmpleadoCantidadContract.View {

    String TAG = ReubicarTipoEmpleadoCantidadFragment.class.getSimpleName();
    public static String LISTA_ALL_POR_EMPLEADO = "allPorEmpleado";

    @BindView(R.id.rv_journal)
    RecyclerView rvJournal;

    EditText etCantidadProducida;

    private Dialog dialogResultado;
    private View viewDialog;
    MaterialButton aceptar, cancelar;

    @Inject
    ReubicarTipoEmpleadoCantidadPresenter presenter;

    private boolean FLAG_CANTIDAD;
    private ArrayList<AllEmpleadoRow> listaAllEmpleados = new ArrayList<>();
    private ReubicarTipoEmpleadoCantidadAdapter mAdapter;

    public ReubicarTipoEmpleadoCantidadFragment() {
    }

    public ReubicarTipoEmpleadoCantidadFragment newInstance(List<AllEmpleadoRow> allPorEmpleado) {
        ReubicarTipoEmpleadoCantidadFragment fragment = new ReubicarTipoEmpleadoCantidadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTA_ALL_POR_EMPLEADO, (Serializable) allPorEmpleado);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_reubicar_tipo_empleado_cantidad;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void setupView() {
        setupRecyclerView();
        presenter.attachView(this);
        presenter.setListAllEmpleados(listaAllEmpleados);
        mostrarListAllEmpleadosPorEmpleado(listaAllEmpleados);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
        if (getArguments() != null) {
            listaAllEmpleados = (ArrayList<AllEmpleadoRow>) getArguments().getSerializable(LISTA_ALL_POR_EMPLEADO);
        }

        viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resultado, null);
        dialogResultado = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setView(viewDialog)
                .create();
        etCantidadProducida = viewDialog.findViewById(R.id.et_cantidadProducida);
        aceptar = viewDialog.findViewById(R.id.mb_aceptar);
        cancelar = viewDialog.findViewById(R.id.mb_cerrar);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public boolean hasCantidad() {
        return FLAG_CANTIDAD;
    }

    @Override
    public void mostrarListAllEmpleadosPorEmpleado(ArrayList<AllEmpleadoRow> listaAllEmpleado) {
        if (listaAllEmpleado != null && listaAllEmpleado.size() > 0) {
            Log.e(TAG, "mAllPorJornal: " + listaAllEmpleado);
            ArrayList<AllEmpleadoRow> newListTipoEmpleado = new ArrayList<>();
            for (AllEmpleadoRow item : listaAllEmpleado) {
                switch (item.getTipoResultado()) {
                    case TypeResultado.TIPO_RESULTADO_POR_EMPLEADO:
                        newListTipoEmpleado.add(item);
                        break;
                }
            }
            mAdapter.setData(newListTipoEmpleado);
            FLAG_CANTIDAD = mAdapter.containZero();
        }
    }

    @Override
    public ArrayList<ResultadoTareo> listAllTareos() {
        return presenter.listResultPorTareo();
    }

    @Override
    public ArrayList<AllEmpleadoRow> listAllEmpleados() {
        return presenter.listResultPorEmpleado();
    }

    @Override
    public void mostrarDialogCantidadTipoEmpleado(AllEmpleadoRow porJornal, int position) {
        dialogResultado.show();
        etCantidadProducida.setText("");
        aceptar.setOnClickListener(v -> {
            dialogResultado.dismiss();
            if (!TextUtils.isEmpty(etCantidadProducida.getText().toString())) {
                int cantidad = Integer.parseInt(etCantidadProducida.getText().toString());
                if (cantidad > 0) {
                    porJornal.setCantProducida(TextUtil.convertToDouble(etCantidadProducida.getText().toString()));
                    updateListItem(porJornal, position);
                } else {
                    showErrorMessage("La cantidad ingresada debe ser mayor a 0",
                            "La cantidad ingresada debe ser mayor a 0");
                }
            } else {
                showErrorMessage("Este campo puede estar vacio",
                        "Este campo puede estar vacio");
            }
        });

        cancelar.setOnClickListener(v -> {
            dialogResultado.dismiss();
        });
    }

    private void updateListItem(AllEmpleadoRow porJornal, int position) {
        mAdapter.setItem(porJornal, position);
        FLAG_CANTIDAD = mAdapter.containZero();
    }

    private void setupRecyclerView() {
        rvJournal.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAdapter = new ReubicarTipoEmpleadoCantidadAdapter();
        rvJournal.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        mAdapter.setListener((item, position) -> {
            presenter.validarEmpleados(item, position);
        });
        rvJournal.setAdapter(mAdapter);
    }
}
