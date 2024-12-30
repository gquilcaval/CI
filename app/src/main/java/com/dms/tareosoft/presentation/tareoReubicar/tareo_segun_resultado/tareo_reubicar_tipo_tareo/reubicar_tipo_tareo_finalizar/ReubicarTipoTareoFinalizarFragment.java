package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_finalizar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ReubicarTipoTareoFinalizarFragment extends BaseFragment
        implements IReubicarTipoTareoFinalizarContract.View {
    public static String LISTA_FECHA_TAREOS = "listaFechaTareos";

    private boolean FLAG_REFRIGERIO = false;
    private boolean FLAG_FINALIZAR = true;

    @Inject
    ReubicarTipoTareoFinalizarPresenter presenter;

    @Inject
    DateTimeManager appDateTime;

    @BindView(R.id.tv_fechaIniTareo)
    TextInputLayout textInputLayoutFechaFin;
    @BindView(R.id.tv_horaIniTareo)
    TextInputLayout textInputLayoutHoraFin;
    @BindView(R.id.cb_horaRefrigerio)
    CheckBox cbHoraRefrigerio;
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

    String fechaIniRefrigerio;
    String fechaFinRefrigerio;

    List<String> listaFechaTareos = new ArrayList<>();

    public ReubicarTipoTareoFinalizarFragment() {

    }

    public ReubicarTipoTareoFinalizarFragment newInstance(List<String> listaFechaTareos) {
        ReubicarTipoTareoFinalizarFragment fragment = new ReubicarTipoTareoFinalizarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LISTA_FECHA_TAREOS,(Serializable) listaFechaTareos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_reubicar_tipo_tareo_finalizar;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void setupView() {
        presenter.attachView(this);
        cbHoraRefrigerio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FLAG_REFRIGERIO = true;
                mostrarCampos();
            } else {
                FLAG_REFRIGERIO = false;
                ocultarCampos();
            }
        });

        if (getArguments() != null) {
            listaFechaTareos = (ArrayList<String>)
                    getArguments().getSerializable(LISTA_FECHA_TAREOS);

            presenter.setListaFechaTareos(listaFechaTareos);
        }


        setAdapters();
        presenter.validarCampos();
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void mostrarTodo() {
        textInputLayoutHoraFin.setEnabled(true);
        textInputLayoutFechaFin.setEnabled(true);
    }

    @Override
    public void ocultarTodo() {
        textInputLayoutFechaFin.setEnabled(false);
        textInputLayoutHoraFin.setEnabled(false);
    }

    @Override
    public boolean hasFinalizar() {
        return FLAG_FINALIZAR;
    }

    @Override
    public boolean hasRefrigerio() {
        return FLAG_REFRIGERIO;
    }

    @Override
    public String getFechaFinTareo() {
        return appDateTime.getFechaSincronizada(Constants.F_DATE_TAREO);
    }

    @Override
    public String getHoraFinTareo() {
        return etHoraFinTareo.getText().toString();
    }

    @Override
    public String getFechaIniRefrigerio() {
        return fechaIniRefrigerio;
    }

    @Override
    public String getFechaFinRefrigerio() {
        return fechaFinRefrigerio;
    }

    private void setAdapters() {
        Calendar calendar = Calendar.getInstance();
        etFechaFinTareo.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
        etHoraFinTareo.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        etFechaIniRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
        etHoraIniRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        etFechaFinRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_DATE_LECTURA));
        etHoraFinRefrig.setText(appDateTime.getFechaSincronizada(Constants.F_TIME_LECTURA));

        etFechaFinTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            if (presenter.validarFechaFinTareo(calendar.getTime(),
                                    etHoraIniRefrig.getText().toString())) {
                                etFechaFinTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                                getFechaFinTareo();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (timePicker, i, i1) -> {
                            calendar.set(Calendar.HOUR_OF_DAY, i);
                            calendar.set(Calendar.MINUTE, i1);

                            if (presenter.validarFechaFinTareo(etFechaFinTareo.getText().toString(), calendar.getTime())) {
                                etHoraFinTareo.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                                getHoraFinTareo();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            if (presenter.validarFechaInicioRefrigerio(calendar.getTime(), etHoraIniRefrig.getText().toString())) {
                                etFechaIniRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                                fechaIniRefrigerio = DateUtil.dateToStringFormat(calendar.getTime(),
                                        Constants.F_DATE_TAREO) + " " + etHoraIniRefrig.getText().toString();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (timePicker, i, i1) -> {
                            calendar.set(Calendar.HOUR_OF_DAY, i);
                            calendar.set(Calendar.MINUTE, i1);

                            if (presenter.validarFechaInicioRefrigerio(etFechaIniRefrig.getText().toString(), calendar.getTime())) {
                                etHoraIniRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                                fechaIniRefrigerio = DateUtil.changeStringDateTimeFormat(
                                        etFechaIniRefrig.getText().toString(), Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO) +
                                        " " + etHoraIniRefrig.getText().toString();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            if (presenter.validarFechaFinRefrigerio(calendar.getTime(),
                                    etHoraFinRefrig.getText().toString())) {
                                etFechaFinRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_LECTURA));
                                fechaFinRefrigerio = DateUtil.dateToStringFormat(calendar.getTime(),
                                        Constants.F_DATE_TAREO) + " " + etHoraIniRefrig.getText().toString();
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (timePicker, i, i1) -> {
                            calendar.set(Calendar.HOUR_OF_DAY, i);
                            calendar.set(Calendar.MINUTE, i1);

                            if (presenter.validarFechaFinRefrigerio(etFechaFinRefrig.getText().toString(),
                                    calendar.getTime())) {
                                etHoraFinRefrig.setText(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_TIME_LECTURA));
                                fechaFinRefrigerio = DateUtil.changeStringDateTimeFormat(
                                        etFechaFinRefrig.getText().toString(), Constants.F_DATE_LECTURA,
                                        Constants.F_DATE_TAREO) + " " + etHoraFinRefrig.getText().toString();
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

    }

    private void ocultarCampos() {
        lblFechaRefrigIni.setVisibility(View.INVISIBLE);
        lblFechaFinRefrig.setVisibility(View.INVISIBLE);
        tvFechaIniRefrig.setVisibility(View.INVISIBLE);
        tvHoraIniRefrig.setVisibility(View.INVISIBLE);
        tvHoraFinRefrig.setVisibility(View.INVISIBLE);
        tvFechaFinRefrig.setVisibility(View.INVISIBLE);
    }

    private void mostrarCampos() {
        lblFechaRefrigIni.setVisibility(View.VISIBLE);
        lblFechaFinRefrig.setVisibility(View.VISIBLE);
        tvFechaIniRefrig.setVisibility(View.VISIBLE);
        tvHoraIniRefrig.setVisibility(View.VISIBLE);
        tvHoraFinRefrig.setVisibility(View.VISIBLE);
        tvFechaFinRefrig.setVisibility(View.VISIBLE);
    }
}
