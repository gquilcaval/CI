package com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porEmpleado;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.ReubicarPorEmpleadoAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
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

public class ListReubicarPorEmpleadoFragment extends BaseFragment implements IListaReubicarPorEmpleadoContract.View,
        View.OnClickListener {

    String TAG = ListReubicarPorEmpleadoFragment.class.getSimpleName();

    @BindView(R.id.rv_empleados)
    RecyclerView rvEmpleados;
    @BindView(R.id.tv_total_tareo)
    TextView tvTotalTareos;
    @BindView(R.id.tv_total_empleados_x_tareo)
    TextView tvTotalEmpleadosXTareo;
    @BindView(R.id.fab_finalizar_reubicar_masivo)
    FloatingActionButton fabFinalizarReubicarMasivo;

    @Inject
    ListaReubicarPorEmpleadoPresenter presenter;

    private ReubicarPorEmpleadoAdapter reubicarPorEmpleadoAdapter;

    public ListReubicarPorEmpleadoFragment() {
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_reubicar_por_empleado;
    }

    @Override
    protected void setupView() {
        setupRecyclerViews();
        presenter.attachView(this);
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    public void mostrarAllEmpleados(List<AllEmpleadoRow> listaAllEmpleados) {

        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = groupDataIntoHashMap(listaAllEmpleados);

        List<AllEmpleadoAdapter> dataList = new ArrayList<>();
        int count = 0;
        for (String header : groupedHashMap.keySet()) {
            AllEmpleadoAdapter dateItem = new AllEmpleadoAdapter();
            String title[] = header.split("/");
            dateItem.setConcepto1(title[0]);
            dateItem.setTypeView(TypeView.HEADER);
            dataList.add(dateItem);
            for (AllEmpleadoRow allEmpleadoNewRow : groupedHashMap.get(header)) {
                ++count;
                AllEmpleadoAdapter generalItem = new AllEmpleadoAdapter();
                generalItem.setData(allEmpleadoNewRow);
                generalItem.setTypeView(TypeView.BODY);
                dataList.add(generalItem);
            }
        }

        reubicarPorEmpleadoAdapter.setData(dataList);
        tvTotalTareos.setText(getString(R.string.total_ini_x,
                groupedHashMap.size()));
        tvTotalEmpleadosXTareo.setText(getString(R.string.total_all_empleados_x,
                count));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerAllEmpleadosWithTareo();
        presenter.obtenerTareosIniciados();
        presenter.clearListEmpleadoPorFinalizar();
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
        reubicarPorEmpleadoAdapter = new ReubicarPorEmpleadoAdapter();
        rvEmpleados.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        getContext(),
                        R.drawable.line_divider));
        reubicarPorEmpleadoAdapter.setListener((item, longPress, position) -> {
            Log.e(TAG, "item: " + item);
            if (!longPress) {
                if (presenter.getListEmpleadoPorFinalizar() != null
                        && presenter.getListEmpleadoPorFinalizar().size() > 0) {
                    presenter.removeListEmpleadoPorFinalizar(item.getData());
                    presenter.removeListaFechaTareos(item.getData().getFechaHora());
                    presenter.removeListTmpTareos(item.getData().getCodigoTareo());
                } else {
                    if (item != null) {
                        presenter.addListEmpleadoPorFinalizar(item.getData());
                        presenter.addListaFechaTareos(item.getData().getFechaHora());
                        int clase = presenter.claseTareoEmpleado(presenter.getListEmpleadoPorFinalizar());
                        if (!presenter.getListCodTareos().contains(item.getData().getCodigoTareo()))
                            presenter.addListCodTareos(item.getData().getCodigoTareo());
                        if (presenter.crearNewTareo(clase)) {
                            mostrarMensajeCrearTareoNew(clase);
                        } else {
                            mostrarMensajeOptionCrearTareoNew(clase);
                        }
                    }
                }
            } else {
                if (item != null) {
                    if (presenter.isMismaClase(item.getData())) {
                        if (presenter.isMismoResultado(item.getData())) {
                            int pos = presenter.getListEmpleadoPorFinalizar().indexOf(item.getData().getCodigoTareo());
                            if (pos < 0) { //validar que no se encuentre
                                if (!presenter.getListCodTareos().contains(item.getData().getCodigoTareo()))
                                    presenter.addListCodTareos(item.getData().getCodigoTareo());
                                presenter.addListEmpleadoPorFinalizar(item.getData());
                                presenter.addListaFechaTareos(item.getData().getFechaHora());
                                reubicarPorEmpleadoAdapter.updateItem(true, position);
                            } else {
                                presenter.removeListTmpTareos(item.getData().getCodigoTareo());
                                presenter.removeListEmpleadoPorFinalizar(item.getData());
                                presenter.removeListaFechaTareos(item.getData().getFechaHora());
                                reubicarPorEmpleadoAdapter.updateItem(false, position);
                            }
                        } else {
                            mostrarMensajeMismoResultado();
                        }
                    } else {
                        mostrarMensajeMismaClase();
                    }
                }
            }
        });
        rvEmpleados.setAdapter(reubicarPorEmpleadoAdapter);
    }

    private HashMap<String, List<AllEmpleadoRow>> groupDataIntoHashMap(List<AllEmpleadoRow> listAllEmpleados) {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoNewRow : listAllEmpleados) {
            if (!DateUtil.tiempoLimiteExcedido(allEmpleadoNewRow.getFechaHora(),
                    presenter.getcurrentDateTime(),
                    presenter.getTimeValidezTareo())) {
                String hashMapKey = allEmpleadoNewRow.getTareo() + "/" + allEmpleadoNewRow.getCodigoTareo();
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

    private void mostrarMensajeMismaClase() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("El empleado no pertenece a la misma clase no lo puede reubicar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void mostrarMensajeMismoResultado() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("El empleado no pertenece al mismo tipo de tareo o al mismo tipo de resultado")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void moveToNewTareo(int clase) {
        Intent intent = new Intent(getContext(), NewTareoNotEmployerActivity.class);
        intent.putExtra(Constants.Intent.CLASE, clase);
        startActivityForResult(intent, Constants.Intents.FOR_RESULT_NEW_TAREO);
    }
}
