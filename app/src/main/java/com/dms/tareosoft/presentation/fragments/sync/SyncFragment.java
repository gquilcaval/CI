package com.dms.tareosoft.presentation.fragments.sync;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import butterknife.BindView;

public class SyncFragment extends BaseFragment implements ISyncContract.View,
        View.OnClickListener {

    String TAG = SyncFragment.class.getSimpleName();

    @BindView(R.id.mb_fechahora)
    MaterialButton mbFechaHora;
    @BindView(R.id.mb_syncFecha)
    MaterialButton mbSyncFechaHora;
    @BindView(R.id.mb_carga)
    MaterialButton mbCarga;
    @BindView(R.id.mb_descarga)
    MaterialButton mbDescarga;
    @BindView(R.id.tv_fecha_servidor)
    TextView tvFechaServidor;
    @BindView(R.id.button_carga_marcaciones)
    MaterialButton btnCargaMarcaciones;

    @Inject
    DateTimeManager appDateTime;
    @Inject
    SyncPresenter presenter;

    Boolean isShowDialog = false;

    public SyncFragment() {
    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                Log.e(TAG, "TIME");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Sincronización");
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_sync;
    }

    @Override
    protected void setupView() {
        tvFechaServidor.setText(appDateTime.getFechaSincronizada(Constants.F_FECHAHORA_WS));
        presenter.attachView(this);
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        mbFechaHora.setOnClickListener(this);
        mbSyncFechaHora.setOnClickListener(this);
        mbCarga.setOnClickListener(this);
        mbDescarga.setOnClickListener(this);
        btnCargaMarcaciones.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void mostrarFechaHora(String fechaServidor, String fechaSincronizada) {
        tvFechaServidor.setText(fechaSincronizada);
    }

    @Override
    public void actualizarFechaHora(String s) {
        //TODO FALTA
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mb_syncFecha:
                presenter.validarConexion();
                break;
            case R.id.mb_carga:
                presenter.cargarMaestros();
                break;
            case R.id.mb_descarga:
                presenter.verifyTareosPending();
                break;
            case R.id.mb_fechahora:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    //presenter.preferenceManager.setFlgFechaHora(false);
                } else {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setComponent(new ComponentName("com.android.settings",
                            "com.android.settings.DateTimeSettingsSetupWizard"));
                    startActivity(intent);
                }
                break;
            case R.id.button_carga_marcaciones:
                presenter.obtenerMarcacionesPendientesEnvio();
                break;
        }
    }

    @Override
    public void mostrarMensajeTareosActivos() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atencion")
                .setMessage("Aun se encuentran tareos pendientes por finalizar y/o liquidar\n" +
                        "Debe finalizar y/o liquidar antes de continuar ")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeSyncExitosa() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atencion")
                .setMessage("Sincronizacion exitosa")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeSyncFallida(String message) {
        if (!isShowDialog) {
            isShowDialog = true;
            Toast.makeText(requireContext(), "hola", Toast.LENGTH_SHORT).show();

            CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                    .setTitle("Atencion")
                    .setMessage("Hubo un problema al actualizar los datos vuelva a intentarlo \n"+
                            message)
                    .setIcon(R.drawable.ic_help_outline)
                    .setPositiveButtonLabel("Aceptar")
                    .setPositiveButtonlistener(() -> {
                        isShowDialog = false;
                    });
            dialog.build().show();

        }

    }

    @Override
    public void mostrarMensajeSinDatos() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atencion")
                .setMessage("Por el momento todos sus datos ya fueron enviados al servidor\n " +
                        "No hay datos para sincronizar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeSinMarcaciones() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Por el momento todas sus marcaciones de asistencia ya fueron enviados al servidor\n " +
                        "No hay datos para sincronizar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }

    @Override
    public void mostrarMensajeSinIncidencias() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext())
                .setTitle("Atención")
                .setMessage("Por el momento todas sus incidencias ya fueron enviados al servidor\n " +
                        "No hay datos para sincronizar")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Aceptar")
                .setPositiveButtonlistener(() -> {
                });
        dialog.build().show();
    }
}