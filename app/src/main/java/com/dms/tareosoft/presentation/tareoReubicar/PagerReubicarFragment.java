package com.dms.tareosoft.presentation.tareoReubicar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porEmpleado.ListReubicarPorEmpleadoFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porTareo.ListReubicarPorTareoFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PagerReubicarFragment extends Fragment {

    String TAG = PagerReubicarFragment.class.getSimpleName();

    private FragmentAdapter mFragmentAdapter;

    ListReubicarPorEmpleadoFragment tabEmpleado;
    ListReubicarPorTareoFragment tabTareo;

    public PagerReubicarFragment() {
    }

    public static PagerReubicarFragment newInstance() {
        PagerReubicarFragment fragment = new PagerReubicarFragment();
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabEmpleado = new ListReubicarPorEmpleadoFragment();
        tabTareo = ListReubicarPorTareoFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Reubicar");

        View view = inflater.inflate(R.layout.fragment_reubicar_pager, container, false);

        TabLayout mTabLayout = view.findViewById(R.id.tab_layout_main);
        ViewPager mViewPager = view.findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_reubicar_x_empleado));
        titles.add(getString(R.string.tab_reubicar_x_tareo));

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabEmpleado);
        fragments.add(tabTareo);

        mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        return view;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.e(TAG, "position: " + position + ", positionOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "position: " + position);
            switch (position) {
                case 0:
                    break;
                case 1:
                    tabTareo.onResume();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.e(TAG, "state: " + state);
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
