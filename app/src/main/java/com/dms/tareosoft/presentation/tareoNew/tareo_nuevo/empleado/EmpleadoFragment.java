package com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.NuevoTareoActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class EmpleadoFragment extends BaseFragment
        implements IEmpleadoContract.View,
        View.OnClickListener {

    static String TAG = EmpleadoFragment.class.getSimpleName();

    @BindView(R.id.rb_entrada)
    RadioButton cbIniTareo;
    @BindView(R.id.rb_salida)
    RadioButton cbIniTareoManual;
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
    EmpleadoPresenter presenter;
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    DateTimeManager appDateTime;

    private MediaPlayer mediaPlayer;

    private String nomina = null;
    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;
    private String mFechaInicio, mHoraInicio;
    private ContenidoAjustes contenidoAjustes;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);
            Log.d("Aqui", "barcode -> " + barcode);
            if (TextUtils.isEmpty(barcode)) {
                barcode = intent.getStringExtra(Constants.SCANNER_STRING_HONEY);
            }

            if (!TextUtils.isEmpty(barcode)) {
                saveFechaHora();
                presenter.evaluarEmpleado(barcode, nomina,
                        mFechaInicio,
                        mHoraInicio);
            }
        }
    };

    public static EmpleadoFragment newInstance(List<AllEmpleadoRow> mDataListEmpleados) {
        EmpleadoFragment fragment = new EmpleadoFragment();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        /*args.putSerializable(Constants.Intent.EXTRA_SOLICITUD_PERMISO, solicitudPermiso);
        args.putInt(Constants.Intent.EXTRA_OPTION_TYPE_VIEW, optionType);*/
        fragment.setArguments(args);
        Log.e(TAG, "newInstance mDataListEmpleados: " + mDataListEmpleados);
        return fragment;
    }

    @Override
    public void playSound() {
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.beep);
        mediaPlayer.setVolume(0.09f, 0.09f);
        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        });
        mediaPlayer.start();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_empleado;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        cbIniTareo.setChecked(true);
        cbIniTareoManual.setChecked(false);
        etFechaIniEmpTareo.setOnClickListener(this);
        etHoraIniEmpTareo.setOnClickListener(this);
    }

    @Override
    protected void setupView() {
        rvEmpleados.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);
        rvEmpleados.setAdapter(adapterEmpleados);
        contenidoAjustes = preferenceManager.getContenidoAjustes();

        etCodigoNuevoEmpleado.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || event != null &&
                    (event.getKeyCode() == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN)) {
                if (!TextUtils.isEmpty(etCodigoNuevoEmpleado.getText().toString())) {
                    saveFechaHora();
                    Log.d("AQUI", "EMP -> " + etCodigoNuevoEmpleado.getText().toString());
                   presenter.evaluarEmpleado(etCodigoNuevoEmpleado.getText().toString(), nomina,
                            mFechaInicio,
                            mHoraInicio);
                   etCodigoNuevoEmpleado.setText("");
                } else {
                    showErrorMessage("El codigo y/o dni del empleado no puede estar vacio", "");
                }
            }
            return true;
        });

        presenter.attachView(this);
        presenter.validarVista();
        tvFechaIniEmpTareo.setVisibility(View.GONE);
        tvHoraIniEmpTareo.setVisibility(View.GONE);
        setAdapters();
    }

    @Override
    public void mostrarEmpleados(List<EmpleadoRow> lista) {
        listaEmpleados.clear();
        listaEmpleados.addAll(lista);
        adapterEmpleados.notifyDataSetChanged();
        setTotal(listaEmpleados.size());
    }

    @Override
    public void agregarEmpleado(EmpleadoRow row) {
        this.listaEmpleados.add(row);
        adapterEmpleados.notifyDataSetChanged();
        setTotal(listaEmpleados.size());
    }

    @Override
    public void quitarEmpleado(int position) {
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
    public void mostrarTodo() {
        tvFechaIniEmpTareo.setVisibility(View.VISIBLE);
        tvHoraIniEmpTareo.setVisibility(View.VISIBLE);
    }

    @Override
    public void ocultarTodo() {
        tvFechaIniEmpTareo.setVisibility(View.GONE);
        tvHoraIniEmpTareo.setVisibility(View.GONE);
    }

    @Override
    public void setFechaInicio(String fechaInicio) {
        Log.e(TAG, "fechaInicio : " + fechaInicio);
        this.mFechaInicio = fechaInicio;
        presenter.setFechaInicio(this.mFechaInicio);
    }

    @Override
    public void setHoraInicio(String horaInicio) {
        Log.e(TAG, "horaInicio : " + horaInicio);
        this.mHoraInicio = horaInicio;
        presenter.setHoraInicio(this.mHoraInicio);
    }

    @Override
    public List<DetalleTareo> getEmpleados() {
        return presenter.obtenerEmpleados();
    }

    @Override
    public void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton) {
        UtilsMethods.showConfirmDialog(getActivity(), titulo, mensaje, iPositiveButton).show();
    }

    @Override
    public boolean hasTareo() {
        if (presenter.isRegisterNotEmployer())
            return true;
        else
            return listaEmpleados.size() > 0;
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void setAdapters() {
        etFechaIniEmpTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                presenter.showDatePickerDialog(getFragmentManager(), etFechaIniEmpTareo, null);
            }
        });

        etHoraIniEmpTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                presenter.showTimePickerDialog(this, etHoraIniEmpTareo);
            }
        });
        reiniciarHoraInicio();
        reiniciarFechaInicio();

        cbIniTareo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reiniciarHoraInicio();
            reiniciarFechaInicio();
            if (isChecked) {
                tvFechaIniEmpTareo.setVisibility(View.GONE);
                tvHoraIniEmpTareo.setVisibility(View.GONE);
                tvTipoIngreso.setText(getString(R.string.automatico));
            }
        });

        cbIniTareoManual.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reiniciarHoraInicio();
            reiniciarFechaInicio();
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

    public void setNomina(String nomina) {
        this.nomina = nomina;
    }

    private void setTotal(int size) {
        String resumen = getString(R.string.total_emp) + " " + size;
        tvTotalEmpTareo.setText(resumen);
    }

    private void saveFechaHora() {
        if (cbIniTareo.isChecked()) {
            mFechaInicio = ((NuevoTareoActivity) getActivity()).getFechaInicio();
            mHoraInicio = ((NuevoTareoActivity) getActivity()).getHoraInicio();
        }
        if (cbIniTareoManual.isChecked()) {
            mFechaInicio = DateUtil.changeStringDateTimeFormat(etFechaIniEmpTareo.getText().toString(),
                    Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO);
            mHoraInicio = etHoraIniEmpTareo.getText().toString();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_fechaIniEmpTareo:
                presenter.showDatePickerDialog(getFragmentManager(), etFechaIniEmpTareo, null);
                break;
            case R.id.et_horaIniEmpTareo:
                presenter.showTimePickerDialog(this, etHoraIniEmpTareo);
                break;
        }
    }

    @Override
    public void mostrarMensajeRegistroConfirmacion(Empleado empleado, String fecha, String hora) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(requireContext())
                .setTitle("Atención")
                .setMessage("¿Desea registrar el personal?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Si")
                .setNegativeButtonLabel("No")
                .setPositiveButtonlistener(() -> {

                    if (!empleado.getApellidoPaterno().isEmpty()) {
                        presenter.registrarEmpleadoTareo(empleado, fecha, hora);
                    } else {
                        presenter.registrarEmpleadoTareoPorDni(empleado.getCodigoEmpleado(), fecha, hora);
                    }
                })
                .setNegativeButtonlistener(() -> {

                });
        dialog.build().show();
    }
}
