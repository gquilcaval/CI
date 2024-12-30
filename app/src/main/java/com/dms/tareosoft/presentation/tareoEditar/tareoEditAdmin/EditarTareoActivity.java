package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin;

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
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.definicion.DefinicionEditarFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.empleado.EmpleadoEditarFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.opciones.OpcionesEditarFragment;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class EditarTareoActivity extends BaseActivity implements IEditarTareoContract.View {
    private DefinicionEditarFragment tabDefinicion;
    private OpcionesEditarFragment tabOpciones;
    private EmpleadoEditarFragment tabEmpleados;
    private ViewPager mViewPager;
    private String codigoTareo;

    @Inject
    EditarTareoPresenter presenter;

    public static Intent newInstance(Context context, String codTareo) {
        Intent intent = new Intent(context, EditarTareoActivity.class);
        intent.putExtra(Constants.EXT_CODTAREO, codTareo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        //consultarTareo
        if (getIntent().hasExtra(Constants.EXT_CODTAREO)) {
            codigoTareo = getIntent().getStringExtra(Constants.EXT_CODTAREO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + Constants.EXT_CODTAREO);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);
        toolbar.setTitle("Editar Tareo");

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

        tabDefinicion = new DefinicionEditarFragment(codigoTareo);
        tabOpciones = new OpcionesEditarFragment(codigoTareo);
        tabEmpleados = new EmpleadoEditarFragment(codigoTareo);

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
                mostrarMensajeConfirmacion();
                break;
            case android.R.id.home:
                if (tabDefinicion.hasTareo() && tabEmpleados.hasTareo()) {
                    mostrarMensajeConfirmacion();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_nuevo_tareo;
    }

    private void mostrarMensajeConfirmacion() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Configuración")
                .setMessage("¿Desea guardar los cambios realizados?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Guardar")
                .setNegativeButtonLabel("Salir")
                .setPositiveButtonlistener(() -> {
                    presenter.actualizarTareo(tabDefinicion.getTareo(),
                            tabDefinicion.totalNiveles(),
                            tabEmpleados.getEmpleados(),
                            tabOpciones.obtenerCampos());
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
    public String getHoraInicio() {
        return tabOpciones.obtenerCampos().getHoraInicio();
    }

    @Override
    public String getFechaInicio() {
        return tabOpciones.obtenerCampos().getFechaInicio();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
