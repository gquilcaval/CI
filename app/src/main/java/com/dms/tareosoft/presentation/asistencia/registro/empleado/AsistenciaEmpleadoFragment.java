package com.dms.tareosoft.presentation.asistencia.registro.empleado;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.asistencia.registro.RegistroAsistenciaActivity;
import com.dms.tareosoft.presentation.asistencia.registro.definicion.AsistenciaDefinicionFragment;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AsistenciaEmpleadoFragment extends BaseFragment
        implements IAsistenciaEmpleadoContract.View,
        View.OnClickListener {

    static String TAG = AsistenciaEmpleadoFragment.class.getSimpleName();

    @BindView(R.id.rb_entrada)
    RadioButton cbIniTareo;
    @BindView(R.id.rb_salida)
    RadioButton cbIniTareoManual;
    @BindView(R.id.rb_group)
    RadioGroup rbGroup;
    @BindView(R.id.et_codNuevoEmpTareo)
    TextInputEditText etCodigoNuevoEmpleado;
    @BindView(R.id.rv_empleadosTareo)
    RecyclerView rvEmpleados;
    @BindView(R.id.tv_total_registros)
    TextView tvTotalEmpTareo;

    @Inject
    AsistenciaEmpleadoPresenter presenter;
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    DateTimeManager appDateTime;

    private MediaPlayer mediaPlayer;

    private AsistenciaDefinicionFragment tabDefinicion;

    private List<EmpleadoRow> listaEmpleados;
    private EmpleadoSimpleAdapter adapterEmpleados;
    private String mFechaInicio, mHoraInicio;
    private ContenidoAjustes contenidoAjustes;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);

            if (TextUtils.isEmpty(barcode)) {
                barcode = intent.getStringExtra(Constants.SCANNER_STRING_HONEY);
            }

            if (!TextUtils.isEmpty(barcode)) {
                int indexRb = rbGroup.indexOfChild(getView().findViewById(rbGroup.getCheckedRadioButtonId()));
                presenter.validarUltimaMarcacion(barcode,
                        mFechaInicio,
                        mHoraInicio, tabDefinicion.getTareo(), String.valueOf(indexRb));
            }
        }
    };

    public static AsistenciaEmpleadoFragment newInstance(List<AllEmpleadoRow> mDataListEmpleados) {
        AsistenciaEmpleadoFragment fragment = new AsistenciaEmpleadoFragment();
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
        return R.layout.fragment_registro_asistencia;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        cbIniTareo.setChecked(true);
        cbIniTareoManual.setChecked(false);
    }

    @Override
    protected void setupView() {


        tabDefinicion = new AsistenciaDefinicionFragment();
        rvEmpleados.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaEmpleados = new ArrayList<>();
        adapterEmpleados = new EmpleadoSimpleAdapter(listaEmpleados);
        rvEmpleados.setAdapter(adapterEmpleados);
        presenter.obtenerAsistencias();
        contenidoAjustes = preferenceManager.getContenidoAjustes();

        //presenter.obtenerAsistencias();
        etCodigoNuevoEmpleado.setOnEditorActionListener((v, actionId, event) -> {
            if ( actionId == EditorInfo.IME_ACTION_NEXT || event != null &&
                    (event.getKeyCode() == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN)) {

                if (!TextUtils.isEmpty(etCodigoNuevoEmpleado.getText().toString())) {
                    int indexRb = rbGroup.indexOfChild(getView().findViewById(rbGroup.getCheckedRadioButtonId()));
                    String tipoEntrada = "";
                    if ( indexRb == 0 ) {
                        tipoEntrada = "E";
                    } else if (indexRb == 1) {
                        tipoEntrada = "S";
                    }
                    presenter.validarUltimaMarcacion(etCodigoNuevoEmpleado.getText().toString(),
                            mFechaInicio,
                            mHoraInicio, RegistroAsistenciaActivity.m, tipoEntrada);
                    etCodigoNuevoEmpleado.setText("");
                } else {
                    showErrorMessage("El codigo y/o dni del empleado no puede estar vacio", "");
                }
            }
            return true;
        });

        presenter.attachView(this);
        presenter.validarVista();
        setAdapters();
    }

    @Override
    public void mostrarEmpleados(List<EmpleadoRow> lista) {
        adapterEmpleados.update(lista);
        //setTotal(listaEmpleados.size());
    }

    @Override
    public void agregarEmpleado(EmpleadoRow row) {
        Log.d(TAG, "agregar empleado -> " + row);
        this.listaEmpleados.add(row);
        Log.d(TAG, "agregar empleado size -> " + listaEmpleados.size());
        adapterEmpleados.notifyDataSetChanged();
       // setTotal(listaEmpleados.size());
    }

    @Override
    public void quitarEmpleado(int position) {
        this.listaEmpleados.remove(position);
        adapterEmpleados.notifyDataSetChanged();
      //  setTotal(listaEmpleados.size());
    }

    @Override
    public void reiniciarHoraInicio() {

    }

    @Override
    public void reiniciarFechaInicio() {

    }

    @Override
    public void mostrarTodo() {
    }

    @Override
    public void ocultarTodo() {
    }

    @Override
    public void mostrarAsistencias(List<Marcacion> lista) {

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

        reiniciarHoraInicio();
        reiniciarFechaInicio();

        cbIniTareo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reiniciarHoraInicio();
            reiniciarFechaInicio();
            if (isChecked) {

            }
        });

        cbIniTareoManual.setOnCheckedChangeListener((buttonView, isChecked) -> {
            reiniciarHoraInicio();
            reiniciarFechaInicio();

        });
    }

    @Override
    public void setTotal(int size) {
        String resumen = getString(R.string.total_emp) + " " + size;
        tvTotalEmpTareo.setText(resumen);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void mostrarMensajeRegistroConfirmacion(Empleado empleado, String fecha, String hora, Marcacion marcacion, String tipoEntrada) {
        UtilsMethods.playSound(getActivity(), R.raw.registro_incorrecto);
        CustomDialog.Builder dialog = new CustomDialog.Builder(requireContext())
                .setTitle("Atención")
                .setSubMessage(empleado.getMensaje())
                .setMessage("¿Desea registrar el personal?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Si")
                .setNegativeButtonLabel("No")
                .setPositiveButtonlistener(() -> {

                    if (!empleado.getApellidoPaterno().isEmpty()) {
                        presenter.registrarEmpleadoTareo(empleado, fecha, hora, marcacion, tipoEntrada);
                    } else  {
                        presenter.registrarEmpleadoTareoPorDni(empleado.getCodigoEmpleado(), fecha, hora, marcacion, tipoEntrada);
                    }
                })
                .setNegativeButtonlistener(() -> {

                });
        dialog.build().show();
    }
}
