package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list.TareoReubicarMasivoListActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.TareoReubicarMasivoPorDestajoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FinalizarMasivoActivity extends BaseActivity implements IFinalizarMasivoContract.View {

    String TAG = FinalizarMasivoActivity.class.getSimpleName();

    public static String LISTA_TAREOS = "codTareos";
    public static String LISTA_ALL_TAREOS = "allTareos";
    public static String FECHAS_TAREOS = "fechasInicioTareos";
    private boolean FLAG_REFRIGERIO = false;
    private boolean FLAG_FECHA_FIN = false;

    @BindView(R.id.cb_fin_tareo)
    CheckBox mCbFinTareo;
    @BindView(R.id.tv_fechaIniTareo)
    TextInputLayout textInputLayoutFechaFin;
    @BindView(R.id.tv_horaIniTareo)
    TextInputLayout textInputLayoutHoraFin;
    @BindView(R.id.cb_horaRefrigerio)
    CheckBox cbHoraRefrigerio;
    @BindView(R.id.cb_reubicar)
    CheckBox cbReubicar;
    @BindView(R.id.lbl_fechaRefrigIni)
    TextView lblFechaRefrigIni;
    @BindView(R.id.lbl_fechaFinRefrig)
    TextView lblFechaFinRefrig;
    @BindView(R.id.tv_fechaIniRefrig)
    TextInputLayout tvFechaIniRefrig;
    @BindView(R.id.tv_horaIniRefrig)
    TextInputLayout tvHoraIniRefrig;
    @BindView(R.id.tv_horaFinRefrig)
    TextInputLayout tvHoraFinRefrig;
    @BindView(R.id.tv_fechaFinRefrig)
    TextInputLayout tvFechaFinRefrig;
    @BindView(R.id.et_fechaIniRefrig)
    TextInputEditText etFechaIniRefrig;
    @BindView(R.id.et_horaIniRefrig)
    TextInputEditText etHoraIniRefrig;
    @BindView(R.id.et_fechaFinRefrig)
    TextInputEditText etFechaFinRefrig;
    @BindView(R.id.et_horaFinRefrig)
    TextInputEditText etHoraFinRefrig;
    @BindView(R.id.et_fechaFinTareo)
    TextInputEditText etFechaFinTareo;
    @BindView(R.id.et_horaFinTareo)
    TextInputEditText etHoraFinTareo;

    @Inject
    FinalizarMasivoPresenter presenter;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferences;

    private ArrayList<String> listaCodTareos = new ArrayList<>();
    private ArrayList<String> listaFechasTareos = new ArrayList<>();
    private List<TareoRow> listaAllTareos = new ArrayList<>();

    private boolean autoFin;

    public static Intent newInstance(Context context,
                                     ArrayList<String> codigosTareo,
                                     ArrayList<TareoRow> listAllTareo,
                                     ArrayList<String> fechasTareo) {
        Intent intent = new Intent(context, FinalizarMasivoActivity.class);
        intent.putExtra(LISTA_TAREOS, codigosTareo);
        intent.putExtra(LISTA_ALL_TAREOS, listAllTareo);
        intent.putExtra(FECHAS_TAREOS, fechasTareo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        Toolbar toolbar = findViewById(R.id.ly_toolbar);
        setToolbar(toolbar);

        if (getIntent().hasExtra(LISTA_TAREOS) && getIntent().hasExtra(FECHAS_TAREOS)) {
            listaCodTareos = (ArrayList<String>) getIntent().getSerializableExtra(LISTA_TAREOS);
            listaFechasTareos = (ArrayList<String>) getIntent().getSerializableExtra(FECHAS_TAREOS);
            listaAllTareos = (ArrayList<TareoRow>) getIntent().getSerializableExtra(LISTA_ALL_TAREOS);
            presenter.setListaCodTareos(listaCodTareos);
            presenter.setListaFechaTareos(listaFechasTareos);

            toolbar.setTitle("Finalizar Tareos (" + listaCodTareos.size() + ")");
            presenter.obtenerAllEmpleados(listaCodTareos);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + LISTA_TAREOS);
        }
        setAdapters();
        autoFin = preferences.getFechaHoraFinManual();
        showCheckAuto(autoFin);
        if (listaAllTareos.size() > 1) {
            cbReubicar.setVisibility(View.VISIBLE);
        } else {
            cbReubicar.setVisibility(View.GONE);
        }
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
                if (cbReubicar.isChecked()) {
                    mostrarMensajeParaReubicar();
                } else {
                    presenter.finalizarTareo(cbHoraRefrigerio.isChecked());
                }
                break;
            case android.R.id.home:
                if (FLAG_REFRIGERIO || FLAG_FECHA_FIN) {
                    mostrarMensajeConfirmacion();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarMensajeConfirmacion() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Configuración")
                .setMessage("¿Desea guardar los cambios realizados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Guardar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                    presenter.finalizarTareo(cbHoraRefrigerio.isChecked());
                })
                .setNegativeButtonlistener(() -> {
                    onBackPressed();
                });
        dialog.build().show();
    }

    private void mostrarMensajeParaReubicar() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atención")
                .setMessage("¿Desea reubicar a todo el personal elegido?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("SI")
                .setNegativeButtonLabel("NO")
                .setPositiveButtonlistener(() -> {
                    presenter.setIsReubicar(cbReubicar.isChecked());
                    moveToOneToOthers();
                })
                .setNegativeButtonlistener(() -> {
                    presenter.setIsReubicar(false);
                });
        dialog.build().show();
    }

    private void setAdapters() {
        Calendar calendar = Calendar.getInstance();
        etFechaFinTareo.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));

        etHoraFinTareo.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        etFechaIniRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));

        etHoraIniRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        etFechaFinRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));

        etHoraFinRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        presenter.setFechaFinTareo(appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO));

        presenter.setHoraFinTareo(etHoraFinTareo.getText().toString());

        etFechaFinTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (presenter.validarFechaFinTareo(calendar.getTime(), etHoraIniRefrig.getText().toString())) {
                        etFechaFinTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                        presenter.setFechaFinTareo(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_TAREO));
                    }

                    etFechaFinTareo.clearFocus();
                }, appDateTime.getYear(), appDateTime.getMonth(), appDateTime.getDay());

                datePickerDialog.setOnCancelListener(dialog -> {
                    etFechaFinTareo.clearFocus();
                });

                datePickerDialog.setOnDismissListener(dialog -> {
                    etFechaFinTareo.clearFocus();
                });
                datePickerDialog.show();
            }
        });

        etHoraFinTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    if (presenter.validarFechaFinTareo(etFechaFinTareo.getText().toString(), calendar.getTime())) {
                        etHoraFinTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                        presenter.setHoraFinTareo(etHoraFinTareo.getText().toString());
                    }

                    etHoraFinTareo.clearFocus();
                }, appDateTime.getHour(), appDateTime.getMinute(), true);

                timePickerDialog.setOnCancelListener(dialog -> {
                    etHoraFinTareo.clearFocus();
                });
                timePickerDialog.setOnDismissListener(dialog -> {
                    etHoraFinTareo.clearFocus();
                });
                timePickerDialog.show();
            }
        });

        etFechaIniRefrig.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (presenter.validarFechaInicioRefrigerio(calendar.getTime(), etHoraIniRefrig.getText().toString())) {
                        etFechaIniRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                        presenter.setFechaIniRefrigerio(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_TAREO) + " " + etHoraIniRefrig.getText().toString());
                    }

                    etFechaIniRefrig.clearFocus();
                }, appDateTime.getYear(), appDateTime.getMonth(), appDateTime.getDay());

                datePickerDialog.setOnCancelListener(dialog -> {
                    etFechaIniRefrig.clearFocus();
                });

                datePickerDialog.setOnDismissListener(dialog -> {
                    etFechaIniRefrig.clearFocus();
                });
                datePickerDialog.show();
            }
        });

        etHoraIniRefrig.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    if (presenter.validarFechaInicioRefrigerio(etFechaIniRefrig.getText().toString(), calendar.getTime())) {
                        etHoraIniRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                        presenter.setFechaIniRefrigerio(DateUtil.changeStringDateTimeFormat(
                                etFechaIniRefrig.getText().toString(),
                                Constants.F_DATE_LECTURA,
                                Constants.F_DATE_TAREO) +
                                " " + etHoraIniRefrig.getText().toString());
                    }

                    etHoraIniRefrig.clearFocus();
                }, appDateTime.getHour(), appDateTime.getMinute(), true);

                timePickerDialog.setOnCancelListener(dialog -> {
                    etHoraIniRefrig.clearFocus();
                });

                timePickerDialog.setOnDismissListener(dialog -> {
                    etHoraIniRefrig.clearFocus();
                });
                timePickerDialog.show();
            }
        });

        etFechaFinRefrig.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (presenter.validarFechaFinRefrigerio(calendar.getTime(), etHoraFinRefrig.getText().toString())) {
                        etFechaFinRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                        presenter.setFechaFinRefrigerio(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_TAREO) + " " + etHoraIniRefrig.getText().toString());
                    }

                    etFechaFinRefrig.clearFocus();
                }, appDateTime.getYear(), appDateTime.getMonth(), appDateTime.getDay());

                datePickerDialog.setOnCancelListener(dialog -> {
                    etFechaFinRefrig.clearFocus();
                });

                datePickerDialog.setOnDismissListener(dialog -> {
                    etFechaFinRefrig.clearFocus();
                });

                datePickerDialog.show();
            }
        });

        etHoraFinRefrig.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    if (presenter.validarFechaFinRefrigerio(etFechaFinRefrig.getText().toString(), calendar.getTime())) {
                        etHoraFinRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                        presenter.setFechaFinRefrigerio(DateUtil.changeStringDateTimeFormat(etFechaFinRefrig.getText().toString(), Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO) + " " + etHoraFinRefrig.getText().toString());
                    }

                    etFechaFinRefrig.clearFocus();
                }, appDateTime.getHour(), appDateTime.getMinute(), true);

                timePickerDialog.setOnCancelListener(dialog -> {
                    etHoraFinRefrig.clearFocus();
                });
                timePickerDialog.setOnDismissListener(dialog -> {
                    etHoraFinRefrig.clearFocus();
                });
                timePickerDialog.show();
            }
        });

        mCbFinTareo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableFehaHoraFin(autoFin);
            } else {
                enableFehaHoraFin(!autoFin);
            }
        });

        cbHoraRefrigerio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showRefrig(true);
            } else {
                showRefrig(false);
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
        return R.layout.activity_finalizar_masivo;
    }

    @Override
    public void closeWindow() {
        this.finish();
    }

    @Override
    public void showRefrig(boolean show) {
        lblFechaRefrigIni.setVisibility(show ? View.VISIBLE : View.GONE);
        lblFechaFinRefrig.setVisibility(show ? View.VISIBLE : View.GONE);
        tvFechaIniRefrig.setVisibility(show ? View.VISIBLE : View.GONE);
        tvHoraIniRefrig.setVisibility(show ? View.VISIBLE : View.GONE);
        tvHoraFinRefrig.setVisibility(show ? View.VISIBLE : View.GONE);
        tvFechaFinRefrig.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showCheckAuto(boolean show) {
        mCbFinTareo.setChecked(show);
        mCbFinTareo.setVisibility(show ? View.VISIBLE : View.GONE);
        enableFehaHoraFin(show);
    }

    @Override
    public void enableFehaHoraFin(boolean enable) {
        textInputLayoutFechaFin.setEnabled(enable);
        textInputLayoutHoraFin.setEnabled(enable);
    }

    @Override
    public void moveToReubicarAllEmpleados(ArrayList<AllEmpleadoRow> listAllEmpleados) {
        startActivity(TareoReubicarMasivoListActivity.newInstance(
                FinalizarMasivoActivity.this, listAllEmpleados));
    }

    @Override
    public void moveToTareoReubicarMasivoPorDestajoActivity(List<AllEmpleadoRow> listaAllEmpleados,
                                                            List<TareoRow> listaAllTareos,
                                                            List<AllEmpleadoRow> listPorDestajo,
                                                            List<String> listaFechaTareos) {
        startActivity(TareoReubicarMasivoPorDestajoActivity.newInstance(
                FinalizarMasivoActivity.this, listaAllEmpleados, listaAllTareos,
                listPorDestajo, listaFechaTareos));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void moveToOneToOthers() {
        List<AllEmpleadoRow> listPorDetajo = new ArrayList<>();
        for (AllEmpleadoRow allEmpleadoRow : presenter.getAllEmpleados()) {
            Log.e(TAG, "for allEmpleadoRow: " + allEmpleadoRow);
            switch (allEmpleadoRow.getTipoTareo()) {
                case TypeTareo.TIPO_TAREO_DESTAJO:
                    AllEmpleadoRow AllEmpleadoRow = new AllEmpleadoRow();
                    AllEmpleadoRow.setCodigoTareo(allEmpleadoRow.getCodigoTareo());
                    AllEmpleadoRow.setCodigoDetalleTareo(allEmpleadoRow.getCodigoDetalleTareo());
                    AllEmpleadoRow.setCodigoEmpleado(allEmpleadoRow.getCodigoEmpleado());
                    AllEmpleadoRow.setEmpleado(allEmpleadoRow.getEmpleado());
                    listPorDetajo.add(AllEmpleadoRow);
                    break;
            }
        }
        if (listPorDetajo != null && listPorDetajo.size() > 0) {
            moveToTareoReubicarMasivoPorDestajoActivity(presenter.getAllEmpleados(),
                    listaAllTareos, listPorDetajo, listaFechasTareos);
        } else {
            presenter.finalizarTareo(cbHoraRefrigerio.isChecked());
        }
        finish();
    }
}
