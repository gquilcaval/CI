package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorTareo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.ResultadoPorTareoAdapter;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.FinalizarMasivoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class ResultadoPorTareoActivity extends BaseActivity
        implements IResultadoPorTareoContract.View {

    String TAG = ResultadoPorTareoActivity.class.getSimpleName();

    @BindView(R.id.txt_name_tareo)
    TextView txtNameTareo;
    @BindView(R.id.txt_cant_total)
    TextView txtCantProd;
    @BindView(R.id.et_cant_prod)
    TextInputEditText etCantProd;
    @BindView(R.id.rv_list_res_por_tareo)
    RecyclerView rvListResPorTareo;
    @Inject
    ResultadoPorTareoPresenter presenter;

    private ResultadoPorTareoAdapter mAdapter;

    private TareoRow mTareoRow;

    private ArrayList<TareoRow> listaAllTareos = new ArrayList<>();
    private ArrayList<String> listaCodigosTareos = new ArrayList<>();
    private ArrayList<String> listaFechasTareos = new ArrayList<>();

    public static Intent newInstance(Context context,
                                     TareoRow tareo) {
        Intent intent = new Intent(context, ResultadoPorTareoActivity.class);
        intent.putExtra(Constants.EXT_TAREO, tareo);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        outState.putSerializable(Constants.EXT_TAREO, mTareoRow);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        mTareoRow = (TareoRow) savedInstanceState.getSerializable(Constants.EXT_TAREO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        //consultarTareo
        if (getIntent().hasExtra(Constants.EXT_TAREO)) {
            mTareoRow = (TareoRow) getIntent().getSerializableExtra(Constants.EXT_TAREO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + Constants.EXT_CODTAREO + " " + Constants.EXT_TITLE_TAREO);
        }

        listaAllTareos.add(mTareoRow);
        listaCodigosTareos.add(mTareoRow.getCodigo());
        listaFechasTareos.add(mTareoRow.getFechaInicio());

        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);
        toolbar.setTitle("Resultado Por Tareo");

        etCantProd.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etCantProd.getText().length() > 0) {
                    double cantidad = Double.parseDouble(etCantProd.getText().toString());
                    if (cantidad == 0) {
                        showErrorMessage("El valor ingresado debe ser mayor o menor a 0",
                                "El valor ingresado debe ser mayor a 0");
                    } else {
                        presenter.setCantProd(cantidad);
                        presenter.guardarResultadoPorTareo();
                        etCantProd.setText("");
                    }
                } else {
                    showErrorMessage("No puede enviar valores vacios coloque una cantidad valida",
                            "No puede enviar valores vacios coloque una cantidad valida");
                }
            }
            return true;
        });
        setupRecyclerView();
        presenter.attachView(this);
        presenter.setCodTareo(mTareoRow.getCodigo());
        presenter.obtenerListResult();

        txtNameTareo.setText("Resultado por tareo");
    }

    @Override
    public void mostrarListResult(List<AllResultadoPorTareoRow> listResult) {
        mAdapter.setData(listResult);
        txtCantProd.setText(String.format("%s: %s",
                "Cantidad producida",
                String.valueOf(mAdapter.countTotal())));
    }

    @Override
    public void salir() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                String message = "";
                String title = "";
                switch (mTareoRow.getStatusTareo()) {
                    /*case StatusTareo.TAREO_ELIMINADO:
                        break;*/
                    case StatusTareo.TAREO_ACTIVO:
                        title = "Finalizar";
                        message = "¿Desea Finalizar el tareo?";
                        if (!tiempoLimiteExcedido(mTareoRow.getFechaInicio())) {
                            mostrarMensajeTareoVencido();
                        } else {
                            messageAction(message, title);
                        }
                        break;
                    case StatusTareo.TAREO_FINALIZADO:
                        title = "Liquidación";
                        message = "¿Desea liquidar el tareo?";
                        messageAction(message, title);
                        break;
                    /*case StatusTareo.TAREO_LIQUIDADO:
                        break;
                    default:
                        break;*/
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO QUE PUEDA REGISTRAR POR PARTES NO SOLO AL FINALIZAR
    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_resultado_por_tareo;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void setupRecyclerView() {
        rvListResPorTareo.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ResultadoPorTareoAdapter();
        rvListResPorTareo.addItemDecoration(
                new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                        this,
                        R.drawable.line_divider));
        rvListResPorTareo.setAdapter(mAdapter);
    }

    private boolean tiempoLimiteExcedido(String fechaInicial) {
        boolean excedio = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = sdf.parse(fechaInicial);
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

    private void mostrarMensajeTareoVencido() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("El tareo seleccionado tiene mas de 20 horas y no se puede finalizar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void messageAction(String message, String title) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_liquidar)
                .setPositiveButtonLabel("Aceptar")
                .setNegativeButtonLabel("Cancelar")
                .setPositiveButtonlistener(() -> {
                    switch (mTareoRow.getStatusTareo()) {
                        /*case StatusTareo.TAREO_ELIMINADO:
                            break;*/
                        case StatusTareo.TAREO_ACTIVO:
                            startActivity(FinalizarMasivoActivity.newInstance(this,
                                    listaCodigosTareos,
                                    listaAllTareos,
                                    listaFechasTareos));
                            break;
                        case StatusTareo.TAREO_FINALIZADO:
                            presenter.liquidarTareo(mTareoRow.getCodigo());
                            break;
                        /*case StatusTareo.TAREO_LIQUIDADO:
                            break;
                        default:
                            break;*/
                    }
                    salir();
                })
                .setNegativeButtonlistener(() -> {

                });
        dialog.build().show();
    }
}
