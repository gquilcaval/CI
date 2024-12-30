package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.TareoReubicarListAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TareoReubicarMasivoListActivity extends BaseActivity
        implements ITareoReubicarMasivoListContract.View {

    public static String LISTA_ALL_EMPLEADOS = "allEmpleados";

    private List<TareoRow> listaTareos;
    private TareoReubicarListAdapter adapterTareo;

    @Inject
    TareoReubicarMasivoListPresenter presenter;

    @Inject
    Context context;

    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;

    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;

    public static Intent newInstance(Context context,
                                     ArrayList<AllEmpleadoRow> listAllEmpleados) {
        Intent intent = new Intent(context, TareoReubicarMasivoListActivity.class);
        intent.putExtra(LISTA_ALL_EMPLEADOS, listAllEmpleados);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);

        rvTareos.setLayoutManager(new LinearLayoutManager(this));
        listaTareos = new ArrayList<>();
        adapterTareo = new TareoReubicarListAdapter(this);
        adapterTareo.setListener((view, tareo, position, longPress) -> {
            presenter.createNewListDetalleTareo(tareo.getCodigo());
        });
        rvTareos.setAdapter(adapterTareo);

        ArrayList<AllEmpleadoRow> listaAllEmpleados = (ArrayList<AllEmpleadoRow>)
                getIntent().getSerializableExtra(LISTA_ALL_EMPLEADOS);

        presenter.setListAllEmpleados(listaAllEmpleados);

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
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.tareo_reubicar_masivo_list_activity;
    }

    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        listaTareos.clear();
        listaTareos.addAll(lista);
        adapterTareo.setData(listaTareos);
        tvTotalIniciados.setText(getString(R.string.total_ini_x,
                lista.size()));
    }

    @Override
    public void closeWindows() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.obtenerTareosIniciados();
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
}
