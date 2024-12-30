package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porTareo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.ReubicarPorTareoAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.NewTareoNotEmployerActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.ReubicarTipoEmpleadoActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.ReubicarTipoTareoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class ListReubicarPorTareoFragment extends BaseFragment implements IListaReubicarPorTareoContract.View,
        View.OnClickListener {

    String TAG = ListReubicarPorTareoFragment.class.getSimpleName();

    @BindView(R.id.rv_empleados)
    RecyclerView rvEmpleados;
    @BindView(R.id.tv_total_tareo)
    TextView tvTotalTareos;
    @BindView(R.id.tv_total_empleados_x_tareo)
    TextView tvTotalEmpleadosXTareo;
    @BindView(R.id.fab_finalizar_reubicar_masivo)
    FloatingActionButton fabFinalizarReubicarMasivo;

    @Inject
    ListaReubicarPorTareoPresenter presenter;

    private ReubicarPorTareoAdapter reubicarAdapter;
    private List<AllEmpleadoRow> mDataListAllEmpleados;

    public ListReubicarPorTareoFragment() {
        mDataListAllEmpleados = new ArrayList<>();
    }

    public static ListReubicarPorTareoFragment newInstance() {
        ListReubicarPorTareoFragment fragment = new ListReubicarPorTareoFragment();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_reubicar_por_tareo;
    }

    @Override
    protected void setupView() {
        presenter.obtenerAllEmpleadosWithTareo();
        presenter.obtenerTareosIniciados();
        presenter.clearListaAllEmpleados();
        setupRecyclerViews();
        presenter.attachView(this);
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    public void mostrarAllEmpleados(List<AllEmpleadoRow> listaAllEmpleados) {
        if (mDataListAllEmpleados != null)
            mDataListAllEmpleados.clear();
        mDataListAllEmpleados.addAll(listaAllEmpleados);

        HashMap<String, List<AllEmpleadoRow>> groupDataEmpleadosIntoHashMap = groupDataEmpleadosIntoHashMap(mDataListAllEmpleados);
        Log.e(TAG, "mostrarAllEmpleados groupDataEmpleadosIntoHashMap: " + groupDataEmpleadosIntoHashMap);
        Log.e(TAG, "mostrarAllEmpleados mDataListAllEmpleados: " + mDataListAllEmpleados);

        List<AllEmpleadoAdapter> listEmpleados = new ArrayList<>();
        int count = 0;
        for (String date : groupDataEmpleadosIntoHashMap.keySet()) {
            AllEmpleadoAdapter dateItem = new AllEmpleadoAdapter();
            dateItem.setConcepto1(date);
            dateItem.setTypeView(TypeView.HEADER);
            listEmpleados.add(dateItem);
            for (AllEmpleadoRow allEmpleadoNewRow : groupDataEmpleadosIntoHashMap.get(date)) {
                ++count;
                AllEmpleadoAdapter generalItem = new AllEmpleadoAdapter();
                generalItem.setData(allEmpleadoNewRow);
                generalItem.setTypeView(TypeView.BODY);
                listEmpleados.add(generalItem);
            }
        }

        HashMap<String, List<TareoRow>> groupDataTareoIntoHashMap = groupDataTareoIntoHashMap(presenter.getListaAllTareos());
        Log.e(TAG, "mostrarAllEmpleados groupDataTareoIntoHashMap: " + groupDataTareoIntoHashMap);

        List<GroupTareoRow> listTareos = new ArrayList<>();
        for (String date : groupDataTareoIntoHashMap.keySet()) {
            GroupTareoRow dateItem = new GroupTareoRow();
            dateItem.setNivel1(date);
            dateItem.setTypeView(TypeView.HEADER);
            listTareos.add(dateItem);
            for (TareoRow pojoOfJsonArray : groupDataTareoIntoHashMap.get(date)) {
                GroupTareoRow generalItem = new GroupTareoRow();
                generalItem.setData(pojoOfJsonArray);
                generalItem.setTypeView(TypeView.BODY);
                listTareos.add(generalItem);
            }
        }

        reubicarAdapter.setData(listTareos);
        tvTotalTareos.setText(getString(R.string.total_ini_x,
                groupDataEmpleadosIntoHashMap.size()));
        tvTotalEmpleadosXTareo.setText(getString(R.string.total_all_empleados_x,
                count));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerAllEmpleadosWithTareo();
        presenter.obtenerTareosIniciados();
        presenter.clearListaAllEmpleados();
        presenter.clearListTmpTareos();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED)
            return;
        switch (requestCode) {
            case Constants.Intents.FOR_RESULT_NEW_TAREO:
                presenter.moveToOneToOthers();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_finalizar_reubicar_masivo:
                presenter.actionReubicar();
                break;
        }
    }

    @Override
    protected void initEvents() {
        fabFinalizarReubicarMasivo.setOnClickListener(this);
    }


    @Override
    public void moveToReubicarTipoTareoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                                List<String> listAllTareoEnd,
                                                List<String> listaFechaTareos) {
        startActivity(ReubicarTipoTareoActivity.newInstance(getContext(),
                listaAllEmpleados, listAllTareoEnd, listaFechaTareos));
    }

    @Override
    public void moveToReubicarTipoEmpleadoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                                   List<String> listaFechaTareos) {

        Log.e(TAG, "listaAllEmpleados: " + listaAllEmpleados);
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);
        startActivity(ReubicarTipoEmpleadoActivity.newInstance(getContext(),
                listaAllEmpleados, listaFechaTareos));
    }

    private void setupRecyclerViews() {
        rvEmpleados.setLayoutManager(new LinearLayoutManager(getContext()));
        reubicarAdapter = new ReubicarPorTareoAdapter(getContext());
        rvEmpleados.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        reubicarAdapter.setListener((item, position, longPress) -> {
            if (longPress) {
                if (item != null) {
                    if (presenter.isMismaClase(item)) {
                        if (presenter.isMismoResultado(item)) {
                            if (presenter.getListaTmpTareos().contains(item)) {
                                reubicarAdapter.updateItem(position, false);
                                presenter.removeListTmpTareos(item);
                                for (AllEmpleadoRow allEmpleadoRow : mDataListAllEmpleados) {
                                    if (item.getCodigo().equals(allEmpleadoRow.getCodigoTareo())) {
                                        presenter.removeListaAllEmpleados(allEmpleadoRow);
                                    }
                                }
                            } else {
                                presenter.addListaTmpTareos(item);
                                reubicarAdapter.updateItem(position, true);
                                for (AllEmpleadoRow allEmpleadoRow : mDataListAllEmpleados) {
                                    if (item.getCodigo().equals(allEmpleadoRow.getCodigoTareo())) {
                                        presenter.addListaAllEmpleados(allEmpleadoRow);
                                    }
                                }
                            }
                            Log.e(TAG, "setListener presenter.getListaAllEmpleados(): " + presenter.getListaAllEmpleados());
                        } else {
                            mostrarMensajeTareoMismoResultado();
                        }
                    } else {
                        mostrarMensajeTareoMismaClase();
                    }
                }
            } else {
                if (item != null) {
                    if (presenter.getListaTmpTareos().contains(item)) {
                        reubicarAdapter.updateItem(position, false);
                        presenter.removeListTmpTareos(item);
                        for (AllEmpleadoRow allEmpleadoRow : mDataListAllEmpleados) {
                            if (item.getCodigo().equals(allEmpleadoRow.getCodigoTareo())) {
                                presenter.removeListaAllEmpleados(allEmpleadoRow);
                            }
                        }
                    }
                }
                Log.e(TAG, "se usara en caso se desee implementar el evento click");
            }
            return false;
        });
        rvEmpleados.setAdapter(reubicarAdapter);
    }

    private HashMap<String, List<AllEmpleadoRow>> groupDataEmpleadosIntoHashMap(List<AllEmpleadoRow> listAllEmpleados) {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoNewRow : listAllEmpleados) {
            if (!DateUtil.tiempoLimiteExcedido(allEmpleadoNewRow.getFechaHora(),
                    presenter.getcurrentDateTime(),
                    presenter.getTimeValidezTareo())) {
                String hashMapKey = allEmpleadoNewRow.getTareo();
                if (groupedHashMap.containsKey(hashMapKey)) {
                    groupedHashMap.get(hashMapKey).add(allEmpleadoNewRow);
                } else {
                    List<AllEmpleadoRow> list = new ArrayList<>();
                    list.add(allEmpleadoNewRow);
                    groupedHashMap.put(hashMapKey, list);
                }
            }
        }
        return groupedHashMap;
    }

    private HashMap<String, List<TareoRow>> groupDataTareoIntoHashMap(List<TareoRow> tareoRowList) {
        HashMap<String, List<TareoRow>> groupedHashMap = new HashMap<>();
        for (TareoRow tareoRow : tareoRowList) {
            if (!DateUtil.tiempoLimiteExcedido(tareoRow.getFechaInicio(),
                    presenter.getcurrentDateTime(),
                    presenter.getTimeValidezTareo())) {
                String hashMapKey = tareoRow.getConcepto1();
                if (groupedHashMap.containsKey(hashMapKey)) {
                    groupedHashMap.get(hashMapKey).add(tareoRow);
                } else {
                    List<TareoRow> list = new ArrayList<>();
                    list.add(tareoRow);
                    groupedHashMap.put(hashMapKey, list);
                }
            }
        }
        return groupedHashMap;
    }

    private void mostrarMensajeTareoMismaClase() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("El Tareo seleccionado pertenece a la misma clase no lo puede reubicar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void mostrarMensajeTareoMismoResultado() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("El Tareo seleccionado no pertenece al mismo tipo de tareo o al mismo tipo de resultado")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeOptionCrearTareoNew(int clase) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Para continuar puede asignar los empleados a un tareo nuevo o un tareo existente")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Nuevo")
                .setNegativeButtonLabel("Existente")
                .setPositiveButtonlistener(() -> {
                    moveToNewTareo(clase);
                })
                .setNegativeButtonlistener(() -> {
                    presenter.moveToOneToOthers();
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeCrearTareoNew(int clase) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Para continuar es necesario crear un tareo de la misma clase.\n¿Desea crear una nueva clase?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Crear")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                    moveToNewTareo(clase);
                })
                .setNegativeButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void moveToNewTareo(int clase) {
        Intent intent = new Intent(getContext(), NewTareoNotEmployerActivity.class);
        intent.putExtra(Constants.Intent.CLASE, clase);
        startActivityForResult(intent, Constants.Intents.FOR_RESULT_NEW_TAREO);
    }
}
