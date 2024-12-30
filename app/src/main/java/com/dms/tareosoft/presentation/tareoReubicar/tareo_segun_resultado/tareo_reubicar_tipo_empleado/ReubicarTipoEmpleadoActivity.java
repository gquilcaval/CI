package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos.TareoReubicarListActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.ReubicarTipoEmpleadoCantidadFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_finalizar.ReubicarTipoEmpleadoFinalizarFragment;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class ReubicarTipoEmpleadoActivity extends BaseActivity
        implements IReubicarTipoEmpleadoContract.View {
    public static String horaInicio = ""; //add
    static String TAG = ReubicarTipoEmpleadoActivity.class.getSimpleName();

    public static String LISTA_ALL_EMPLEADOS = "allEmpleados";
    public static String LISTA_FECHA_TAREOS = "listaFechaTareos";

    @Inject
    ReubicarTipoEmpleadoPresenter presenter;

    private ViewPager mViewPager;
    private ReubicarTipoEmpleadoCantidadFragment tabCantidad;
    private ReubicarTipoEmpleadoFinalizarFragment tabFinalizar;
    private ProgressBar progressBar;

    public static Intent newInstance(Context context,
                                     List<AllEmpleadoRow> listEmpleadoPorFinalizar,
                                     List<String> listaFechaTareos) {
        Intent intent = new Intent(context, ReubicarTipoEmpleadoActivity.class);
        intent.putExtra(LISTA_ALL_EMPLEADOS, (Serializable) listEmpleadoPorFinalizar);
        intent.putExtra(LISTA_FECHA_TAREOS, (Serializable) listaFechaTareos);
        Log.e(TAG, "listEmpleadoPorFinalizar: " + listEmpleadoPorFinalizar);
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        List<AllEmpleadoRow> listEmpleadoPorFinalizar = new ArrayList<>();
        List<String> listaFechaTareos = new ArrayList<>();

        if (getIntent() != null) {
            listEmpleadoPorFinalizar = (List<AllEmpleadoRow>)
                    getIntent().getSerializableExtra(LISTA_ALL_EMPLEADOS);
            listaFechaTareos = (List<String>)
                    getIntent().getSerializableExtra(LISTA_FECHA_TAREOS);
        }

        Log.e(TAG, "listEmpleadoPorFinalizar: " + listEmpleadoPorFinalizar);
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);

        presenter.getlistAllTareos();
        presenter.setListEmpleadoPorFinalizar(listEmpleadoPorFinalizar);
        presenter.setListaFechaTareos(listaFechaTareos);

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

        tabCantidad = new ReubicarTipoEmpleadoCantidadFragment().newInstance(listEmpleadoPorFinalizar);
        tabFinalizar = new ReubicarTipoEmpleadoFinalizarFragment().newInstance(listaFechaTareos);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabCantidad);
        fragments.add(tabFinalizar);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        toolbar.setTitle(getResources().getString(R.string.finalizar_empleados_x,
                listEmpleadoPorFinalizar.size()));

        presenter.attachView(this);
        ProgressDialog.show(this, "Proceso", "Creando Tareo...", true);
        verifyTareoAndEmpleadosEnd();//momentaneo
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_reubicar_tipo_empleado;
    }

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
    public void moveToTareoReubicarListActivity(Boolean correctly) {
        if (correctly) {
            Log.e(TAG, "listEmpleadoPorFinalizar: " + presenter.listEmpleadoPorFinalizar());
            startActivity(TareoReubicarListActivity.newInstance(ReubicarTipoEmpleadoActivity.this,
                    presenter.listEmpleadoPorFinalizar(), horaInicio));
            finish();
        } else {
            showErrorMessage("Hubo un error al actualizar", "");
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
                    verifyTareoAndEmpleadosEnd();
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
                    mostrarMensajeAvoidExit();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void mostrarMensajeAvoidExit() {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Ah realizado modificaciones\n" +
                        "¿Desea salir sin guardar los cambios?")
                .setIcon(R.drawable.ic_help_outline)
                .setPositiveButtonLabel("Salir")
                .setNegativeButtonLabel("Continuar")
                .setNegativeButtonlistener(() -> {
                    if (tabCantidad.hasCantidad() && tabFinalizar.hasFinalizar()) {
                        verifyTareoAndEmpleadosEnd();
                    } else {
                        if (!tabCantidad.hasCantidad()) {
                            showWarningMessage(getString(R.string.cantidad_vacia));
                        }
                        if (!tabFinalizar.hasFinalizar()) {
                            showWarningMessage(getString(R.string.datos_vacios));
                        }
                    }
                })
                .setPositiveButtonlistener(() -> {
                    onBackPressed();
                });
        dialog.build().show();
    }

    private void mostrarMensajeConfirmacionFinalizarEmpleados(String title, String mensaje) {
        CustomDialog.Builder dialog = new CustomDialog.Builder(this)
                .setTitle(title)
                .setMessage(mensaje)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_outline)
                .setNegativeButtonLabel("NO")
                .setPositiveButtonLabel("SI")
                .setNegativeButtonlistener(() -> {
                    onBackPressed();
                })
                .setPositiveButtonlistener(() -> {
                    presenter.validarDatosParaFinalizar(tabFinalizar.hasRefrigerio(),
                            tabCantidad.listAllTareos(), tabCantidad.listAllEmpleados());
                });
        dialog.build().show();
    }

    public void verifyTareoAndEmpleadosEnd() {
        HashMap<String, List<AllEmpleadoRow>> porTareo = porTareo();
        List<String> listTareoPorFinalizar = new ArrayList<>();
        for (String codTareo : porTareo.keySet()) {
            int total = porTareo.get(codTareo).size();
            Log.e(TAG, "verifyTareoAndEmpleadosEnd total: " + total);
            for (TareoRow tareo : presenter.getListTareo()) {
                Log.e(TAG, "verifyTareoAndEmpleadosEnd tareo.getCantTrabajadores(): " + tareo.getCantTrabajadores());
                if (codTareo.equals(tareo.getCodigo())
                        && total == tareo.getCantTrabajadores()) {
                    if (!listTareoPorFinalizar.contains(codTareo)) {
                        listTareoPorFinalizar.add(codTareo);
                        Log.e(TAG, "verifyTareoAndEmpleadosEnd listTareoPorFinalizar: " + listTareoPorFinalizar);
                    }
                }
            }
        }

        Log.e(TAG, "verifyTareoAndEmpleadosEnd porTareo: " + porTareo);
        Log.e(TAG, "verifyTareoAndEmpleadosEnd listCodResulXTareo: " + listTareoPorFinalizar);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.validarDatosParaFinalizar(tabFinalizar.hasRefrigerio(),
                        tabCantidad.listAllTareos(), tabCantidad.listAllEmpleados());
            }
        }, 2000);


    }

    private HashMap<String, List<AllEmpleadoRow>> porTareo() {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoNewRow : presenter.listEmpleadoPorFinalizar()) {
            String hashMapKey = allEmpleadoNewRow.getCodigoTareo();
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(allEmpleadoNewRow);
            } else {
                List<AllEmpleadoRow> list = new ArrayList<>();
                list.add(allEmpleadoNewRow);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }
}
