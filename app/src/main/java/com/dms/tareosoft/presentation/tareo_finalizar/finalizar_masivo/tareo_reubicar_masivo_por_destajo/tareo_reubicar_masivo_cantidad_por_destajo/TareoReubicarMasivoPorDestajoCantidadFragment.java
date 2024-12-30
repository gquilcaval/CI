package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_cantidad_por_destajo;

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

public class TareoReubicarMasivoPorDestajoCantidadFragment extends BaseFragment
        implements ITareoReubicarMasivoPorDestajoCantidadContract.View {

    String TAG = TareoReubicarMasivoPorDestajoCantidadFragment.class.getSimpleName();
    public static String LISTA_ALL_POR_JORNAL = "allPorJornal";

    @BindView(R.id.rv_journal)
    RecyclerView rvJournal;

    EditText etCantidadProducida;

    private Dialog dialogResultado;
    private View viewDialog;
    MaterialButton aceptar, cancelar;

    @Inject
    TareoReubicarMasivoPorDestajoCantidadPresenter presenter;

    private boolean FLAG_CANTIDAD = false;
    private ArrayList<AllEmpleadoRow> mAllPorJornal = new ArrayList<>();
    private ReubicarTipoEmpleadoCantidadAdapter mAdapter;

    public TareoReubicarMasivoPorDestajoCantidadFragment() {
    }

    public TareoReubicarMasivoPorDestajoCantidadFragment newInstance(List<AllEmpleadoRow> allPorJornal) {
        TareoReubicarMasivoPorDestajoCantidadFragment fragment = new TareoReubicarMasivoPorDestajoCantidadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTA_ALL_POR_JORNAL, (Serializable) allPorJornal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tareo_reubicar_masivo_por_destajo_cantidad;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void setupView() {
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
        presenter.attachView(this);
        presenter.setListAllJornal(mAllPorJornal);
        mostrarListParaJornal(mAllPorJornal);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
        if (getArguments() != null) {
            mAllPorJornal = (ArrayList<AllEmpleadoRow>) getArguments().getSerializable(LISTA_ALL_POR_JORNAL);
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
    public void mostrarListParaJornal(ArrayList<AllEmpleadoRow> listaPorJornal) {
        if (listaPorJornal != null && listaPorJornal.size() > 0) {
            Log.e(TAG, "mAllPorJornal: " + listaPorJornal);
            mAdapter.setData(listaPorJornal);
        }
    }

    @Override
    public ArrayList<ResultadoTareo> listaResultado() {
        return presenter.listResultadoTareo();
    }

    @Override
    public ArrayList<AllEmpleadoRow> listResultadoXTareo() {
        return presenter.listResultadoXTareo();
    }

    @Override
    public void mostrarDialogCantidadParaDestajo(AllEmpleadoRow porDestajo, int position, int cantidad) {
        dialogResultado.show();
        etCantidadProducida.setText(String.valueOf(cantidad));
        aceptar.setOnClickListener(v -> {
            porDestajo.setCantProducida(TextUtil.convertToDouble(etCantidadProducida.getText().toString()));
            updateListItem(porDestajo, position);
            dialogResultado.dismiss();
        });
        cancelar.setOnClickListener(v -> {
            dialogResultado.dismiss();
        });
    }

    private void updateListItem(AllEmpleadoRow porDestajo, int position) {
        mAdapter.setItem(porDestajo, position);
        FLAG_CANTIDAD = mAdapter.containZero();
    }
}
