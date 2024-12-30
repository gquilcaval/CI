package com.dms.tareosoft.presentation.asistencia.registro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.asistencia.registro.definicion.AsistenciaDefinicionFragment;
import com.dms.tareosoft.presentation.asistencia.registro.empleado.AsistenciaEmpleadoFragment;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.INuevoTareoContract;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.NuevoTareoPresenter;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.definicion.DefinicionFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado.EmpleadoFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.opciones.OpcionesFragment;
import com.dms.tareosoft.presentation.tareo_principal.finalizados.FinalizadosFragment;
import com.dms.tareosoft.presentation.tareo_principal.iniciados.IniciadosFragment;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.CustomToast;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RegistroAsistenciaActivity extends Fragment  {

    String TAG = RegistroAsistenciaActivity.class.getSimpleName();

    private AsistenciaDefinicionFragment tabDefinicion;
    private AsistenciaEmpleadoFragment tabEmpleados;
    private ViewPager mViewPager;
    public static Marcacion m =  new Marcacion();

    @Inject
    RegistroAsistenciaPresenter presenter;

    public RegistroAsistenciaActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Registro Asistencia");

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        TabLayout mTabLayout = view.findViewById(R.id.tab_layout_main);
        mViewPager = view.findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add("Definición");
        titles.add("Empleados");

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AsistenciaDefinicionFragment());
        fragments.add(new AsistenciaEmpleadoFragment());

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        // Inflate the layout for this fragment
        return view;
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, RegistroAsistenciaActivity.class);
        return intent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        m = null;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            try {
                Log.d(TAG, "MARC -> " + m.getTarea());
                if (position == 1) {
                    if (m.getTarea() != null) {
                        Log.d(TAG, "MARC -> " + "a");
                        // La variable en la pestaña "Definición" está llena, permitir cambiar a la pestaña "Empleados"
                        mViewPager.setCurrentItem(1);
                    } else {
                        Log.d(TAG, "MARC -> " + "b");
                        // La variable en la pestaña "Definición" no está llena, retroceder a la pestaña "Definición"
                        mViewPager.setCurrentItem(0);
                        new CustomToast.Builder(getActivity().getApplicationContext(), getLayoutInflater(), "Debe seleccionar una agencia").build().show();

                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "MARC -> " + "c");
                mViewPager.setCurrentItem(0);
                new CustomToast.Builder(getActivity().getApplicationContext(), getLayoutInflater(), "Debe seleccionar una agencia").build().show();
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}