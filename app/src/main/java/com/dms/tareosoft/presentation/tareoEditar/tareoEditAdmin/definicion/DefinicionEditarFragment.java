package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.definicion;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.adapter.AutoCompleteConceptoTareoAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DefinicionEditarFragment extends BaseFragment
        implements IDefinicionEditarContract.View {
    String TAG = DefinicionEditarFragment.class.getSimpleName();

    @BindView(R.id.tv_clase_tareo)
    TextView tvClaseTareo;
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
    DefinicionEditarPresenter presenter;

    private String mCodTareo;
    private boolean FLAG_TAREO;
    private int cantidadNiveles;
    private List<ConceptoTareo> listaConcepto1, listaConcepto2, listaConcepto3, listaConcepto4, listaConcepto5;
    private AutoCompleteConceptoTareoAdapter adConcepto1, adConcepto2, adConcepto3, adConcepto4, adConcepto5;

    public DefinicionEditarFragment(String codTareo) {
        mCodTareo = codTareo;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_editar_definicion;
    }

    @Override
    protected void setupVariables() {
        listaConcepto1 = new ArrayList<>();
        listaConcepto2 = new ArrayList<>();
        listaConcepto3 = new ArrayList<>();
        listaConcepto4 = new ArrayList<>();
        listaConcepto5 = new ArrayList<>();
        FLAG_TAREO = false;
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void setupView() {
        setAdapters();

        getActivityComponent().inject(this);
        presenter.attachView(this);
        presenter.setCodTareo(mCodTareo);
        blockTextView();
        presenter.cargarTareo();
    }

    private void setAdapters() {

        adConcepto1 = new AutoCompleteConceptoTareoAdapter(getActivity(),
                R.layout.row_auto_conceptotareo, listaConcepto1);
        autoCompleteNivel1.setThreshold(1);
        autoCompleteNivel1.setAdapter(adConcepto1);

        autoCompleteNivel1.setOnItemClickListener((parent, view, position, id) -> {
            ConceptoTareo model = (ConceptoTareo) parent.getItemAtPosition(position);
            autoCompleteNivel1.setText(model.getDescripcion());
            presenter.setNivel1(model.getIdConceptoTareo());
        });

        adConcepto2 = new AutoCompleteConceptoTareoAdapter(getActivity(),
                R.layout.row_auto_conceptotareo, listaConcepto2);
        autoCompleteNivel2.setThreshold(1);
        autoCompleteNivel2.setAdapter(adConcepto2);

        autoCompleteNivel2.setOnItemClickListener((parent, view, position, id) -> {
            ConceptoTareo model = (ConceptoTareo) parent.getItemAtPosition(position);
            autoCompleteNivel2.setText(model.getDescripcion());
            presenter.setNivel2(model.getIdConceptoTareo());
        });

        adConcepto3 = new AutoCompleteConceptoTareoAdapter(getActivity(),
                R.layout.row_auto_conceptotareo, listaConcepto3);
        autoCompleteNivel3.setThreshold(1);
        autoCompleteNivel3.setAdapter(adConcepto3);

        autoCompleteNivel3.setOnItemClickListener((parent, view, position, id) -> {
            ConceptoTareo model = (ConceptoTareo) parent.getItemAtPosition(position);
            autoCompleteNivel3.setText(model.getDescripcion());
            presenter.setNivel3(model.getIdConceptoTareo());
        });

        adConcepto4 = new AutoCompleteConceptoTareoAdapter(getActivity(),
                R.layout.row_auto_conceptotareo, listaConcepto4);
        autoCompleteNivel4.setThreshold(1);
        autoCompleteNivel4.setAdapter(adConcepto4);

        autoCompleteNivel4.setOnItemClickListener((parent, view, position, id) -> {
            ConceptoTareo model = (ConceptoTareo) parent.getItemAtPosition(position);
            autoCompleteNivel4.setText(model.getDescripcion());
            presenter.setNivel4(model.getIdConceptoTareo());
        });

        adConcepto5 = new AutoCompleteConceptoTareoAdapter(getActivity(),
                R.layout.row_auto_conceptotareo, listaConcepto5);
        autoCompleteNivel5.setThreshold(1);
        autoCompleteNivel5.setAdapter(adConcepto5);

        autoCompleteNivel5.setOnItemClickListener((parent, view, position, id) -> {
            ConceptoTareo model = (ConceptoTareo) parent.getItemAtPosition(position);
            autoCompleteNivel5.setText(model.getDescripcion());
            presenter.setNivel5(model.getIdConceptoTareo());
        });
    }

    private void blockTextView() {
        autoCompleteNivel1.setKeyListener(null);
        autoCompleteNivel2.setKeyListener(null);
        autoCompleteNivel3.setKeyListener(null);
        autoCompleteNivel4.setKeyListener(null);
        autoCompleteNivel5.setKeyListener(null);
    }

    @Override
    public void actualizarSeccionNiveles(List<NivelTareo> listaNiveles) {
        if (listaNiveles != null) {
            cantidadNiveles = listaNiveles.size();
            int indice = 0;

            if (cantidadNiveles > indice) {
                tvNivel1.setVisibility(View.VISIBLE);
                autoCompleteNivel1.setVisibility(View.VISIBLE);

                tvNivel1.setText(listaNiveles.get(indice).getDescripcion());
                presenter.obtenerConceptoTareo1(listaNiveles.get(indice).getIdNivel());
            } else {
                tvNivel1.setVisibility(View.GONE);
                autoCompleteNivel1.setVisibility(View.GONE);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel2.setVisibility(View.VISIBLE);
                autoCompleteNivel2.setVisibility(View.VISIBLE);

                tvNivel2.setText(listaNiveles.get(indice).getDescripcion());
                presenter.obtenerConceptoTareo2(listaNiveles.get(indice).getIdNivel());
            } else {
                tvNivel2.setVisibility(View.GONE);
                autoCompleteNivel2.setVisibility(View.GONE);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel3.setVisibility(View.VISIBLE);
                autoCompleteNivel3.setVisibility(View.VISIBLE);

                tvNivel3.setText(listaNiveles.get(indice).getDescripcion());
                presenter.obtenerConceptoTareo3(listaNiveles.get(indice).getIdNivel());
            } else {
                tvNivel3.setVisibility(View.GONE);
                autoCompleteNivel3.setVisibility(View.GONE);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel4.setVisibility(View.VISIBLE);
                autoCompleteNivel4.setVisibility(View.VISIBLE);

                tvNivel4.setText(listaNiveles.get(indice).getDescripcion());
                presenter.obtenerConceptoTareo4(listaNiveles.get(indice).getIdNivel());
            } else {
                tvNivel4.setVisibility(View.GONE);
                autoCompleteNivel4.setVisibility(View.GONE);
            }

            indice += 1;
            if (cantidadNiveles > indice) {
                tvNivel5.setVisibility(View.VISIBLE);
                autoCompleteNivel5.setVisibility(View.VISIBLE);

                tvNivel5.setText(listaNiveles.get(indice).getDescripcion());
                presenter.obtenerConceptoTareo5(listaNiveles.get(indice).getIdNivel());
            } else {
                tvNivel5.setVisibility(View.GONE);
                autoCompleteNivel5.setVisibility(View.GONE);
            }

            hiddenProgressbar();
            FLAG_TAREO = true;
        }
    }

    @Override
    public void listarConceptoTareo1(List<ConceptoTareo> lista,
                                     String descripcionConcepto) {
        listaConcepto1.clear();
        listaConcepto1.addAll(lista);

        adConcepto1.notifyDataSetChanged();
        adConcepto1.updateOriginalList();

        autoCompleteNivel1.setText(descripcionConcepto);
        autoCompleteNivel1.dismissDropDown();
        autoCompleteNivel1.clearFocus();
    }

    @Override
    public void listarConceptoTareo2(List<ConceptoTareo> lista,
                                     String descripcionConcepto) {
        listaConcepto2.clear();
        listaConcepto2.addAll(lista);

        adConcepto2.notifyDataSetChanged();
        adConcepto2.updateOriginalList();

        autoCompleteNivel2.setText(descripcionConcepto);
    }

    @Override
    public void listarConceptoTareo3(List<ConceptoTareo> lista,
                                     String descripcionConcepto) {
        listaConcepto3.clear();
        listaConcepto3.addAll(lista);

        adConcepto3.notifyDataSetChanged();
        adConcepto3.updateOriginalList();

        autoCompleteNivel3.setText(descripcionConcepto);
    }

    @Override
    public void listarConceptoTareo4(List<ConceptoTareo> lista,
                                     String descripcionConcepto) {
        listaConcepto4.clear();
        listaConcepto4.addAll(lista);

        adConcepto4.notifyDataSetChanged();
        adConcepto4.updateOriginalList();

        autoCompleteNivel4.setText(descripcionConcepto);
    }

    @Override
    public void listarConceptoTareo5(List<ConceptoTareo> lista,
                                     String descripcionConcepto) {
        listaConcepto5.clear();
        listaConcepto5.addAll(lista);

        adConcepto5.notifyDataSetChanged();
        adConcepto5.updateOriginalList();

        autoCompleteNivel5.setText(descripcionConcepto);
    }

    @Override
    public boolean hasTareo() {
        return FLAG_TAREO;
    }

    @Override
    public void actualizarClaseTareo(String descripcion) {
        Log.e(TAG, "actualizarClaseTareo: " + descripcion);
        tvClaseTareo.setText(descripcion);
    }

    public String getNomina() {
        if (!TextUtils.isEmpty(presenter.obtenerNomina()))
            return presenter.obtenerNomina();
        else
            return null;
    }

    public Tareo getTareo() {
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
}
