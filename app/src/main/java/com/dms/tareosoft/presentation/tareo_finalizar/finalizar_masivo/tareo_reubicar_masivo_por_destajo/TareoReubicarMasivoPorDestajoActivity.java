package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list.TareoReubicarMasivoListActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_cantidad_por_destajo.TareoReubicarMasivoPorDestajoCantidadFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_finalizar_por_destajo.TareoReubicarMasivoPorDestajoFinalizarFragment;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

public class TareoReubicarMasivoPorDestajoActivity extends BaseActivity
        implements ITareoReubicarMasivoPorDestajoContract.View {
    String TAG = TareoReubicarMasivoPorDestajoActivity.class.getSimpleName();

    public static String LISTA_ALL_EMPLEADOS = "allEmpleados";
    public static String LISTA_ALL_TAREOS = "allTareos";
    public static String LISTA_ALL_POR_DESTAJO = "allPorDestajo";
    public static String LISTA_FECHA_TAREOS = "listaFechaTareos";

    private ViewPager mViewPager;
    private TareoReubicarMasivoPorDestajoCantidadFragment tabCantidad;
    private TareoReubicarMasivoPorDestajoFinalizarFragment tabFinalizar;

    @Inject
    TareoReubicarMasivoPorDestajoPresenter presenter;

    ArrayList<AllEmpleadoRow> allEmpleados = new ArrayList<>();
    ArrayList<TareoRow> allTareos = new ArrayList<>();
    ArrayList<AllEmpleadoRow> allPorDestajo = new ArrayList<>();
    ArrayList<String> listaFechaTareos = new ArrayList<>();

    public static Intent newInstance(Context context,
                                     List<AllEmpleadoRow> allEmpleados,
                                     List<TareoRow> listaAllTareos,
                                     List<AllEmpleadoRow> allPorDestajo,
                                     List<String> listaFechaTareos) {
        Intent intent = new Intent(context, TareoReubicarMasivoPorDestajoActivity.class);
        intent.putExtra(LISTA_ALL_EMPLEADOS, (Serializable) allEmpleados);
        intent.putExtra(LISTA_ALL_TAREOS, (Serializable) listaAllTareos);
        intent.putExtra(LISTA_ALL_POR_DESTAJO, (Serializable) allPorDestajo);
        intent.putExtra(LISTA_FECHA_TAREOS, (Serializable) listaFechaTareos);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        if (getIntent() != null) {
            allEmpleados = (ArrayList<AllEmpleadoRow>)
                    getIntent().getSerializableExtra(LISTA_ALL_EMPLEADOS);
            allTareos = (ArrayList<TareoRow>)
                    getIntent().getSerializableExtra(LISTA_ALL_TAREOS);
            allPorDestajo = (ArrayList<AllEmpleadoRow>)
                    getIntent().getSerializableExtra(LISTA_ALL_POR_DESTAJO);
            listaFechaTareos = (ArrayList<String>)
                    getIntent().getSerializableExtra(LISTA_FECHA_TAREOS);
        }

        presenter.setListaFechaTareos(listaFechaTareos);
        presenter.setListAllEmpleados(allEmpleados);
        presenter.setListAllTareos(allTareos);

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);

        TabLayout mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_tareo_reubicar_cantidad));
        titles.add(getString(R.string.tab_tareo_reubicar_finalizar));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        tabCantidad = new TareoReubicarMasivoPorDestajoCantidadFragment().newInstance(allPorDestajo);
        tabFinalizar = new TareoReubicarMasivoPorDestajoFinalizarFragment().newInstance(listaFechaTareos);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabCantidad);
        fragments.add(tabFinalizar);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        toolbar.setTitle(getResources().getString(R.string.finalizar_empleados_x,
                allEmpleados.size()));

        presenter.attachView(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tareo_reubicar_masivo_por_destajo;
    }

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.fechaFinTareo(tabFinalizar.getFechaFinTareo());
        presenter.horaFinTareo(tabFinalizar.getHoraFinTareo());
        presenter.fechaIniRefrigerio(tabFinalizar.getFechaIniRefrigerio());
        presenter.fechaFinRefrigerio(tabFinalizar.getFechaFinRefrigerio());
        switch (item.getItemId()) {
            case R.id.action_save:
                if (tabCantidad.hasCantidad() && tabFinalizar.hasFinalizar()) {
                    presenter.validarDatosParaFinalizar(tabFinalizar.hasRefrigerio(),
                            tabCantidad.listaResultado(), tabCantidad.listResultadoXTareo());
                } else {
                    if (!tabCantidad.hasCantidad()) {
                        showWarningMessage(getString(R.string.cantidad_vacia));
                    }
                    if (!tabFinalizar.hasFinalizar()) {
                        showWarningMessage(getString(R.string.datos_vacios));
                    }

                }
                break;
            case android.R.id.home:
                if (tabCantidad.hasCantidad() && tabFinalizar.hasFinalizar()) {
                    mostrarMensajeConfirmacion();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarMensajeConfirmacion() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Configuración")
                .setMessage("¿Desea guardar los cambios realizados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Guardar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                   /* presenter.guardarTareo(tabDefinicion.getTareo(), tabDefinicion.totalNiveles(),
                            tabEmpleados.getEmpleados(), tabOpciones.obtenerCampos());*/
                })
                .setNegativeButtonlistener(() -> {
                    onBackPressed();
                });
        dialog.build().show();
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            /*if (position == 2) {
                tabEmpleados.setNomina(tabDefinicion.getNomina());
                tabEmpleados.setFechaHoraInicio(tabOpciones.obtenerCampos().getFechaInicio(), tabOpciones.obtenerCampos().getHoraInicio());
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void salir() {
        finish();
    }

    @Override
    public void changePage(int page) {
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void showDetailedErrorDialog(ArrayList<String> listaErrores) {
        UtilsMethods.showDetailedErrorDialog(this, listaErrores).show();
    }

    @Override
    public void moveToTareoReubicarMasivoListActivity(Boolean correctly) {
        if (correctly) {
            startActivity(TareoReubicarMasivoListActivity.newInstance(
                    TareoReubicarMasivoPorDestajoActivity.this, allEmpleados));
            finish();
        } else {
            showErrorMessage("Hubo un error al actualizar", "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}