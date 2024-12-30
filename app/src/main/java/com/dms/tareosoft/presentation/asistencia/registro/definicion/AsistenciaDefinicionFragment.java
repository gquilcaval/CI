package com.dms.tareosoft.presentation.asistencia.registro.definicion;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.NivelConceptoTareo;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.presentation.activities.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AsistenciaDefinicionFragment extends BaseFragment implements IAsistenciaDefinicionContract.View,
        View.OnClickListener {

    static String TAG = AsistenciaDefinicionFragment.class.getSimpleName();

    @BindView(R.id.tv_clase_tareo)
    Spinner spClaseTareo;
    @BindView(R.id.tv_nivel1)
    TextView tvNivel1;
    @BindView(R.id.tv_nivel2)
    TextView tvNivel2;
    @BindView(R.id.tv_nivel3)
    TextView tvNivel3;
    @BindView(R.id.tv_nivel4)
    TextView tvNivel4;
    @BindView(R.id.tv_nivel5)
    TextView tvNivel5;
    @BindView(R.id.actv_nivel1)
    AutoCompleteTextView autoCompleteNivel1;
    @BindView(R.id.edt_msg_incidencia)
    AutoCompleteTextView autoCompleteNivel2;
    @BindView(R.id.actv_nivel3)
    AutoCompleteTextView autoCompleteNivel3;
    @BindView(R.id.actv_nivel4)
    AutoCompleteTextView autoCompleteNivel4;
    @BindView(R.id.actv_nivel5)
    AutoCompleteTextView autoCompleteNivel5;

    @Inject
    AsistenciaDefinicionPresenter presenter;

    private boolean FLAG_TAREO;
    private int cantidadNiveles;
    private List<String> listaClaseTareos;
    private ArrayAdapter<String> adapterClaseTareos;
    private int fkNivel1, fkNivel2, fkNivel3, fkNivel4, fkNivel5;
    private int fkPadre1, fkPadre2, fkPadre3, fkPadre4, fkPadre5;

    @Override
    protected int getLayout() {
        return R.layout.fragment_registro_asistencia_definicion;
    }

    @Override
    protected void setupVariables() {
        listaClaseTareos = new ArrayList<>();
        FLAG_TAREO = false;
    }

    @Override
    protected void initEvents() {
        autoCompleteNivel1.setInputType(InputType.TYPE_NULL);
        autoCompleteNivel2.setInputType(InputType.TYPE_NULL);
        autoCompleteNivel3.setInputType(InputType.TYPE_NULL);
        autoCompleteNivel4.setInputType(InputType.TYPE_NULL);
        autoCompleteNivel5.setInputType(InputType.TYPE_NULL);
    }

    @Override
    protected void setupView() {
        setAdapters();

        getActivityComponent().inject(this);
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
    public void listarClaseTareos(List<String> lista, int posicion) {
        listaClaseTareos.clear();
        listaClaseTareos.addAll(lista);
        adapterClaseTareos.notifyDataSetChanged();
        spClaseTareo.setSelection(2);
        presenter.setClaseTareo(2);  // selection control asistencia default int 2
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

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel3.setVisibility(View.VISIBLE);
                autoCompleteNivel3.setVisibility(View.VISIBLE);
                autoCompleteNivel3.setText("");
                autoCompleteNivel3.setOnClickListener(this);
                tvNivel3.setText(listaNiveles.get(indice).getDescripcion());
                fkNivel3 = listaNiveles.get(indice).getIdNivel();
                Log.e(TAG, "fkNivel3: " + fkNivel3);
                Log.e(TAG, "listaNiveles3: " + listaNiveles.get(indice));
            } else {
                tvNivel3.setVisibility(View.GONE);
                autoCompleteNivel3.setVisibility(View.GONE);
                autoCompleteNivel3.setOnClickListener(null);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel4.setVisibility(View.VISIBLE);
                autoCompleteNivel4.setVisibility(View.VISIBLE);
                autoCompleteNivel4.setText("");
                autoCompleteNivel4.setOnClickListener(this);
                tvNivel4.setText(listaNiveles.get(indice).getDescripcion());
                fkNivel4 = listaNiveles.get(indice).getIdNivel();
                Log.e(TAG, "fkNivel4: " + fkNivel4);
                Log.e(TAG, "listaNiveles4: " + listaNiveles.get(indice));
            } else {
                tvNivel4.setVisibility(View.GONE);
                autoCompleteNivel4.setVisibility(View.GONE);
                autoCompleteNivel4.setOnClickListener(null);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel5.setVisibility(View.VISIBLE);
                autoCompleteNivel5.setVisibility(View.VISIBLE);
                autoCompleteNivel5.setText("");
                autoCompleteNivel5.setOnClickListener(this);
                tvNivel5.setText(listaNiveles.get(indice).getDescripcion());
                fkNivel5 = listaNiveles.get(indice).getIdNivel();
                Log.e(TAG, "fkNivel5: " + fkNivel5);
                Log.e(TAG, "listaNiveles5: " + listaNiveles.get(indice));
            } else {
                tvNivel5.setVisibility(View.GONE);
                autoCompleteNivel5.setVisibility(View.GONE);
                autoCompleteNivel5.setOnClickListener(null);
            }

            FLAG_TAREO = true;
        }
        if (!presenter.getPerfilUser().contains("ADMINISTRADOR")) {
            Log.d(TAG,"moveToSearchActivity");
            moveToSearchActivity(6, 0, NivelConceptoTareo.CONCEPTO_1);
        }

    }

    @Override
    public boolean hasTareo() {
        return FLAG_TAREO;
    }


    public Marcacion getTareo() {
        return presenter.obtenerTareo();
    }

    public int totalNiveles() {
        return cantidadNiveles;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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

                        @NivelConceptoTareo
                        int concepto = data.getIntExtra("concepto", 0);
                        switch (concepto) {
                            case NivelConceptoTareo.CONCEPTO_1:
                                fkPadre1 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel1.setText(conceptoTareo.getDescripcion());
                                autoCompleteNivel2.setText("");
                                autoCompleteNivel3.setText("");
                                autoCompleteNivel4.setText("");
                                autoCompleteNivel5.setText("");
                                fkPadre2 = 0;
                                fkPadre3 = 0;
                                fkPadre4 = 0;
                                fkPadre5 = 0;
                                presenter.setNivel1(conceptoTareo.getDescripcion());
                                presenter.setNivel2(conceptoTareo.getDescripcion());

                                if (autoCompleteNivel2.getVisibility() == View.VISIBLE)

                                break;
                            case NivelConceptoTareo.CONCEPTO_2:
                                fkPadre2 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel2.setText(conceptoTareo.getDescripcion());
                                autoCompleteNivel3.setText("");
                                autoCompleteNivel4.setText("");
                                autoCompleteNivel5.setText("");
                                fkPadre3 = 0;
                                fkPadre4 = 0;
                                fkPadre5 = 0;
                                presenter.setNivel2(conceptoTareo.getDescripcion());
                                if (autoCompleteNivel3.getVisibility() == View.VISIBLE)
                                    autoCompleteNivel3.requestFocus();
                                break;
                            case NivelConceptoTareo.CONCEPTO_3:
                                fkPadre3 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel3.setText(conceptoTareo.getDescripcion());
                                autoCompleteNivel4.setText("");
                                autoCompleteNivel5.setText("");
                                fkPadre4 = 0;
                                fkPadre5 = 0;

                                if (autoCompleteNivel4.getVisibility() == View.VISIBLE)
                                    autoCompleteNivel4.requestFocus();
                                break;
                            case NivelConceptoTareo.CONCEPTO_4:
                                fkPadre4 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel4.setText(conceptoTareo.getDescripcion());
                                autoCompleteNivel5.setText("");
                                fkPadre5 = 0;

                                if (autoCompleteNivel5.getVisibility() == View.VISIBLE)
                                    autoCompleteNivel5.requestFocus();
                                break;
                            case NivelConceptoTareo.CONCEPTO_5:
                                fkPadre5 = conceptoTareo.getIdConceptoTareo();
                                autoCompleteNivel5.setText(conceptoTareo.getDescripcion());
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
            case R.id.edt_msg_incidencia:
                if (fkNivel2 > 0 && fkPadre1 > 0)
                    moveToSearchActivity(fkNivel2, fkPadre1, NivelConceptoTareo.CONCEPTO_2);
                else
                    showErrorMessage("Debe seleccionar el nivel 1", "");
                break;
            case R.id.actv_nivel3:
                if (fkNivel3 > 0 && fkPadre2 > 0)
                    moveToSearchActivity(fkNivel3, fkPadre2, NivelConceptoTareo.CONCEPTO_3);
                else
                    showErrorMessage("Debe seleccionar el nivel 2", "");
                break;
            case R.id.actv_nivel4:
                if (fkNivel4 > 0 && fkPadre3 > 0)
                    moveToSearchActivity(fkNivel4, fkPadre3, NivelConceptoTareo.CONCEPTO_4);
                else
                    showErrorMessage("Debe seleccionar el nivel 3", "");
                break;
            case R.id.actv_nivel5:
                if (fkNivel5 > 0 && fkPadre4 > 0)
                    moveToSearchActivity(fkNivel5, fkPadre4, NivelConceptoTareo.CONCEPTO_5);
                else
                    showErrorMessage("Debe seleccionar el nivel 4", "");
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
