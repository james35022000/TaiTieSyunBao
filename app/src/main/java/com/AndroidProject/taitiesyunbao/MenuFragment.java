package com.AndroidProject.taitiesyunbao;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Menu Page Fragment.
 * It's a container to display TopTab and Pager.
 *
 * Layout File: menu_frg_layout.
 *
 * Created by JCChen on 2017/4/7.
 */

public class MenuFragment extends Fragment {

    private LikeFragment.OnLikeListener likeListener;
    // Store the name (string ID) of tab at the top.
    private int menuTab[] = {R.string.food_tab, R.string.drink_tab,
                                R.string.snack_tab, R.string.other_tab};

    // Store each kind of goods' fragment in order to MenuPagerAdapter to initialize pager.
    private List<Fragment> kindList = new ArrayList<>();

    // Layout initial.
    private FloatingActionButton floatingActionButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Interface to communicate with MainActivity, and also, it can use in other Fragment.
     *
     * Features: Using to access buyList in the MainActivity.
     */
    public interface OnBuyItemListListener {
        Vector<ItemInfo> getBuyList();
        void setBuyList(Vector<ItemInfo> buyList);
        void addBuyList(ItemInfo ItemInfo);
        void delBuyList(ItemInfo ItemInfo);
        int isBuyItemExist(ItemInfo ItemInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_frg_layout, container, false);

        // Initialize layout items.
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.menu_floatingActionButton);
        likeListener = (LikeFragment.OnLikeListener) getActivity();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.pager_menu);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize TopTab.
        initTab();
        // Initialize content.
        initPage();
        // Initialize OnTabSelectedListener and FloatingActionButton OnClickListener.
        initListener();

        floatingActionButton.getBackground().setAlpha(150);
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
        kindList.add(new FoodMenuFragment());
        kindList.add(new DrinkMenuFragment());
        kindList.add(new SnackMenuFragment());
        kindList.add(new OtherMenuFragment());

        viewPager.setAdapter(new MenuPagerAdapter(getChildFragmentManager(), kindList));

    }

    private void initListener() {

        // OnTabSelectedListener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Set viewPager(content) when selecting a tab at the top.
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

        // FloatingActionButton OnClickListener
        // When clicking this button, show BuyInfoFragment and also set FloatingActionButton
        // visibility as GONE.
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuyInfoFragment buyInfoFragment = new BuyInfoFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.menu_fragment, buyInfoFragment)
                        .commit();

                floatingActionButton.setVisibility(View.GONE);
            }
        });

    }
}