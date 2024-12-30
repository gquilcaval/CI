package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_tareo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.FinalizarEmpleadoActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.FinalizarMasivoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;

public class TabTareoFragment extends BaseFragment
        implements ITabTareoContract.View,
        View.OnClickListener {

    @BindView(R.id.et_codNuevoEmpTareo)
    TextInputEditText etCodNuevoEmpTareo;
    @BindView(R.id.cb_fin_tareo)
    CheckBox mCbFinTareo;
    @BindView(R.id.tv_fecha_fin_tareo)
    TextInputLayout tvFechaFinTareo;
    @BindView(R.id.et_fecha_fin_tareo)
    TextInputEditText etFechaFinTareo;
    @BindView(R.id.tv_hora_fin_tareo)
    TextInputLayout tvHoraFinTareo;
    @BindView(R.id.et_hora_fin_tareo)
    TextInputEditText etHoraFinTareo;

    @BindView(R.id.cb_horaRefrigerio)
    CheckBox cbHoraRefrigerio;
    @BindView(R.id.lbl_fechaRefrigIni)
    TextView lblFechaRefrigIni;

    @BindView(R.id.tv_fechaIniRefrig)
    TextInputLayout tvFechaIniRefrig;
    @BindView(R.id.et_fechaIniRefrig)
    TextInputEditText etFechaIniRefrig;
    @BindView(R.id.tv_horaIniRefrig)
    TextInputLayout tvHoraIniRefrig;
    @BindView(R.id.et_horaIniRefrig)
    TextInputEditText etHoraIniRefrig;
    @BindView(R.id.lbl_fechaFinRefrig)
    TextView lblFechaFinRefrig;
    @BindView(R.id.tv_fechaFinRefrig)
    TextInputLayout tvFechaFinRefrig;
    @BindView(R.id.et_fechaFinRefrig)
    TextInputEditText etFechaFinRefrig;
    @BindView(R.id.tv_horaFinRefrig)
    TextInputLayout tvHoraFinRefrig;
    @BindView(R.id.et_horaFinRefrig)
    TextInputEditText etHoraFinRefrig;

    @BindView(R.id.fab_finalizar_tareo)
    FloatingActionButton fabFinalizarTareo;

    @Inject
    DateTimeManager appDateTime;
    @Inject
    TabTareoPresenter presenter;
    @Inject
    PreferenceManager preferences;

    private boolean autoFin;
    private TareoRow tareoRow;

    public TabTareoFragment(TareoRow tareoRow) {
        this.tareoRow = tareoRow;
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);
            presenter.finalizarEmpleado(tareoRow.getCodigo(), barcode, cbHoraRefrigerio.isChecked());
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.fragment_finalizar;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        fabFinalizarTareo.setOnClickListener(this);
    }

    @Override
    protected void setupView() {
        presenter.attachView(this);
        presenter.camposRequeridos(tareoRow.getFechaInicio());

        cbHoraRefrigerio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.setFechaIniRefrigerio(DateUtil.changeStringDateTimeFormat(
                        etFechaIniRefrig.getText().toString(), Constants.F_DATE_LECTURA,
                        Constants.F_DATE_TAREO) + " " + etHoraIniRefrig.getText().toString());
                presenter.setFechaFinRefrigerio(DateUtil.changeStringDateTimeFormat(
                        etFechaFinRefrig.getText().toString(), Constants.F_DATE_LECTURA,
                        Constants.F_DATE_TAREO) + " " + etHoraFinRefrig.getText().toString());
                showRefrig(true);
            } else {
                showRefrig(false);
            }
        });

        autoFin = preferences.getFechaHoraFinManual();
        showCheckAuto(autoFin);

        setAdapters();

        presenter.obtenerCodigoEmpleados(tareoRow.getCodigo());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SCANNER_ACTION);
        getContext().registerReceiver(this.mScanReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(this.mScanReceiver);

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
    public void cerrar() {
        getActivity().finish();
    }

    public void showCheckAuto(boolean show) {
        mCbFinTareo.setChecked(show);
        mCbFinTareo.setVisibility(show ? View.VISIBLE : View.GONE);
        enableFehaHoraFin(show);
    }

    public void enableFehaHoraFin(boolean enable) {
        etFechaFinTareo.setEnabled(enable);
        etHoraFinTareo.setEnabled(enable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_finalizar_tareo:
                mostrarMensajeFinalizarTareo();
                break;
        }
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

        etCodNuevoEmpTareo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.finalizarEmpleado(tareoRow.getCodigo(), etCodNuevoEmpTareo.getText().toString(), cbHoraRefrigerio.isChecked());
                etCodNuevoEmpTareo.setText("");
            }
            return true;
        });

        etFechaFinTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) -> {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, i, i1) -> {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) -> {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, i, i1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, i);
                    calendar.set(Calendar.MINUTE, i1);

                    if (presenter.validarFechaInicioRefrigerio(etFechaIniRefrig.getText().toString(), calendar.getTime())) {
                        etHoraIniRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                        presenter.setFechaIniRefrigerio(DateUtil.changeStringDateTimeFormat(etFechaIniRefrig.getText().toString(), Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO) +
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) -> {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (timePicker, i, i1) -> {
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

    }

    private void mostrarMensajeFinalizarTareo() {

        ArrayList<String> listaCodigosTareos = new ArrayList<>();
        listaCodigosTareos.add(tareoRow.getCodigo());
        ArrayList<TareoRow> listaAllTareos = new ArrayList<>();
        listaAllTareos.add(tareoRow);
        ArrayList<String> listaFechasTareos = new ArrayList<>();
        listaFechasTareos.add(tareoRow.getFechaInicio());


        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("¿Desea finalizar el tareo y todos los empleado?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setNegativeButtonLabel("Cancelar")
                .setPositiveButtonlistener(() -> {
                    startActivity(FinalizarMasivoActivity.newInstance(getContext(),
                            listaCodigosTareos,
                            listaAllTareos,
                            listaFechasTareos));
                    ((FinalizarEmpleadoActivity) getActivity()).salir();
                })
                .setNegativeButtonlistener(() -> {
                });
        dialog.build().show();
    }
}