package com.dms.tareosoft.presentation.acceso.registro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.NivelConceptoTareo;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.presentation.activities.search.SearchActivity;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.incidencia.RegistroIncidenciaFragment;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroAccesoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroAccesoFragment extends BaseFragment
        implements IRegistroAccesoContract.View,
        View.OnClickListener {

    static String TAG = RegistroAccesoFragment.class.getSimpleName();

    @BindView(R.id.tv_fecha)
    TextView tvFechaHoy;
    @BindView(R.id.tv_codigo_solicitud)
    TextView tv_codigo_solicitud;
    @BindView(R.id.tv_agencia)
    TextView tv_agencia;
    @BindView(R.id.tv_fecha_ini)
    TextView tv_fecha_ini;
    @BindView(R.id.tv_hora_ini)
    TextView tv_hora_ini;
    @BindView(R.id.tv_fecha_fin)
    TextView tv_fecha_fin;
    @BindView(R.id.tv_hora_fin)
    TextView tv_hora_fin;
    @BindView(R.id.tv_detalle_motivo)
    TextView tv_detalle_motivo;
    @BindView(R.id.tv_zonas)
    TextView tv_zonas;
    @BindView(R.id.tv_area_solicitante)
    TextView tv_area_solicitante;
    @BindView(R.id.tv_motivo)
    TextView tv_motivo;
    @BindView(R.id.tv_empresa)
    TextView tv_empresa;
    @BindView(R.id.tv_visitante)
    TextView tv_visitante;
    @BindView(R.id.tv_equipo_portatil)
    TextView tv_equipo_portatil;
    @BindView(R.id.tv_serie_marca)
    TextView tv_serie_marca;
    @BindView(R.id.imv_cerrar)
    ImageView imv_cerrar;
    @BindView(R.id.ly_data_dialog)
    ConstraintLayout ly_data_dialog;


    @Inject
    RegistroAccesoPresenter presenter;
    @Inject
    PreferenceManager preferenceManager;


    public static RegistroIncidenciaFragment newInstance() {
        RegistroIncidenciaFragment fragment = new RegistroIncidenciaFragment();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        fragment.setArguments(args);

        return fragment;
    }


    private BroadcastReceiver scannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
            if (action != null && action.equals(getString(R.string.activity_intent_filter_action))) {
                try {
                    String decodedData = intent.getStringExtra(getString(R.string.datawedge_intent_key_data));
                    Log.d("AQUI", decodedData);
                    String json = decodedData;
                    Acceso acceso = new Acceso();
                    try {
                         acceso = convertJsonAccesoToObject(json);
                        presenter.registroAcceso(acceso);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Código QR incorrecto", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void configureDefaultProfile(Context context) {
        String profileName = "Profile0 (default)"; // Nombre del perfil predeterminado

        // Configurar la salida del escáner a través de Intents
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");

        // Configuración de salida por Intent
        Bundle intentConfig = new Bundle();
        intentConfig.putString("PLUGIN_NAME", "INTENT");
        intentConfig.putString("RESET_CONFIG", "true");

        Bundle intentProps = new Bundle();
        intentProps.putString("intent_output_enabled", "true");
        intentProps.putString("intent_action", getString(R.string.activity_intent_filter_action));

        intentProps.putString("intent_delivery", "2"); // 0: StartActivity, 1: StartService, 2: Broadcast

        intentConfig.putBundle("PARAM_LIST", intentProps);

        profileConfig.putBundle("PLUGIN_CONFIG", intentConfig);

        // Enviar el Intent para configurar el perfil
        Intent intent = new Intent();
        intent.setAction("com.symbol.datawedge.api.ACTION");
        intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", profileConfig);

        context.sendBroadcast(intent);
    }

    private void registerScannerReceiver() {
        IntentFilter filter = new IntentFilter();

        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getString(R.string.activity_intent_filter_action));
        requireActivity().registerReceiver(scannerReceiver, filter);

    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_registro_acceso;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        configureDefaultProfile(requireContext());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void setupView() {
        setAdapters();
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Registro Accesso");
        presenter.attachView(this);

        tvFechaHoy.setText(DateUtil.obtenerFechaHoraEquipo("dd-MM-YYYY"));
        registerScannerReceiver();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imv_cerrar.setOnLongClickListener(v -> {

            ly_data_dialog.setVisibility(View.GONE);
            return true;
        });
    }

    private Acceso convertJsonAccesoToObject(String json) {
        Log.d("aqui", "json -> " + json);
        String[] values = json.split(",");
        Acceso acceso = new Acceso();
        acceso.setCod_solicitud(values[0].trim());
        acceso.setAgencia_oficina(values[1].trim());
        acceso.setFech_inicial(values[2].trim());
        acceso.setHora_inicio(values[3].trim());
        acceso.setFecha_final(values[4].trim());
        acceso.setHora_final(values[5].trim());
        acceso.setDetalle_motivo(values[6].trim());
        acceso.setZonas(values[7].trim());
        acceso.setArea_solicitante(values[8].trim());
        acceso.setMotivo(values[9].trim());
        acceso.setEmpresa(values[10].trim());
        acceso.setVisitante_personal(values[11].trim());
        acceso.setEquipo_portatil(values[12].trim());
        acceso.setSerie_marca(values[13].trim());

       return acceso;
    }
    private void setAdapters() {


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        getActivity().unregisterReceiver(scannerReceiver);
    }

    @Override
    public void listarClaseTareos(List<String> lista, int posicion) {

    }

    @Override
    public void actualizarVistaNiveles(List<NivelTareo> listaNiveles) {


    }

    @Override
    public boolean hasTareo() {
        return false;
    }

    @Override
    public void viewResult(Acceso acceso) {
        getActivity().runOnUiThread(() -> {
            ly_data_dialog.setVisibility(View.VISIBLE);
            tv_codigo_solicitud.setText(acceso.getCod_solicitud());
            tv_agencia.setText(acceso.getAgencia_oficina());
            tv_fecha_ini.setText(acceso.getFech_inicial());
            tv_hora_ini.setText(acceso.getHora_inicio());
            tv_fecha_fin.setText(acceso.getFecha_final());
            tv_hora_fin.setText(acceso.getHora_final());
            tv_detalle_motivo.setText(acceso.getDetalle_motivo());
            tv_zonas.setText(acceso.getZonas());
            tv_area_solicitante.setText(acceso.getArea_solicitante());
            tv_motivo.setText(acceso.getMotivo());
            tv_empresa.setText(acceso.getEmpresa());
            tv_visitante.setText(acceso.getVisitante_personal());
            tv_equipo_portatil.setText(acceso.getEquipo_portatil());
            tv_serie_marca.setText(acceso.getSerie_marca());
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);

    }

    @Override
    public void onClick(View v) {

    }


}
