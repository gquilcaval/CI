package com.dms.tareosoft.presentation.tareo_control.nuevo;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.tareo_control.nuevo.tab_empleado.ControlTareoFragment;
import com.dms.tareosoft.presentation.tareo_control.nuevo.tab_tareo.EmpleadoControlFragment;
import com.dms.tareosoft.util.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ControlEmpleadoActivity extends BaseActivity {
    private static String CODIGO_TAREO = "codTareo";
    private String codigoTareo;

    private EmpleadoControlFragment tabEmpleado;
    private ControlTareoFragment tabNuevoControl;
    private ViewPager mViewPager;
    private String concepto1;
    private String concepto2;
    private String fechaInicio;


    public static Intent newInstance(Context context, String codTareo,
                                     String concepto1, String concepto2,
                                     String fechaInicio) {
        Intent intent = new Intent(context, ControlEmpleadoActivity.class);
        intent.putExtra(CODIGO_TAREO, codTareo);
        intent.putExtra(Constants.EXT_CODCONCEPTO1, concepto1);
        intent.putExtra(Constants.EXT_CODCONCEPTO2, concepto2);
        intent.putExtra(Constants.EXT_FECHAINICIO, fechaInicio);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //consultarTareo
        if (getIntent().hasExtra(CODIGO_TAREO)) {
            codigoTareo = getIntent().getStringExtra(CODIGO_TAREO);
            concepto1 = getIntent().getStringExtra(Constants.EXT_CODCONCEPTO1);
            concepto2 = getIntent().getStringExtra(Constants.EXT_CODCONCEPTO2);
            fechaInicio = getIntent().getStringExtra(Constants.EXT_FECHAINICIO);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + CODIGO_TAREO);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);
        toolbar.setTitle("Control Tareo");

        TabLayout mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add("Empleado");
        titles.add("Tareo");

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        tabEmpleado = new EmpleadoControlFragment(codigoTareo, concepto1, concepto2, fechaInicio);
        tabNuevoControl = new ControlTareoFragment(codigoTareo);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabNuevoControl);
        fragments.add(tabEmpleado);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
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

    @Override
    protected int getLayout() {
        return R.layout.activity_control_empleado;
    }

}
