package com.AndroidProject.taitiesyunbao;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

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

    // Store the name (string ID) of tab at the top.
    private int menuTab[] = {R.string.food_tab, R.string.drink_tab,
                                R.string.snack_tab, R.string.other_tab};

    // Store each kind of goods' fragment in order to MenuPagerAdapter to initialize pager.
    private List<Fragment> kindList;

    // Layout initial.
    private FloatingActionButton floatingActionButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private float initialY;
    private boolean purchaseInfoFragmentExist = false;

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
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.pager_menu);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(purchaseInfoFragmentExist) {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.menu_fragment))
                    .commit();
            floatingActionButton.setVisibility(View.VISIBLE);
            purchaseInfoFragmentExist = false;
        }

        // Initialize TopTab.
        initTab();
        // Initialize content.
        initPage();
        // Initialize OnTabSelectedListener and FloatingActionButton OnClickListener.
        initListener();

        //floatingActionButton.getBackground().setAlpha(150);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initTab() {
        for (int i = 0; i < menuTab.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(menuTab[i])));
        }
        tabLayout.bringToFront();
        initialY = tabLayout.getY();
    }



    private void initPage() {
        kindList = new ArrayList<>();
        Bundle arg = new Bundle();
        GoodsMenuFragment goodsMenuFragment = new GoodsMenuFragment();
        arg.putString("Kind", "FOOD");
        goodsMenuFragment.setArguments(arg);
        goodsMenuFragment.setTabLayout(tabLayout);
        kindList.add(goodsMenuFragment);

        arg = new Bundle();
        goodsMenuFragment = new GoodsMenuFragment();
        arg.putString("Kind", "DRINK");
        goodsMenuFragment.setArguments(arg);
        goodsMenuFragment.setTabLayout(tabLayout);
        kindList.add(goodsMenuFragment);

        arg = new Bundle();
        goodsMenuFragment = new GoodsMenuFragment();
        arg.putString("Kind", "SNACK");
        goodsMenuFragment.setArguments(arg);
        goodsMenuFragment.setTabLayout(tabLayout);
        kindList.add(goodsMenuFragment);

        arg = new Bundle();
        goodsMenuFragment = new GoodsMenuFragment();
        arg.putString("Kind", "OTHER");
        goodsMenuFragment.setArguments(arg);
        goodsMenuFragment.setTabLayout(tabLayout);
        kindList.add(goodsMenuFragment);

        viewPager.setAdapter(new MenuPagerAdapter(getChildFragmentManager(), kindList));
    }

    private void initListener() {

        // OnTabSelectedListener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Set viewPager(content) when selecting a tab at the top.
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Animation animation = new TranslateAnimation(
                                        Animation.ABSOLUTE, 0f,
                                        Animation.ABSOLUTE, 0f,
                                        Animation.ABSOLUTE, -(initialY - tabLayout.getY()),
                                        Animation.ABSOLUTE, 0f
                                        );
                AnimationSet animationSet = new AnimationSet(true);
                animation.setDuration(500);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        tabLayout.setY(initialY);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animationSet.addAnimation(animation);
                tabLayout.startAnimation(animationSet);
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
                purchaseInfoFragmentExist = true;
                PurchaseInfoFragment purchaseInfoFragment = new PurchaseInfoFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.menu_fragment, purchaseInfoFragment)
                        .commit();
                floatingActionButton.setVisibility(View.GONE);
            }
        });

    }
}