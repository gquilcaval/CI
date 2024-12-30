package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_empleados.TabEmpleadoFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_tareo.TabTareoFragment;
import com.dms.tareosoft.util.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FinalizarEmpleadoActivity extends BaseActivity
        implements IFinalizarEmpleadoContract.View{

    private static String CODIGO_TAREO = "codTareo";
    private static String TAREO = "codTareo";

    private TabEmpleadoFragment tabEmpleado;
    private TabTareoFragment tabFinalizar;
    private ViewPager mViewPager;

    @Inject
    FinalizarEmpleadoPresenter presenter;

    private TareoRow tareoRow;

    public static Intent newInstance(Context context, String codTareo, String concepto1,
                                     String concepto2, String fechaInicio) {
        Intent intent = new Intent(context, FinalizarEmpleadoActivity.class);
        intent.putExtra(CODIGO_TAREO, codTareo);
        intent.putExtra(Constants.EXT_CODCONCEPTO1, concepto1);
        intent.putExtra(Constants.EXT_CODCONCEPTO2, concepto2);
        intent.putExtra(Constants.EXT_FECHAINICIO, fechaInicio);
        return intent;
    }

    public static Intent newInstance(Context context, TareoRow tareo) {
        Intent intent = new Intent(context, FinalizarEmpleadoActivity.class);
        intent.putExtra(TAREO, tareo);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);
        toolbar.setTitle("Finalizar Tareo");

        if (getIntent().hasExtra(TAREO)) {
            tareoRow = (TareoRow) getIntent().getSerializableExtra(TAREO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + CODIGO_TAREO);
        }

        TabLayout mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add("Tareo");
        titles.add("Empleados");

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        tabEmpleado = new TabEmpleadoFragment(tareoRow);
        tabFinalizar = new TabTareoFragment(tareoRow);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabFinalizar);
        fragments.add(tabEmpleado);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        presenter.attachView(this);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void salir() {
        this.finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_finalizar_empleado;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
