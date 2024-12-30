package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.opciones;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

public class OpcionesEditarFragment extends BaseFragment
        implements IOpcionesEditarContract.View {

    @BindView(R.id.et_fechaIniTareo)
    TextInputEditText etFechaIniTareo;
    @BindView(R.id.et_horaIniTareo)
    TextInputEditText etHoraInicioTareo;
    @BindView(R.id.tv_turno_tareo)
    TextView tvTurnoTareo;
    @BindView(R.id.tv_tipo_tareo)
    TextView tvTipoTareo;
    @BindView(R.id.tv_unidad_tareo)
    TextView tvUnidadTareo;
    @BindView(R.id.tv_lblunidad_tareo)
    TextView tvlblUnidadTareo;

    @Inject
    OpcionesEditarPresenter presenter;
    @Inject
    DateTimeManager appDateTime;
    private OpcionesTareo campos;
    private String codTareo;
    private String mHoraInicio;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    public OpcionesEditarFragment(String codigoTareo) {
        this.codTareo = codigoTareo;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_editar_opciones;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
        campos = new OpcionesTareo();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void setupView() {
        getActivityComponent().inject(this);
        setAdapters();
        presenter.attachView(this);
        presenter.obtenerCamposTareo(codTareo);
    }

    private void setAdapters() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        datePickerDialog = getDatePickerDialog(calendar);

        datePickerDialog.setOnCancelListener(dialog -> {
            etFechaIniTareo.clearFocus();
        });

        datePickerDialog.setOnDismissListener((dialog) -> {
            etFechaIniTareo.clearFocus();
        });

        etFechaIniTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mostrarMensajeCambioFecha();
            }
        });

        timePickerDialog = getTimePickerDialog(calendar);

        timePickerDialog.setOnCancelListener(dialog -> {
            etHoraInicioTareo.clearFocus();
        });

        timePickerDialog.setOnDismissListener((dialog) -> {
            etHoraInicioTareo.clearFocus();
        });

        etHoraInicioTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mostrarMensajeCambioHora();
            }
        });
    }

    @Override
    public OpcionesTareo obtenerCampos() {
        return campos;
    }

    @Override
    public void mostrarUnidadMedida(String descripcion) {
        tvUnidadTareo.setVisibility(View.VISIBLE);
        tvlblUnidadTareo.setVisibility(View.VISIBLE);
        tvUnidadTareo.setText(descripcion);
    }

    @Override
    public void mostrarTurno(String descripcion) {
        tvTurnoTareo.setText(descripcion);
    }

    @Override
    public void mostrarCampos(String tipo, String fechaInicio, String horaInicio) {
        mHoraInicio = horaInicio;
        tvTipoTareo.setText(tipo);
        etHoraInicioTareo.setText(mHoraInicio);
        etFechaIniTareo.setText(fechaInicio);

        campos.setFechaInicio(DateUtil.changeStringDateTimeFormat(fechaInicio,
                Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO));
        campos.setHoraInicio(mHoraInicio);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private TimePickerDialog getTimePickerDialog(Calendar calendar) {
        return new TimePickerDialog(getContext(), (timePicker, hour, minut) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minut);

            String time = DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA);
            campos.setHoraInicio(time);
            etHoraInicioTareo.setText(time);
            etHoraInicioTareo.clearFocus();

        }, appDateTime.getHour(), appDateTime.getMinute(), true);
    }

    private DatePickerDialog getDatePickerDialog(Calendar calendar) {
        return new DatePickerDialog(getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (presenter.validarFechaInicio(calendar.getTime())) {
                etFechaIniTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                campos.setFechaInicio(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
            } else {
                etFechaIniTareo.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
                campos.setFechaInicio(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
            }

            etFechaIniTareo.clearFocus();
        }, appDateTime.getYear(), appDateTime.getMonth(), appDateTime.getDay());
    }

    private void mostrarMensajeCambioHora() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Se modificará la hora de inicio para los trabajadores asignados al tareo inicialmente")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setNegativeButtonLabel("Cancelar")
                .setPositiveButtonlistener(() -> {
                    timePickerDialog.show();
                })
                .setNegativeButtonlistener(() -> {
                });
        dialog.build().show();
    }

    private void mostrarMensajeCambioFecha() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Se modificará la fecha de inicio para los trabajadores asignados al tareo inicialmente")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setNegativeButtonLabel("Cancelar")
                .setPositiveButtonlistener(() -> {
                    datePickerDialog.show();
                })
                .setNegativeButtonlistener(() -> {
                });
        dialog.build().show();
    }
}
