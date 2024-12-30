package com.dms.tareosoft.presentation.tareo_principal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.adapter.FragmentAdapter;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.tareo_principal.finalizados.FinalizadosFragment;
import com.dms.tareosoft.presentation.tareo_principal.iniciados.IniciadosFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ListaTareoFragment extends Fragment {

    public ListaTareoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Tareos");

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        TabLayout mTabLayout = view.findViewById(R.id.tab_layout_main);
        ViewPager mViewPager = view.findViewById(R.id.view_pager_main);
        mViewPager.setOffscreenPageLimit(2);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_tareo_ini));
        titles.add(getString(R.string.tab_tareo_fin));

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new IniciadosFragment());
        fragments.add(new FinalizadosFragment());

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        // Inflate the layout for this fragment
        return view;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
           /* if (position == 2) {
                fab.show();
            } else {
                fab.hide();
            }*/
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
