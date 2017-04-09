package com.example.jack_pc.trainordersystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack-PC on 2017/4/7.
 */

public class MenuFragment extends Fragment {

    private TabLayout tabLayout;
    private MenuViewPager viewPager;
    private int menuTab[] = {R.string.rice_tab, R.string.drink_tab,
                                R.string.snacks_tab, R.string.others_tab};
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_frg_layout, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (MenuViewPager) view.findViewById(R.id.pager_menu);
        initTab();
        initPage();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initTab() {
        for(int i = 0; i < menuTab.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(menuTab[i])));
        }
    }

    private void initPage() {
        list.add(new RiceMenuFragment());
        list.add(new DrinkMenuFragment());
        list.add(new SnacksMenuFragment());
        list.add(new OthersMenuFragment());

        viewPager.setAdapter(new MenuPagerAdapter(getChildFragmentManager(), list));
        initListener();
    }

    private void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}