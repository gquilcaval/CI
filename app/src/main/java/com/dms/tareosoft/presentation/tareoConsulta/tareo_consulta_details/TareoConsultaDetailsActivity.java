package com.dms.tareosoft.presentation.tareoConsulta.tareo_consulta_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.presentation.adapter.TareoConsultaDetailsAdapter;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.EditarTareoActivity;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditUser.EditarEmpleadosActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.Nullable;

public class TareoConsultaDetailsActivity extends BaseActivity
        implements ITareoConsultaDetailsContract.View, View.OnClickListener {

    String TAG = TareoConsultaDetailsActivity.class.getSimpleName();

    @BindView(R.id.tv_tareo)
    TextView tvTareo;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_fecha_inicio)
    TextView tvFechaInicio;
    @BindView(R.id.tv_fecha_fin)
    TextView tvFechaFin;
    @BindView(R.id.tv_cantidad_producida)
    TextView tvCantidadProducida;
    @BindView(R.id.rv_empleados_tareo)
    RecyclerView rvEmpleados;
    @BindView(R.id.fab_edit)
    FloatingActionButton fabEdit;

    @Inject
    TareoConsultaDetailsPresenter presenter;
    private TareoConsultaDetailsAdapter mAdapter;

    private String codTareo;
    private String fechaInicio;

    public static Intent newInstance(Context context, String codigo) {
        Intent intent = new Intent(context, TareoConsultaDetailsActivity.class);
        intent.putExtra(Constants.EXT_CODTAREO, codigo);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);
        toolbar.setTitle("Detalle de tareo");

        getActivityComponent().inject(this);
        if (getIntent().hasExtra(Constants.EXT_CODTAREO)) {
            codTareo = getIntent().getStringExtra(Constants.EXT_CODTAREO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + Constants.EXT_CODTAREO);
        }
        setupRecyclerView();
        presenter.setCodTareo(codTareo);
        presenter.attachView(this);
        presenter.obtenerDetailTareo();
        presenter.obtenerAllEmpleados();
        fabEdit.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tareo_consulta_iniciado;
    }

    @Override
    public void mostrarEmpleados(List<AllEmpleadosConsulta> lista) {
        mAdapter.setData(lista);
    }

    @Override
    public void mostrarDetalleTareo(TareoRow tareoRow) {
        Log.e(TAG,"mostrarDetalleTareo tareoRow: "+tareoRow);
        tvTareo.setText(tareoRow.getConcepto1() + " " + tareoRow.getConcepto2() + " " + tareoRow.getConcepto3() + "" +
                " " + tareoRow.getConcepto4() + " " + tareoRow.getConcepto5());
        String status = "";
        switch (tareoRow.getStatusTareo()) {
            case StatusTareo.TAREO_ELIMINADO:
                status = "Eliminado";
                fabEdit.setVisibility(View.GONE);
                break;
            case StatusTareo.TAREO_ACTIVO:
                status = "Activo";
                fabEdit.setVisibility(View.VISIBLE);
                break;
            case StatusTareo.TAREO_FINALIZADO:
                status = "Finalizado";
                fabEdit.setVisibility(View.GONE);
                break;
            case StatusTareo.TAREO_LIQUIDADO:
                status = "Liquidado";
                fabEdit.setVisibility(View.GONE);
                break;
        }
        tvStatus.setText(status);
        fechaInicio = tareoRow.getFechaInicio();
        tvFechaInicio.setText(DateUtil.changeStringDateTimeFormat(fechaInicio,
                Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
        if (!TextUtils.isEmpty(tareoRow.getFechaFin()))
            tvFechaFin.setText(DateUtil.changeStringDateTimeFormat(tareoRow.getFechaFin(),
                    Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
        tvCantidadProducida.setText(String.valueOf(tareoRow.getCantProducida()));
    }

    @Override
    public void closed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result, menu);
        menu.findItem(R.id.action_save).setVisible(false);
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

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void setupRecyclerView() {
        rvEmpleados.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new TareoConsultaDetailsAdapter(this);
        mAdapter.setNew_icon(R.drawable.ic_visibility);
        rvEmpleados.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_edit:
                viewMenu(v);
                break;
        }
    }

    private void viewMenu(View v) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.inflate(R.menu.popup_menu);
        if (!tiempoLimiteExcedido(fechaInicio)) {
            popup.getMenu().findItem(R.id.edit_menu).setVisible(false);
        }
        if (presenter.isAdmin()) {
            popup.getMenu().findItem(R.id.elim_menu).setVisible(true);
        } else {
            popup.getMenu().findItem(R.id.elim_menu).setVisible(false);
        }
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.elim_menu:
                    deleteTareo(codTareo);
                    return true;
                case R.id.edit_menu:
                    if (presenter.isAdmin()) {
                        startActivity(EditarTareoActivity.newInstance(this, codTareo));
                    } else {
                        startActivity(EditarEmpleadosActivity.newInstance(this, codTareo));
                    }
                    return true;
            }
            return false;
        });
        popup.show();
    }

    private void deleteTareo(String codTareo) {
        UtilsMethods.showConfirmDialog(this, "Confirmar",
                "¿Esta seguro de eliminar el tareo iniciado?", () -> {
                    presenter.deleteTareo(codTareo);
                }, () -> {
                }).show();
    }

    private boolean tiempoLimiteExcedido(String fechaActual) {
        boolean excedio = false;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.F_DATE_TIME_TAREO, Locale.getDefault());
        Date fecha = null;
        try {
            fecha = sdf.parse(fechaActual);
        } catch (ParseException ex) {

            ex.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 20);
        Date tempDate = cal.getTime();
        int resultDate = tempDate.compareTo(new Date());
        /**
         * si resultDate es igual a cero son iguales
         * si resultDate es menor a cero tempDate es menor a new Date
         * si resultDate es mayor a cero tempdate es mayor a new date
         */
        Log.e(TAG, "valorcito: " + resultDate);
        switch (resultDate) {
            case -1:
                excedio = false;
                break;
            case 0:
                excedio = true;
                break;
            case 1:
                excedio = true;
                break;
        }
        return excedio;
    }
}
