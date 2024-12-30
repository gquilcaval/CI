package com.dms.tareosoft.presentation.tareoEditar.tareoEditUser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class EditarEmpleadosActivity extends BaseActivity
        implements IEditarEmpleadosContract.View {

    static String TAG = EditarEmpleadosActivity.class.getSimpleName();

    @BindView(R.id.rb_entrada)
    CheckBox cbIniTareo;
    @BindView(R.id.rb_salida)
    CheckBox cbIniTareoManual;
    @BindView(R.id.tv_tipo_ingreso)
    TextView tvTipoIngreso;
    @BindView(R.id.et_fechaIniEmpTareo)
    TextInputEditText etFechaIniEmpTareo;
    @BindView(R.id.et_horaIniEmpTareo)
    TextInputEditText etHoraIniEmpTareo;
    @BindView(R.id.tv_fechaIniEmpTareo)
    TextInputLayout tvFechaIniEmpTareo;
    @BindView(R.id.tv_horaIniEmpTareo)
    TextInputLayout tvHoraIniEmpTareo;
    @BindView(R.id.et_codNuevoEmpTareo)
    EditText etCodigoNuevoEmpleado;
    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvEmpleados;
    @BindView(R.id.tv_totalEmpTareo)
    TextView tvTotalEmpTareo;
    @Inject
    EditarEmpleadosPresenter presenter;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferenceManager;

    private String codigoTareo;
    private String mFechaInicio, mHoraInicio;
    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;
    private MediaPlayer mediaPlayer;
    private ContenidoAjustes contenidoAjustes;

    private boolean FLAG_EDIT = false;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);

            if (!TextUtils.isEmpty(barcode)) {
                saveFechaHora();
                presenter.evaluarEmpleado(barcode,
                        mFechaInicio,
                        mHoraInicio);
            }
        }
    };

    public static Intent newInstance(Context context, String codTareo) {
        Log.e(TAG, "newInstance codTareo : " + codTareo);
        Intent intent = new Intent(context, EditarEmpleadosActivity.class);
        intent.putExtra(Constants.EXT_CODTAREO, codTareo);
        return intent;
    }

    @Override
    public void playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.setVolume(0.09f, 0.09f);
        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        });
        mediaPlayer.start();
    }

    @Override
    public void salir() {
        finish();
    }

    @Override
    public void showDetailedErrorDialog(ArrayList<String> listaErrores) {
        UtilsMethods.showDetailedErrorDialog(this, listaErrores).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        contenidoAjustes = preferenceManager.getContenidoAjustes();

        if (getIntent().hasExtra(Constants.EXT_CODTAREO)) {
            codigoTareo = getIntent().getStringExtra(Constants.EXT_CODTAREO);
            presenter.cargarTareo(codigoTareo);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + Constants.EXT_CODTAREO);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar);

        toolbar.setTitle("Empleados (+/-)");
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mostrarMensajeConfirmacion();
                break;
            case android.R.id.home:
                if (FLAG_EDIT) {
                    mostrarMensajeConfirmacion();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        rvEmpleados.setLayoutManager(new LinearLayoutManager(this));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);
        rvEmpleados.setAdapter(adapterEmpleados);

        etCodigoNuevoEmpleado.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveFechaHora();
                presenter.evaluarEmpleado(etCodigoNuevoEmpleado.getText().toString(),
                        mFechaInicio,
                        mHoraInicio);
                etCodigoNuevoEmpleado.setText("");
            }
            return true;
        });

        presenter.attachView(this);
        cbIniTareo.setChecked(true);
        tvFechaIniEmpTareo.setVisibility(View.GONE);
        tvHoraIniEmpTareo.setVisibility(View.GONE);
        cbIniTareoManual.setChecked(false);
        setAdapters();
        presenter.cargarEmpleados(codigoTareo, StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO);
    }

    private void setAdapters() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (presenter.validarFecha(calendar.getTime(), etHoraIniEmpTareo.getText().toString())) {
                etFechaIniEmpTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
            }
            etFechaIniEmpTareo.clearFocus();//Quitamos el focus para que pueda volver a ser seleccionado despues del setText
        }, appDateTime.getYear(), appDateTime.getMonth(), appDateTime.getDay());

        datePickerDialog.setOnCancelListener(dialog -> {
            etFechaIniEmpTareo.clearFocus();
        });
        datePickerDialog.setOnDismissListener(dialog -> {
            etFechaIniEmpTareo.clearFocus();
        });

        etFechaIniEmpTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                datePickerDialog.show();
            }
        });
        etHoraIniEmpTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    if (presenter.validarHora(etFechaIniEmpTareo.getText().toString(), calendar.getTime())) {
                        etHoraIniEmpTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                    }
                    etHoraIniEmpTareo.clearFocus();
                }, appDateTime.getHour(), appDateTime.getMinute(), true);

                timePickerDialog.setOnCancelListener(dialog -> {
                    etHoraIniEmpTareo.clearFocus();
                });

                timePickerDialog.setOnDismissListener(dialog -> {
                    etHoraIniEmpTareo.clearFocus();
                });

                timePickerDialog.show();
            }
        });

        reiniciarHoraInicio();
        reiniciarFechaInicio();

        cbIniTareo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            validChecker(cbIniTareoManual, isChecked);
            if (isChecked) {
                tvFechaIniEmpTareo.setVisibility(View.GONE);
                tvHoraIniEmpTareo.setVisibility(View.GONE);
                tvTipoIngreso.setText(getString(R.string.automatico));
                reiniciarHoraInicio();
                reiniciarFechaInicio();
            }
        });

        cbIniTareoManual.setOnCheckedChangeListener((buttonView, isChecked) -> {
            validChecker(cbIniTareo, isChecked);
            if (isChecked) {
                tvFechaIniEmpTareo.setVisibility(View.VISIBLE);
                tvHoraIniEmpTareo.setVisibility(View.VISIBLE);
                tvTipoIngreso.setText(getString(R.string.prefijado));
                if (contenidoAjustes.isFechaHoraInicio()) {
                    etFechaIniEmpTareo.setEnabled(isChecked);
                    etHoraIniEmpTareo.setEnabled(isChecked);
                } else {
                    etFechaIniEmpTareo.setEnabled(!isChecked);
                    etHoraIniEmpTareo.setEnabled(!isChecked);
                }
            }
        });
    }

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
        return R.layout.activity_editar_empleados;
    }

    @Override
    public void mostrarEmpleados(List<EmpleadoRow> lista) {
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
        setTotal(listaEmpleados.size());
    }

    private void setTotal(int size) {
        String resumen = getString(R.string.total_emp) + " " + size;
        tvTotalEmpTareo.setText(resumen);
    }

    @Override
    public void agregarEmpleado(EmpleadoRow row) {
        FLAG_EDIT = true;
        this.listaEmpleados.add(row);
        adapterEmpleados.notifyDataSetChanged();
        setTotal(listaEmpleados.size());
    }

    @Override
    public void quitarEmpleado(int position) {
        FLAG_EDIT = true;
        this.listaEmpleados.remove(position);
        adapterEmpleados.notifyDataSetChanged();
        setTotal(listaEmpleados.size());
    }

    @Override
    public void reiniciarHoraInicio() {
        etHoraIniEmpTareo.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));
    }

    @Override
    public void reiniciarFechaInicio() {
        etFechaIniEmpTareo.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
    }

    @Override
    public List<DetalleTareo> getEmpleados() {
        return presenter.obtenerEmpleados();
    }

    @Override
    public void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton) {
        UtilsMethods.showConfirmDialog(this, titulo, mensaje, iPositiveButton).show();
    }

    @Override
    public boolean hasTareo() {
        return listaEmpleados.size() > 0;
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

    private void validChecker(CheckBox checkBox, boolean isChecked) {
        checkBox.setChecked(!isChecked);
    }

    private void saveFechaHora() {
        if (cbIniTareo.isChecked()) {
            mFechaInicio = presenter.fechaInicio();
            mHoraInicio = presenter.horaInicio();
        }
        if (cbIniTareoManual.isChecked()) {
            mFechaInicio = DateUtil.changeStringDateTimeFormat(etFechaIniEmpTareo.getText().toString(),
                    Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO);
            mHoraInicio = etHoraIniEmpTareo.getText().toString();
        }
    }

    private void mostrarMensajeConfirmacion() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Configuración")
                .setMessage("¿Desea guardar los cambios realizados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Guardar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                    presenter.guardarTareo();
                })
                .setNegativeButtonlistener(() -> {
                    onBackPressed();
                });
        dialog.build().show();
    }
}
