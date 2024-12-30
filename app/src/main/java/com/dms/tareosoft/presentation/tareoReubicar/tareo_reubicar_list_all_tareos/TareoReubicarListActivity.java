package com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.TareoGroupSimpleAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TareoReubicarListActivity extends BaseActivity
        implements ITareoReubicarListContract.View {

    static String TAG = TareoReubicarListActivity.class.getSimpleName();

    public static String LISTA_ALL_EMPLEADOS = "allEmpleados";
    public static String horainicio = ""; //add
    private TareoGroupSimpleAdapter adapterTareo;

    @Inject
    TareoReubicarListPresenter presenter;
    @Inject
    Context context;
    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;
    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;

    public static Intent newInstance(Context context,
                                     List<AllEmpleadoRow> listaAllEmpleados, String horaInicio) {
        Intent intent = new Intent(context, TareoReubicarListActivity.class);
        TareoReubicarListActivity.horainicio = horaInicio;
        intent.putExtra(LISTA_ALL_EMPLEADOS, (Serializable) listaAllEmpleados);

        Log.e(TAG, "listaAllEmpleados: " + listaAllEmpleados);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);

        setupRecycler();

        ArrayList<AllEmpleadoRow> listaAllEmpleados = (ArrayList<AllEmpleadoRow>)
                getIntent().getSerializableExtra(LISTA_ALL_EMPLEADOS);
        Log.e(TAG, "listaAllEmpleados: " + listaAllEmpleados);

        presenter.setListAllEmpleados(listaAllEmpleados);
        presenter.obtenerTareosIniciados();

        toolbar.setTitle(getResources().getString(R.string.finalizar_empleados_x,
                listaAllEmpleados.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mostrarMensajeSalirSinReubicar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tareos_inciados;
    }

    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        if (lista != null && lista.size() > 0) {
            HashMap<String, List<TareoRow>> groupedHashMap = groupDataIntoHashMap(lista);
            List<GroupTareoRow> list = new ArrayList<>();
            for (String date : groupedHashMap.keySet()) {
                GroupTareoRow dateItem = new GroupTareoRow();
                dateItem.setNivel1(date);
                dateItem.setTypeView(TypeView.HEADER);
                list.add(dateItem);
                for (TareoRow pojoOfJsonArray : groupedHashMap.get(date)) {
                    GroupTareoRow generalItem = new GroupTareoRow();
                    generalItem.setData(pojoOfJsonArray);
                    generalItem.setTypeView(TypeView.BODY);
                    list.add(generalItem);
                }
            }
            adapterTareo.setData(list);
            tvTotalIniciados.setText(getString(R.string.total_ini_x,
                    lista.size()));
        }
    }

    @Override
    public void closeWindows() {
        finish();
    }

    @Override
    public void reubicarExitoso() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Exitoso")
                .setMessage("Los empleados y/o tareos han sido reubicados, " +
                        "de manera exitosa.")
                .setIcon(R.drawable.ic_done)
                .setPositiveButtonLabel("CONTINUAR")
                .setPositiveButtonlistener(() -> {
                    closeWindows();
                });
        dialog.build().show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void setupRecycler() {
        rvTareos.setLayoutManager(new LinearLayoutManager(this));
        adapterTareo = new TareoGroupSimpleAdapter();
        adapterTareo.setListener((view, tareo, position, longPress) -> {
            presenter.createNewListDetalleTareo(tareo.getCodigo());
        });
        rvTareos.setAdapter(adapterTareo);
    }

    private void mostrarMensajeSalirSinReubicar() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Los empleados y/o tareos han sido finalizados, " +
                        "si sale de está ventana no podrán ser reubicados.\n" +
                        "¿Está seguro de salir?\n")
                .setIcon(R.drawable.ic_help_outline)
                .setNegativeButtonLabel("CONTINUAR")
                .setPositiveButtonLabel("SALIR")
                .setNegativeButtonlistener(() -> {
                })
                .setPositiveButtonlistener(() -> {
                    onBackPressed();
                });
        dialog.build().show();
    }

    private HashMap<String, List<TareoRow>> groupDataIntoHashMap(List<TareoRow> ListTareos) {
        HashMap<String, List<TareoRow>> groupData = new HashMap<>();
        for (TareoRow tareoRow : ListTareos) {
            if (!DateUtil.tiempoLimiteExcedido(tareoRow.getFechaInicio(),
                    presenter.getcurrentDateTime(),
                    presenter.getTimeValidezTareo())) {
                String key = tareoRow.getConcepto1();
                if (groupData.containsKey(key)) {
                    groupData.get(key).add(tareoRow);
                } else {
                    List<TareoRow> list = new ArrayList<>();
                    list.add(tareoRow);
                    groupData.put(key, list);
                }
            } else {
                Log.e(TAG, "El tareo: " + tareoRow.getConcepto1() + " no se puede agregar por exceso de tiempo");
            }
        }
        return groupData;
    }
}
