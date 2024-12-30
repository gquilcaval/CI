package com.dms.tareosoft.presentation.fragments.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dms.tareosoft.BuildConfig;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.data.models.ContenidoGeneral;
import com.dms.tareosoft.data.models.ContenidoTareo;
import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.presentation.adapter.SpinUnidadMedidaAdapter;
import com.dms.tareosoft.presentation.adapter.ViewPagerAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.SpinnerListener;
import com.dms.tareosoft.util.TextWatcher;
import com.dms.tareosoft.util.TextUtil;
import com.dms.tareosoft.workmanager.SendDataWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.dms.tareosoft.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SettingActivity extends BaseActivity implements ISettingContract.View {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private boolean FLAG_URL = false;
    private boolean FLAG_TAB_GENERAL = false;
    private boolean FLAG_TAB_TAREO = false;
    private boolean FLAG_TAB_AJUSTES = false;

    private ViewPager viewPager;
    private BottomNavigationView navigation;

    private TextInputEditText txtServicioWeb;
    private TextInputEditText txtMargenTiempo;
    private TextInputEditText txtTimeout;
    private TextInputEditText txtDuracionRefrigerio;
    private TextInputEditText tvFechaServidor;
    private TextView tvUltimaValidacion;
    private RadioButton rbBatch;
    private RadioButton rbLinea;
    private Switch swErroresDetallados;

    private Spinner spUnidadMedida;
    private Spinner spTurno;
    private Spinner spClaseTareo;

    private Switch swAjustesUnidadMedida;
    private Switch swAjustesFechaHoraIni;
    private Switch swAjustesFechaHoraFin;
    private Switch swAjustesVigencia;
    private Switch swAjustesRegistrarEmpleado;
    private Switch swAjustesRegistrarTareoNotEmpleado;
    private Switch swSound;
    private Switch swValidDescarga;
    private Switch swModuloAsistencia;
    private Switch swModuloIncidencia;
    private Switch swModuloAcceso;
    private BubbleSeekBar sliderTimeWorker;

    MaterialButton mbDescargar;
    MaterialButton mbCambiarFechaHora;

    private List<String> listaTurnos;
    private List<String> listaClaseTareo;
    private SpinUnidadMedidaAdapter adapterUnidadMedida;

    @Inject
    PreferenceManager preferenceManager;
    private PeriodicWorkRequest mPeriodicWorkRequestSync;

    @Inject
    SettingPresenter presenter;

    //TODO getResources().getString(R.string.mystring)
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        listaTurnos = new ArrayList<>();
        listaClaseTareo = new ArrayList<>();
        initView();
        presenter.attachView(this);
        setAdapters();
        presenter.cargarVista();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    private void initWorker() {
        WorkManager.getInstance(this).getWorkInfosByTag("DownloadDataWorker").cancel(true);
        Log.d(TAG, "Workmanager time  download  -> " + preferenceManager.getTimeWorker() + " min");
        mPeriodicWorkRequestSync = new PeriodicWorkRequest.Builder(SendDataWorker.class,
                preferenceManager.getTimeWorker(), TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(SendDataWorker.TAG,
                        ExistingPeriodicWorkPolicy.REPLACE, mPeriodicWorkRequestSync);
    }
    @Override
    public void reiniciar() {
        Log.d(TAG, "Workmanager time  download  -> " + preferenceManager.getTimeWorker() + " min");
        clearDaggerComponent();
        closed();
        startActivity(SettingActivity.newInstance(this));
    }

    @Override
    public void closed() {
        initWorker();
        clearDaggerComponent();
        setResult(RESULT_OK);
        finish();

    }

    @Override
    public void mostrarFechaHora(String fechaCarga, String fechaHoraServidor) {
        tvUltimaValidacion.setText(fechaCarga);
        tvFechaServidor.setText(fechaHoraServidor);
    }

    @Override
    public void mostrarCampos(ContenidoGeneral general, ContenidoTareo tareo, ContenidoAjustes ajustes) {
        txtServicioWeb.setText(general.getUrlServicioWeb());
        txtMargenTiempo.setText(String.valueOf(general.getMargenDiferencia()));
        tvFechaServidor.setText(general.getFechaHora());
        tvUltimaValidacion.setText(general.getUltimaFechaHora());
        txtTimeout.setText(String.valueOf(general.getTimeOut()));
        txtDuracionRefrigerio.setText(String.valueOf(general.getDuracionRefrigerio()));
        swErroresDetallados.setChecked(general.isErroresDetallados());
        swSound.setChecked(general.isSound());
        swValidDescarga.setChecked(general.isValidaDescarga());
        swModuloAsistencia.setChecked(general.isActiveModuloAsistencia());
        swModuloIncidencia.setChecked(general.isActiveModuloIncidencia());
        swModuloAcceso.setChecked(general.isActiveModuloAccceso());


        switch (general.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                rbBatch.setChecked(false);
                rbLinea.setChecked(true);
                break;
            case ModoTrabajo.BATCH:
                rbLinea.setChecked(false);
                rbBatch.setChecked(true);
                break;
        }

        swAjustesUnidadMedida.setChecked(ajustes.isUnidadMedida());
        swAjustesFechaHoraIni.setChecked(ajustes.isFechaHoraInicio());
        swAjustesFechaHoraFin.setChecked(ajustes.isFechaHoraFin());
        swAjustesVigencia.setChecked(ajustes.isVigenciaDescarga());
        swAjustesRegistrarEmpleado.setChecked(ajustes.isRegistrarEmpleado());
        swAjustesRegistrarTareoNotEmpleado.setChecked(ajustes.isRegistrarTareoNotEmpleado());
        sliderTimeWorker.setProgress(ajustes.getTimeWorker());

        if (tareo != null) {
            spUnidadMedida.setSelection(tareo.getUnidadMedida());
            spTurno.setSelection(tareo.getTurno());
            spClaseTareo.setSelection(tareo.getClaseTareo());
        }
    }

    @Override
    public void setTextButton(String message) {
        mbDescargar.setText(message);
    }

    @Override
    public void fechaDesfasada() {
        tvFechaServidor.setTextColor(Color.RED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
            case android.R.id.home:
                verificarCambios();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void verificarCambios() {
        if (FLAG_URL || FLAG_TAB_GENERAL || FLAG_TAB_AJUSTES || FLAG_TAB_TAREO) {
            mostrarMensajeConfirmacion();
        } else {
            closed();
        }
    }

    @Override
    public void onBackPressed() {
        verificarCambios();
    }

    @Override
    public void limpiarFlags() {
        FLAG_URL = false;
        FLAG_TAB_GENERAL = false;
        FLAG_TAB_AJUSTES = false;
        FLAG_TAB_TAREO = false;
    }

    @Override
    public void listarUnidadMedida(List<UnidadMedida> resultado, int seleccion) {
        adapterUnidadMedida = new SpinUnidadMedidaAdapter(this,
                R.layout.spinner_dropdown, resultado);
        spUnidadMedida.setAdapter(adapterUnidadMedida);

        adapterUnidadMedida.notifyDataSetChanged();

        spUnidadMedida.setOnItemSelectedListener(null);//Para no activar el listener al dar setSelection en la siguiente linea(es una acción no causada por el usuario)
        spUnidadMedida.setSelection(seleccion, false);
        spUnidadMedida.setOnItemSelectedListener((SpinnerListener) pos -> {
            FLAG_TAB_TAREO = true;
            presenter.setCodigoUnidadMedida(pos);
        });
    }

    @Override
    public void listarTurno(List<String> turnos, int posicion) {
        listaTurnos.clear();
        listaTurnos.addAll(turnos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown, listaTurnos);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spTurno.setAdapter(adapter);
        spTurno.setOnItemSelectedListener(null);//Para no activar el listener al dar setSelection (es una acción no causada por el usuario)
        spTurno.setSelection(posicion, false);
        spTurno.setOnItemSelectedListener((SpinnerListener) pos -> {
            FLAG_TAB_TAREO = true;
            presenter.setCodigoTurno(pos);
        });
    }

    @Override
    public void listarClaseTarea(List<String> clasesTareos, int posicion) {
        listaClaseTareo.clear();
        listaClaseTareo.addAll(clasesTareos);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown, listaClaseTareo);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spClaseTareo.setAdapter(adapter);
        spClaseTareo.setOnItemSelectedListener(null);//Para no activar el listener al dar setSelection (es una acción no causada por el usuario)s
        spClaseTareo.setSelection(posicion, false);
        spClaseTareo.setOnItemSelectedListener((SpinnerListener) pos -> {
            FLAG_TAB_TAREO = true;
            presenter.setClaseTareo(pos);
        });
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.general);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.tareo);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.ajustes);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.general:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.tareo:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.ajustes:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);

        View tabGenerals = getLayoutInflater().inflate(R.layout.fragment_setting_general, null);
        View tabTareo = getLayoutInflater().inflate(R.layout.fragment_setting_tareo, null);
        View tabAjustess = getLayoutInflater().inflate(R.layout.fragment_setting_ajustes, null);

        viewPager = findViewById(R.id.view_pager_bottom_navigation);
        navigation = findViewById(R.id.bottom_navigation);

        List<View> viewList = new ArrayList<>();
        viewList.add(tabGenerals);
        viewList.add(tabTareo);
        viewList.add(tabAjustess);

        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /* General */
        mbDescargar = tabGenerals.findViewById(R.id.mb_descargar);
        mbCambiarFechaHora = tabGenerals.findViewById(R.id.mb_fechahora);
        txtServicioWeb = tabGenerals.findViewById(R.id.tv_servicioweb);
        txtMargenTiempo = tabGenerals.findViewById(R.id.txt_diferencia);
        tvFechaServidor = tabGenerals.findViewById(R.id.tv_fechaServidor);
        tvUltimaValidacion = tabGenerals.findViewById(R.id.tv_ultima_validacion);
        txtTimeout = tabGenerals.findViewById(R.id.txt_timeout);
        txtDuracionRefrigerio = tabGenerals.findViewById(R.id.txt_duracion_refrigerio);
        swErroresDetallados = tabGenerals.findViewById(R.id.sw_errores_detallados);
        swValidDescarga = tabGenerals.findViewById(R.id.sw_valida_descarga);
        swModuloAsistencia = tabGenerals.findViewById(R.id.sw_mostrar_asistencia);
        swModuloIncidencia = tabGenerals.findViewById(R.id.sw_mostrar_incidencia);
        swModuloAcceso = tabGenerals.findViewById(R.id.sw_mostrar_acceso);
        swSound = tabGenerals.findViewById(R.id.sw_sound);
        rbBatch = tabGenerals.findViewById(R.id.rb_batch);
        rbLinea = tabGenerals.findViewById(R.id.rb_linea);



        /* Tareo */
        spUnidadMedida = tabTareo.findViewById(R.id.sp_unidadMedida);
        spTurno = tabTareo.findViewById(R.id.sp_turno);
        spClaseTareo = tabTareo.findViewById(R.id.tv_clase_tareo);

        /* Ajustes */
        swAjustesUnidadMedida = tabAjustess.findViewById(R.id.sw_ajustes_um);
        swAjustesFechaHoraIni = tabAjustess.findViewById(R.id.sw_ajustes_fecha_hora_ini);
        swAjustesFechaHoraFin = tabAjustess.findViewById(R.id.sw_ajustes_fecha_hora_fin);
        swAjustesVigencia = tabAjustess.findViewById(R.id.sw_ajustes_vigencia);
        swAjustesRegistrarEmpleado = tabAjustess.findViewById(R.id.sw_ajustes_registrar_empleado);
        swAjustesRegistrarTareoNotEmpleado = tabAjustess.findViewById(R.id.sw_ajustes_registrar_tareo_sin_empleado);
        sliderTimeWorker = tabAjustess.findViewById(R.id.sliderTimeWorker);
    }

    private void setAdapters() {
        mbDescargar.setOnClickListener(v -> {
            presenter.descargarMaestros(FLAG_TAB_GENERAL, FLAG_TAB_AJUSTES, FLAG_TAB_TAREO);
        });
        mbCambiarFechaHora.setOnClickListener(view -> {
            abrirVentanaConfigurarFechaHora();
        });
        txtServicioWeb.addTextChangedListener((TextWatcher) value -> {
            FLAG_URL = presenter.guardarWebService(value);
        });
        txtMargenTiempo.addTextChangedListener((TextWatcher) s -> {
            FLAG_TAB_GENERAL = true;
            presenter.setMargenDiferencia(TextUtil.convertToInt(s));
        });
        txtTimeout.addTextChangedListener((TextWatcher) s -> {
            FLAG_TAB_GENERAL = true;
            presenter.setTimeOut(TextUtil.convertToInt(s));
        });
        txtDuracionRefrigerio.addTextChangedListener((TextWatcher) s -> {
            FLAG_TAB_GENERAL = true;
            presenter.setDuracionRefrigerio(TextUtil.convertToInt(s));
        });
        swAjustesUnidadMedida.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setAjustesUnidadMedida(swAjustesUnidadMedida.isChecked());
        });
        swAjustesFechaHoraIni.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setAjustesFechaHoraInicio(swAjustesFechaHoraIni.isChecked());
        });
        swAjustesFechaHoraFin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setAjustesFechaHoraFin(swAjustesFechaHoraFin.isChecked());
        });
        swAjustesVigencia.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setAjustesVigenciaDescarga(swAjustesVigencia.isChecked());
        });
        swAjustesRegistrarEmpleado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setRegistrarEmpleado(swAjustesRegistrarEmpleado.isChecked());
        });
        swAjustesRegistrarTareoNotEmpleado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FLAG_TAB_AJUSTES = true;
            presenter.setRegistrarTareoNotEmpleado(swAjustesRegistrarTareoNotEmpleado.isChecked());
        });
        sliderTimeWorker.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                Log.d(TAG, "ONPROGRESS  ->" + progress);
                FLAG_TAB_AJUSTES = true;
                presenter.setTimeSendDataWorker(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                super.getProgressOnActionUp(bubbleSeekBar, progress, progressFloat);
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
            }
        });
        rbLinea.setOnClickListener(view -> {
            FLAG_TAB_GENERAL = true;
            rbBatch.setChecked(false);
            presenter.setModoTrabajo(ModoTrabajo.LINEA);
            setTextButton(getString(R.string.btn_validar_conexion));
        });
        rbBatch.setOnClickListener(view -> {
            FLAG_TAB_GENERAL = true;
            rbLinea.setChecked(false);
            presenter.setModoTrabajo(ModoTrabajo.BATCH);
            setTextButton(getString(R.string.btn_descarga_maestro));
        });
        swErroresDetallados.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setErroresDetallados(swErroresDetallados.isChecked());
        });
        swSound.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setSound(swSound.isChecked());
        });
        swValidDescarga.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setValidaDescarga(swValidDescarga.isChecked());
        });
        swModuloAsistencia.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setMostrarModuloAsistencia(swModuloAsistencia.isChecked());
        });
        swModuloIncidencia.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setMostrarModuloIncidencia(swModuloIncidencia.isChecked());
        });
        swModuloAcceso.setOnCheckedChangeListener((compoundButton, b) -> {
            FLAG_TAB_GENERAL = true;
            presenter.setMostrarModuloAcceso(swModuloAcceso.isChecked());
        });
    }

    @Override
    public void actualizarFechaHora(String mensaje) {
        new CustomDialog.Builder(SettingActivity.this)
                .setTheme(R.style.AppTheme_Dialog_Error)
                .setTitle("Error de Fecha y Hora")
                .setMessage(mensaje)
                .setIcon(R.drawable.ic_error)
                .setPositiveButtonLabel("Cambiar fecha")
                .setPositiveButtonlistener(() -> {
                    abrirVentanaConfigurarFechaHora();
                })
                .build()
                .show();
    }

    public void abrirVentanaConfigurarFechaHora() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
        } else {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.DateTimeSettingsSetupWizard"));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general, menu);
        return true;
    }

    private void mostrarMensajeConfirmacion() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Configuración")
                .setMessage("¿Desea guardar los cambios realizados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Guardar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                    presenter.guardarCampos(FLAG_URL, FLAG_TAB_GENERAL, FLAG_TAB_AJUSTES, FLAG_TAB_TAREO);
                })
                .setNegativeButtonlistener(() -> {
                    closed();
                });
        dialog.build().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}