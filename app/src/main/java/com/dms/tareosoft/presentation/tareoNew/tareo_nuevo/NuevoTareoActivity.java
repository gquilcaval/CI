package com.dms.tareosoft.presentation.tareoNew.tareo_nuevo;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.definicion.DefinicionFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado.EmpleadoFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.opciones.OpcionesFragment;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NuevoTareoActivity extends BaseActivity implements INuevoTareoContract.View {

    String TAG = NuevoTareoActivity.class.getSimpleName();

    private DefinicionFragment tabDefinicion;
    private OpcionesFragment tabOpciones;
    private EmpleadoFragment tabEmpleados;
    private ViewPager mViewPager;

    @Inject
    NuevoTareoPresenter presenter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, NuevoTareoActivity.class);
        return intent;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_nuevo_tareo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);
        toolbar.setTitle("Nuevo Tareo");

        TabLayout mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_title_main_1));
        titles.add(getString(R.string.tab_title_main_2));
        titles.add(getString(R.string.tab_title_main_3));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));

        tabDefinicion = new DefinicionFragment();
        tabOpciones = new OpcionesFragment();
        tabEmpleados = new EmpleadoFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabDefinicion);
        fragments.add(tabOpciones);
        fragments.add(tabEmpleados);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        presenter.attachView(this);
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
        switch (item.getItemId()) {
            case R.id.action_save:
                if (tabDefinicion.hasTareo() && tabEmpleados.hasTareo()) {
                    presenter.guardarTareo(tabDefinicion.getTareo(), tabDefinicion.totalNiveles(),
                            tabEmpleados.getEmpleados(), tabOpciones.obtenerCampos());
                } else {
                    showWarningMessage(getString(R.string.datos_vacios));
                }
                break;
            case android.R.id.home:
                if (tabDefinicion.hasTareo() || tabEmpleados.hasTareo()) {
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
                .setTitle("Atención")
                .setMessage("¿Está seguro de salir, se perderán todos los valores ingresados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("continuar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
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
            if (position == 2) {
                tabEmpleados.setNomina(tabDefinicion.getNomina());
                tabEmpleados.setFechaInicio(tabOpciones.obtenerCampos().getFechaInicio());
                tabEmpleados.setHoraInicio(tabOpciones.obtenerCampos().getHoraInicio());
                Log.e(TAG, "Fecha inicio: " + tabOpciones.obtenerCampos().getFechaInicio());
                Log.e(TAG, "Hora Inicio: " + tabOpciones.obtenerCampos().getHoraInicio());
            }
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
    public String getFechaInicio() {
        return tabOpciones.obtenerCampos().getFechaInicio();
    }

    @Override
    public String getHoraInicio() {
        return tabOpciones.obtenerCampos().getHoraInicio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}