package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Jack-PC on 2017/4/7.
 */

public class MenuFragment extends Fragment {

    private LikeFragment.onLikeListener likeListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int menuTab[] = {R.string.rice_tab, R.string.drink_tab,
                                R.string.snacks_tab, R.string.others_tab};
    private List<Fragment> list = new ArrayList<>();
    private FloatingActionButton floatingActionButton;

    public interface OnBuyItemListListener {
        Vector<CardStruct> getList();
        void addList(CardStruct cardStruct);
        void delList(CardStruct cardStruct);
        int isItemExist(CardStruct cardStruct);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_frg_layout, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.menu_floatingActionButton);
        likeListener = (LikeFragment.onLikeListener) getActivity();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.pager_menu);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTab();
        initPage();
        initListener();
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

    }

    private void initListener() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {  }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {  }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyInfoFragment buyInfoFragment = new BuyInfoFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.menu_fragment, buyInfoFragment).commit();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                //        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
                //fragmentTransaction.hide(getFragmentManager().findFragmentById(R.id.menu_fragment))
                //        .add(R.id.buy_info_fragment, buyInfoFragment).commit();
            }
        });

    }


}