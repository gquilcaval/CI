package com.dms.tareosoft.presentation.tareo_resultado.resultadoPorEmpleado;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.ResultadoPorEmpleadoModificado;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.EmpleadoResultadoAdapter;
import com.dms.tareosoft.presentation.models.ResultadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.FinalizarMasivoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.TextUtil;
import com.google.android.material.button.MaterialButton;
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

public class ResultadoPorEmpleadoActivity extends BaseActivity
        implements IResultadoPorEmpleadoContract.View {

    String TAG = ResultadoPorEmpleadoActivity.class.getSimpleName();

    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvEmpleadosTareo;
    @BindView(R.id.tv_totalAsignados)
    TextView tvTotalAsignados;
    @BindView(R.id.tv_totalPendientes)
    TextView tvTotalPendientes;
    @BindView(R.id.et_codNuevoEmpTareo)
    TextInputEditText etCodNuevoEmpTareo;
    private EditText etCantidadProducida;

    @Inject
    ResultadoPorEmpleadoPresenter presenter;

    private List<ResultadoRow> listaEmpleados;
    private EmpleadoResultadoAdapter adapterEmpleados;
    private String codEmpleado;
    private Dialog dialogResultado;
    private TareoRow mTareoRow;
    private ArrayList<TareoRow> listaAllTareos = new ArrayList<>();
    private ArrayList<String> listaCodigosTareos = new ArrayList<>();
    private ArrayList<String> listaFechasTareos = new ArrayList<>();

    private MaterialButton aceptar, cancelar;
    private boolean isHavePending;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            codEmpleado = intent.getStringExtra(Constants.SCANNER_STRING);
            presenter.validarEmpleados(codEmpleado, mTareoRow.getCodigo());
        }
    };

    public static Intent newInstance(Context context,
                                     TareoRow tareo) {
        Intent intent = new Intent(context, ResultadoPorEmpleadoActivity.class);
        intent.putExtra(Constants.EXT_TAREO, tareo);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.EXT_TAREO, mTareoRow);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
            throw new IllegalArgumentException("Activity cannot find  extras " + Constants.EXT_CODTAREO);
        }

        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_resultado, null);
        dialogResultado = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(viewDialog)
                .create();

        etCantidadProducida = viewDialog.findViewById(R.id.et_cantidadProducida);
        aceptar = viewDialog.findViewById(R.id.mb_aceptar);
        cancelar = viewDialog.findViewById(R.id.mb_cerrar);

        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);

        etCodNuevoEmpTareo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.validarEmpleados(etCodNuevoEmpTareo.getText().toString(),
                        mTareoRow.getCodigo());
                etCodNuevoEmpTareo.setText("");
            }
            return true;
        });

        rvEmpleadosTareo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoResultadoAdapter(listaEmpleados);
        rvEmpleadosTareo.setAdapter(adapterEmpleados);
        tvTotalAsignados.setText(mTareoRow.getCantTrabajadores() + "");

        presenter.attachView(this);
        presenter.setCodigoTareo(mTareoRow.getCodigo());
        presenter.obtenerEmpleados();

        listaAllTareos.add(mTareoRow);
        listaCodigosTareos.add(mTareoRow.getCodigo());
        listaFechasTareos.add(mTareoRow.getFechaInicio());

        toolbar.setTitle("Resultado por empleado");
    }

    @Override
    public void mostrarEmpleados(List<ResultadoRow> lista) {
        int resultPendig = (mTareoRow.getCantTrabajadores() - lista.size());
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
        isHavePending = resultPendig == 0;
        tvTotalPendientes.setText("" + resultPendig);
    }

    @Override
    public void mostrarDialogoResultado(int cantProducida,
                                        @ResultadoPorEmpleadoModificado int result) {
        dialogResultado(cantProducida, result);
    }

    @Override
    public void mostrarMensajeEmpleadoConResultado(int cantProducida,
                                                   @ResultadoPorEmpleadoModificado int result) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("El trabajador ya cuenta con un resultado,\n" +
                        "¿Está seguro de modificar esta cantidad?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setNegativeButtonLabel("Cancelar")
                .setPositiveButtonlistener(() -> {
                    dialogResultado(cantProducida, result);
                })
                .setNegativeButtonlistener(() -> {
                });
        dialog.build().show();
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
                endTareo();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void endTareo() {
        String message = "";
        String title = "";
        switch (mTareoRow.getTipoTareo()) {
            case TypeTareo.TIPO_TAREO_DESTAJO:
                if (!isHavePending) {
                    mostrarMensajeTareoPorDestajo();
                    return;
                }
                break;
        }
        switch (mTareoRow.getStatusTareo()) {
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
        }
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
        return R.layout.activity_resultado;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SCANNER_ACTION);
        this.registerReceiver(this.mScanReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mScanReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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

    private void mostrarMensajeCantidadCero() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("La cantidad ingresada debe ser mayor a 0,\n" +
                        "Verifique el valor y vuelva a intentarlo")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void mostrarMensajeTareoPorDestajo() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("El tareo a finalizar fue creado con resultado tipo destajo " +
                        "Ingrese los resultados antes de finalizar el tareo")
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
                        case StatusTareo.TAREO_ACTIVO:
                            startActivity(FinalizarMasivoActivity.newInstance(this,
                                    listaCodigosTareos,
                                    listaAllTareos,
                                    listaFechasTareos));
                            break;
                        case StatusTareo.TAREO_FINALIZADO:
                            presenter.liquidarTareo(mTareoRow.getCodigo());
                            break;
                    }
                    finish();
                })
                .setNegativeButtonlistener(() -> {

                });
        dialog.build().show();
    }

    private void dialogResultado(int cantProducida, @ResultadoPorEmpleadoModificado int result) {
        etCantidadProducida.setText(String.valueOf(cantProducida));
        aceptar.setOnClickListener(v -> {
            dialogResultado.dismiss();
            double cantidad = TextUtil.convertToDouble(etCantidadProducida.getText().toString());
            if (cantidad > 0) {
                presenter.guardarResultado(cantidad,
                        mTareoRow.getCodigo(), result);
            } else {
                mostrarMensajeCantidadCero();
            }
        });

        cancelar.setOnClickListener(v -> {
            dialogResultado.dismiss();
        });

        dialogResultado.show();
    }
}
