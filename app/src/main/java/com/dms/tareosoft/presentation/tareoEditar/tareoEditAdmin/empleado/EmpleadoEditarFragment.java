package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.empleado;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.EditarTareoActivity;
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

public class EmpleadoEditarFragment extends BaseFragment
        implements IEmpleadoEditarContract.View {

    String TAG = EmpleadoEditarFragment.class.getSimpleName();

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
    EmpleadoEditarPresenter presenter;
    @Inject
    DateTimeManager appDateTime;
    @Inject
    PreferenceManager preferenceManager;

    private String mFechaInicio, mHoraInicio;
    private ContenidoAjustes contenidoAjustes;
    private final String codigoTareo;
    private String nomina = null;
    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;
    private MediaPlayer mediaPlayer;

    public EmpleadoEditarFragment(String codTareo) {
        codigoTareo = codTareo;
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);
            if (!TextUtils.isEmpty(barcode)) {
                saveFechaHora();
                presenter.evaluarEmpleado(barcode,
                        nomina,
                        mFechaInicio,
                        mHoraInicio);
            }
        }
    };

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
        return R.layout.fragment_editar_empleado;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        cbIniTareo.setChecked(false);
        tvFechaIniEmpTareo.setVisibility(View.VISIBLE);
        tvHoraIniEmpTareo.setVisibility(View.VISIBLE);
        cbIniTareoManual.setChecked(true);
    }

    @Override
    protected void setupView() {
        contenidoAjustes = preferenceManager.getContenidoAjustes();
        rvEmpleados.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);
        rvEmpleados.setAdapter(adapterEmpleados);

        etCodigoNuevoEmpleado.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(etCodigoNuevoEmpleado.getText().toString())) {
                    saveFechaHora();
                    presenter.evaluarEmpleado(etCodigoNuevoEmpleado.getText().toString(),
                            nomina,
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
        setAdapters();
        presenter.cargarDetalleTareo(codigoTareo, StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO);
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

    public void setNomina(String nomina) {
        if (!TextUtils.isEmpty(nomina))
            this.nomina = nomina;
    }

    private void setTotal(int size) {
        String resumen = getString(R.string.total_emp) + " " + size;
        tvTotalEmpTareo.setText(resumen);
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
        etHoraIniEmpTareo.setText(presenter.getCurrentTime());
    }

    @Override
    public void reiniciarFechaInicio() {
        etFechaIniEmpTareo.setText(DateUtil.setDateFormat(presenter.getCurrentDate(),
                Constants.F_DATE_TAREO, Constants.F_DATE_LECTURA));
    }


    @Override
    public List<DetalleTareo> getEmpleados() {
        return presenter.getListDetalleTareo();
    }

    @Override
    public void setFechaInicio(String fechaInicio) {
        Log.e(TAG, "setFechaInicio: " + fechaInicio);
        mFechaInicio = fechaInicio;
        presenter.setFechaInicio(mFechaInicio);
    }

    @Override
    public void setHoraInicio(String horaInicio) {
        Log.e(TAG, "setHoraInicio: " + horaInicio);
        mHoraInicio = horaInicio;
        presenter.setHoraInicio(mHoraInicio);
    }


    @Override
    public void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton) {
        UtilsMethods.showConfirmDialog(getActivity(), titulo, mensaje, iPositiveButton).show();
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

    private void validChecker(CheckBox checkBox, boolean isChecked) {
        checkBox.setChecked(!isChecked);
    }

    private void saveFechaHora() {
        if (cbIniTareo.isChecked()) {
            mFechaInicio = ((EditarTareoActivity) getActivity()).getFechaInicio();
            mHoraInicio = ((EditarTareoActivity) getActivity()).getHoraInicio();
        }
        if (cbIniTareoManual.isChecked()) {
            mFechaInicio = DateUtil.setDateFormat(etFechaIniEmpTareo.getText().toString(),
                    Constants.F_DATE_LECTURA, Constants.F_DATE_TAREO);
            mHoraInicio = etHoraIniEmpTareo.getText().toString();
        }
    }
}
