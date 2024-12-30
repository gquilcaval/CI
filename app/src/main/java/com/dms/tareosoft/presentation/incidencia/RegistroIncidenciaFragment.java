package com.dms.tareosoft.presentation.incidencia;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.NivelConceptoTareo;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.models.ContenidoAjustes;
import com.dms.tareosoft.presentation.activities.search.SearchActivity;
import com.dms.tareosoft.presentation.adapter.EmpleadoSimpleAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RegistroIncidenciaFragment extends BaseFragment
        implements IRegistroIncidenciaContract.View,
        View.OnClickListener {

    static String TAG = RegistroIncidenciaFragment.class.getSimpleName();

    @BindView(R.id.tv_clase_tareo)
    Spinner spClaseTareo;
    @BindView(R.id.tv_nivel1)
    TextView tvNivel1;
    @BindView(R.id.tv_nivel2)
    TextView tvNivel2;
    @BindView(R.id.actv_nivel1)
    AutoCompleteTextView autoCompleteNivel1;
    @BindView(R.id.actv_nivel2)
    AutoCompleteTextView autoCompleteNivel2;
    @BindView(R.id.edt_msg_incidencia)
    EditText edtMsgIncidencia;
    @BindView(R.id.btnGuardar)
    MaterialButton btnGuardar;

    @Inject
    RegistroIncidenciaPresenter presenter;
    @Inject
    PreferenceManager preferenceManager;

    private boolean FLAG_TAREO;
    private int cantidadNiveles;
    private List<String> listaClaseTareos;
    private ArrayAdapter<String> adapterClaseTareos;
    private int fkNivel1, fkNivel2;
    private int fkPadre1, fkPadre2, fkPadre3, fkPadre4, fkPadre5;

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

        }
    };

    public static RegistroIncidenciaFragment newInstance() {
        RegistroIncidenciaFragment fragment = new RegistroIncidenciaFragment();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        /*args.putSerializable(Constants.Intent.EXTRA_SOLICITUD_PERMISO, solicitudPermiso);
        args.putInt(Constants.Intent.EXTRA_OPTION_TYPE_VIEW, optionType);*/
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    protected int getLayout() {
        return R.layout.fragment_registro_incidencia;
    }

    @Override
    protected void setupVariables() {
        listaClaseTareos = new ArrayList<>();
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        autoCompleteNivel1.setInputType(InputType.TYPE_NULL);
        autoCompleteNivel2.setInputType(InputType.TYPE_NULL);
    }

    @Override
    protected void setupView() {
        setAdapters();
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Registro Incidencia");
        presenter.attachView(this);
        presenter.obtenerClasesTareo();

    }

    private void setAdapters() {
        adapterClaseTareos = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.spinner_dropdown, listaClaseTareos);
        adapterClaseTareos.setDropDownViewResource(R.layout.spinner_dropdown);
        spClaseTareo.setAdapter(adapterClaseTareos);

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

    @Override
    public void listarClaseTareos(List<String> lista, int posicion) {
        listaClaseTareos.clear();
        listaClaseTareos.addAll(lista);
        adapterClaseTareos.notifyDataSetChanged();
        spClaseTareo.setSelection(3);
        presenter.setClaseTareo(3);  // selection control incidencia default int 3
    }

    @Override
    public void actualizarVistaNiveles(List<NivelTareo> listaNiveles) {
        Log.e(TAG, ": " + listaNiveles);
        if (listaNiveles != null) {
            cantidadNiveles = listaNiveles.size();
            int indice = 0;

            if (cantidadNiveles > indice) {
                tvNivel1.setVisibility(View.VISIBLE);
                autoCompleteNivel1.setVisibility(View.VISIBLE);
                autoCompleteNivel1.setText("");
                autoCompleteNivel1.setOnClickListener(this);
                btnGuardar.setOnClickListener(this);
                tvNivel1.setText(listaNiveles.get(indice).getDescripcion());
                fkNivel1 = listaNiveles.get(indice).getIdNivel();
                Log.e(TAG, "fkNivel1: " + fkNivel1);
                Log.e(TAG, "listaNiveles1: " + listaNiveles.get(indice));
            } else {
                tvNivel1.setVisibility(View.GONE);
                autoCompleteNivel1.setVisibility(View.GONE);
                autoCompleteNivel1.setOnClickListener(null);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel2.setVisibility(View.VISIBLE);
                autoCompleteNivel2.setVisibility(View.VISIBLE);
                autoCompleteNivel2.setText("");
                autoCompleteNivel2.setOnClickListener(this);
                tvNivel2.setText(listaNiveles.get(indice).getDescripcion());
                fkNivel2 = listaNiveles.get(indice).getIdNivel();
                Log.e(TAG, "fkPadre2: " + fkPadre2);
                Log.e(TAG, "listaNiveles2: " + listaNiveles.get(indice));
            } else {
                tvNivel2.setVisibility(View.GONE);
                autoCompleteNivel2.setVisibility(View.GONE);
                autoCompleteNivel2.setOnClickListener(null);
            }

            FLAG_TAREO = true;
        }
        if (!presenter.getPerfilUser().contains("ADMINISTRADOR")) {
            moveToSearchActivity(11, 0, NivelConceptoTareo.CONCEPTO_1);
        }

    }

    @Override
    public boolean hasTareo() {
        return false;
    }

    @Override
    public void limpiarCampos() {
        autoCompleteNivel2.setText("");
        edtMsgIncidencia.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (data != null) {
                        ConceptoTareo conceptoTareo = (ConceptoTareo) data.getSerializableExtra("conceptoTareo");
                        Log.e(TAG, "OnActivityResult concepto tareo " + conceptoTareo);
                        @NivelConceptoTareo
                        int concepto = data.getIntExtra("concepto", 0);
                        switch (concepto) {
                            case NivelConceptoTareo.CONCEPTO_1:
                                fkPadre1 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel1.setText(conceptoTareo.getDescripcion());
                                autoCompleteNivel2.setText("");

                                fkPadre2 = 0;
                                fkPadre3 = 0;
                                fkPadre4 = 0;
                                fkPadre5 = 0;
                                presenter.setNivel1(conceptoTareo.getDescripcion());
                                break;
                            case NivelConceptoTareo.CONCEPTO_2:
                                fkPadre2 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel2.setText(conceptoTareo.getDescripcion());

                                fkPadre3 = 0;
                                fkPadre4 = 0;
                                fkPadre5 = 0;
                                presenter.setNivel2(conceptoTareo.getDescripcion());

                                break;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actv_nivel1:
                if (fkNivel1 > 0)
                    moveToSearchActivity(fkNivel1, 0, NivelConceptoTareo.CONCEPTO_1);
                else
                    showErrorMessage("Debe seleccionar la clase", "");
                break;
            case R.id.actv_nivel2:
                Log.d(TAG, "ACT2 -> " + fkNivel2 + " | "+ fkPadre1  );
                if (fkNivel2 > 0  && fkPadre1 > 0)
                    moveToSearchActivity(fkNivel2, fkPadre1, NivelConceptoTareo.CONCEPTO_2);
                else
                    showErrorMessage("Debe seleccionar el nivel 1" , "");
                break;
            case R.id.btnGuardar:
                presenter.setNivel3(edtMsgIncidencia.getText().toString().trim());
                presenter.registroIncidencia(presenter.obtenerIncidencia());
                break;
        }
    }

    private void moveToSearchActivity(int fkNivel, int fkPadre,
                                      @NivelConceptoTareo int nivelConcepto) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("fkNivel", fkNivel);
        intent.putExtra("fkPadre", fkPadre);
        intent.putExtra("contenido", nivelConcepto);
        startActivityForResult(intent, 100);
    }
}
