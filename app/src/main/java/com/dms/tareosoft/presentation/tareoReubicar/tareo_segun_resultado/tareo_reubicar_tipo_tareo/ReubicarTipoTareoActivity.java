package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos.TareoReubicarListActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad.ReubicarTipoTareoCantidadFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_finalizar.ReubicarTipoTareoFinalizarFragment;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class ReubicarTipoTareoActivity extends BaseActivity
        implements IReubicarTipoTareoContract.View {

    static String TAG = ReubicarTipoTareoActivity.class.getSimpleName();
    public String horaInicio = ""; //add
    public static String LISTA_ALL_EMPLEADOS = "allEmpleados";
    public static String LISTA_FECHA_TAREOS = "listaFechaTareos";
    public static String LISTA_TAREOS_END = "listaFechaTareosEnd";

    @Inject
    ReubicarTipoTareoPresenter presenter;

    private ViewPager mViewPager;
    private ReubicarTipoTareoCantidadFragment tabCantidad;
    private ReubicarTipoTareoFinalizarFragment tabFinalizar;

    private List<String> listaFechaTareos = new ArrayList<>();
    private List<String> listaTareosEnd = new ArrayList<>();
    final Handler handler = new Handler();

    public static Intent newInstance(Context context,
                                     List<AllEmpleadoRow> listaAllEmpleados,
                                     List<String> listaAllTareosEnd,
                                     List<String> listaFechaTareos) {
        Intent intent = new Intent(context, ReubicarTipoTareoActivity.class);
        intent.putExtra(LISTA_ALL_EMPLEADOS, (Serializable) listaAllEmpleados);
        intent.putExtra(LISTA_FECHA_TAREOS, (Serializable) listaFechaTareos);
        intent.putExtra(LISTA_TAREOS_END, (Serializable) listaAllTareosEnd);
        Log.e(TAG, "listaAllEmpleados: " + listaAllEmpleados);
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);
        Log.e(TAG, "listaAllTareosEnd: " + listaAllTareosEnd);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        List<AllEmpleadoRow> mListEmpleadoPorFinalizar = new ArrayList<>();

        if (getIntent() != null) {
            mListEmpleadoPorFinalizar = (List<AllEmpleadoRow>)
                    getIntent().getSerializableExtra(LISTA_ALL_EMPLEADOS);
            listaFechaTareos = (List<String>)
                    getIntent().getSerializableExtra(LISTA_FECHA_TAREOS);
            listaTareosEnd = (List<String>)
                    getIntent().getSerializableExtra(LISTA_TAREOS_END);
        }

        Log.e(TAG, "listaAllEmpleados: " + mListEmpleadoPorFinalizar);
        Log.e(TAG, "listaFechaTareos: " + listaFechaTareos);
        Log.e(TAG, "listaTareosEnd: " + listaTareosEnd);

        presenter.getListAllTareos();
        presenter.setListEmpleadoPorFinalizar(mListEmpleadoPorFinalizar);
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

        tabCantidad = new ReubicarTipoTareoCantidadFragment().newInstance(mListEmpleadoPorFinalizar,
                listaTareosEnd);
        tabFinalizar = new ReubicarTipoTareoFinalizarFragment().newInstance(listaFechaTareos);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabCantidad);
        fragments.add(tabFinalizar);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        toolbar.setTitle(getResources().getString(R.string.finalizar_empleados_x,
                mListEmpleadoPorFinalizar.size()));

        presenter.attachView(this);

        ProgressDialog.show(this, "Proceso", "Creando Tareo...", true);
        verifyTareoAndEmpleadosEnd();//momentaneo
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_reubicar_tipo_tareo;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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
            Log.e(TAG, "listaAllEmpleados: " + presenter.getListEmpleadoPorFinalizar());
            startActivity(TareoReubicarListActivity.newInstance(ReubicarTipoTareoActivity.this,
                    presenter.getListEmpleadoPorFinalizar(), horaInicio)); // hora inicio momentaneo
            finish();
        } else {
            showErrorMessage("Hubo un error al actualizar", "");
        }
    }

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

    private void verifyTareoAndEmpleadosEnd() {
        HashMap<String, List<AllEmpleadoRow>> porTareo = porTareo();
        List<String> listCodResulXTareo = new ArrayList<>();
        for (String codTareo : porTareo.keySet()) {
            for (TareoRow tareo : presenter.getListAllTareo()) {
                int total = porTareo.get(codTareo).size();
                Log.e(TAG, "total: " + total);
                Log.e(TAG, "tareo.getCantTrabajadores(): " + total);
                if (total == tareo.getCantTrabajadores()) {
                    listCodResulXTareo.add(codTareo);
                    Log.e(TAG, "tareo.getCantTrabajadores(): " + tareo.getCantTrabajadores());
                }
            }
        }

        handler.postDelayed(() ->
                presenter.validarDatosParaFinalizar(tabFinalizar.hasRefrigerio(),
                tabCantidad.listAllTareos(), tabCantidad.listAllEmpleados()), 2000);
    }

    private HashMap<String, List<AllEmpleadoRow>> porTareo() {
        HashMap<String, List<AllEmpleadoRow>> groupedHashMap = new HashMap<>();
        for (AllEmpleadoRow allEmpleadoNewRow : presenter.getListEmpleadoPorFinalizar()) {
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
