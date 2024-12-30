package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad;

import android.app.Dialog;
import android.os.Bundle;
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
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.presentation.adapter.ReubicarTipoEmpleadoCantidadAdapter;
import com.dms.tareosoft.presentation.adapter.ReubicarTipoTareoCantidadAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.TextUtil;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ReubicarTipoTareoCantidadFragment extends BaseFragment
        implements IReubicarTipoTareoCantidadContract.View {

    String TAG = ReubicarTipoTareoCantidadFragment.class.getSimpleName();
    public static String LISTA_ALL_POR_EMPLEADO = "allPorEmpleado";
    public static String LISTA_ALL_TAREO_END = "allTareoend";

    @BindView(R.id.rv_empleado)
    RecyclerView rvEmpleado;
    @BindView(R.id.rv_tareo)
    RecyclerView rvTareo;

    EditText etCantidadProducida;

    private Dialog dialogResultado;
    private View viewDialog;
    MaterialButton aceptar, cancelar;

    @Inject
    ReubicarTipoTareoCantidadPresenter presenter;

    private boolean FLAG_CANTIDAD;
    private ArrayList<AllEmpleadoRow> listaAllEmpleados = new ArrayList<>();
    private ArrayList<String> listaAllTareoEnd = new ArrayList<>();
    private ReubicarTipoEmpleadoCantidadAdapter mAdapterEmpleado;
    private ReubicarTipoTareoCantidadAdapter mAdapterTareo;

    public ReubicarTipoTareoCantidadFragment() {
    }

    public ReubicarTipoTareoCantidadFragment newInstance(List<AllEmpleadoRow> allPorEmpleado,
                                                         List<String> allTareoEnd) {
        ReubicarTipoTareoCantidadFragment fragment = new ReubicarTipoTareoCantidadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTA_ALL_POR_EMPLEADO, (Serializable) allPorEmpleado);
        bundle.putSerializable(LISTA_ALL_TAREO_END, (Serializable) allTareoEnd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_reubicar_tipo_tareo_cantidad;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void setupView() {
        setupRecyclerViewEmpleado();
        setupRecyclerViewTareo();
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
            listaAllTareoEnd = (ArrayList<String>) getArguments().getSerializable(LISTA_ALL_TAREO_END);
        }
        presenter.obtenerTareoConResultados(listaAllTareoEnd);

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
        //mostrarListAllTareoEnd(listaAllTareo);
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
            if (newListTipoEmpleado.size() <= 0)
                FLAG_CANTIDAD = true;
            if (newListTipoEmpleado != null
                    && newListTipoEmpleado.size() > 0) {
                rvEmpleado.setVisibility(View.VISIBLE);
                mAdapterEmpleado.setData(newListTipoEmpleado);
            } else {
                rvEmpleado.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void mostrarListAllTareoEnd(List<AllTareosWithResult> listAllTareo) {
        if (listAllTareo != null && listAllTareo.size() > 0) {
            rvTareo.setVisibility(View.VISIBLE);
            mAdapterTareo.setData(listAllTareo);
        } else {
            rvTareo.setVisibility(View.GONE);
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
    public void mostrarDialogCantidadTipoEmpleado(AllEmpleadoRow allEmpleadoRow, int position) {
        dialogResultado.show();
        etCantidadProducida.setText("");
        aceptar.setOnClickListener(v -> {
            dialogResultado.dismiss();
            int cantidad = Integer.parseInt(etCantidadProducida.getText().toString());
            if (cantidad > 0) {
                allEmpleadoRow.setCantProducida(TextUtil.convertToDouble(etCantidadProducida.getText().toString()));
                updateListItemEmpleado(allEmpleadoRow, position);
            } else {
                showErrorMessage("La cantidad ingresada debe ser mayor a 0",
                        "La cantidad ingresada debe ser mayor a 0");
            }
        });

        cancelar.setOnClickListener(v -> {
            dialogResultado.dismiss();
        });
    }

    @Override
    public void mostrarDialogCantidadTipoTareo(TareoRow tareoRow, int position) {
        dialogResultado.show();
        etCantidadProducida.setText("");
        aceptar.setOnClickListener(v -> {
            dialogResultado.dismiss();
            int cantidad = Integer.parseInt(etCantidadProducida.getText().toString());
            if (cantidad > 0) {
                /*tareoRow.setCantProducida(TextUtil.convertToDouble(etCantidadProducida.getText().toString()));
                updateListItemTareo(tareoRow, position);*/
                presenter.guardarResultadoPorTareo(presenter.resultadoPorTareo(tareoRow.getCodigo(),
                        cantidad));
            } else {
                showErrorMessage("La cantidad ingresada debe ser mayor a 0",
                        "La cantidad ingresada debe ser mayor a 0");
            }
        });

        cancelar.setOnClickListener(v -> {
            dialogResultado.dismiss();
        });
    }

    @Override
    public void actualizarResultado() {
        presenter.obtenerTareoConResultados(listaAllTareoEnd);
    }

    private void updateListItemEmpleado(AllEmpleadoRow allEmpleadoRow, int position) {
        mAdapterEmpleado.setItem(allEmpleadoRow, position);
        FLAG_CANTIDAD = mAdapterEmpleado.containZero();
    }

    private void updateListItemTareo(TareoRow tareoRow, int position) {
        mAdapterTareo.setItem(tareoRow, position);
        //FLAG_CANTIDAD = mAdapterTareo.containZero();
    }

    private void setupRecyclerViewEmpleado() {
        rvEmpleado.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapterEmpleado = new ReubicarTipoEmpleadoCantidadAdapter();
        rvEmpleado.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        mAdapterEmpleado.setListener((item, position) -> {
            presenter.validarEmpleados(item, position);
        });
        rvEmpleado.setAdapter(mAdapterEmpleado);
    }

    private void setupRecyclerViewTareo() {
        rvTareo.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapterTareo = new ReubicarTipoTareoCantidadAdapter(getContext());
        rvTareo.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        mAdapterTareo.setListener((item, position) -> {
            presenter.validarTareo(item, position);
        });
        rvTareo.setAdapter(mAdapterTareo);
    }
}
