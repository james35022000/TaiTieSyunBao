package com.example.jack_pc.trainordersystem;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
    TabHost.OnTabChangeListener, LikeFragment.onLikeListener {

    public Vector<CardStruct> likeList = new Vector<>();
    private int tabImage[] = {R.drawable.main_tab_selector, R.drawable.menu_tab_selector,
                                R.drawable.like_tab_selector, R.drawable.sug_tab_selector};
    private int tabText[] = {R.string.main_tab_name, R.string.menu_tab_name,
                                R.string.like_tab_name, R.string.sub_tab_name};
    /* If adding new fragment, you also need to add the fragment in "initPage()" function. */
    private Class contentFragment[] = {MainFragment.class, MenuFragment.class,
                                        LikeFragment.class, SugFragment.class};

    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTabHost mTabHost;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initUI();
        initPage();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {    }

    /**
     *
     * @param arg0
     *
     * Tab change with viewPager.
     */
    @Override
    public void onPageSelected(int arg0) {
        mTabHost.setCurrentTab(arg0);
        setTitle(getResources().getString(tabText[arg0]));
    }

    /**
     *
     * @param tabId
     *
     * ViewPager change with tab.
     */
    @Override
    public void onTabChanged(String tabId) {
        int index = mTabHost.getCurrentTab();
        viewPager.setCurrentItem(index);
        setTitle(getResources().getString(tabText[index]));
    }

    /**
     *
     * Initialize toolbar.
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle(getResources().getString(tabText[0]));
        setSupportActionBar(toolbar);
    }

    /**
     *
     * Initialize tab at bottom.
     */
    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);

        for(int i = 0; i < tabImage.length; i++) {
            String tabName = getResources().getString(tabText[i]);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabName).setIndicator(getImageView(i));
            mTabHost.addTab(tabSpec, contentFragment[i], null);
        }
    }


    /**
     *
     * Initialize contents of each tab.
     */
    private void initPage() {
        fragmentList.add(new MainFragment());
        fragmentList.add(new MenuFragment());
        fragmentList.add(new LikeFragment());
        fragmentList.add(new SugFragment());

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList));
    }



    /**
     *
     * @param index
     * @return View with image and text of the tab
     *
     * Using LayoutInflater to transfer the layout from XML(tab_content.xml) to View.
     */
    private View getImageView(int index) {
        View view = getLayoutInflater().inflate(R.layout.tab_content, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_imageView);
        TextView  textView  = (TextView) view.findViewById(R.id.tab_textView);
        imageView.setImageDrawable(getResources().getDrawable(tabImage[index]));
        textView.setText(tabText[index]);
        return view;
    }

    /**
     * Declare in LikeFragment.
     *
     * @return
     *
     * Return likeList to other fragment.
     */
    @Override
    public Vector<CardStruct> getLikeList() {
        if(likeList == null)  likeList = new Vector<>();
        return likeList;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param likeList
     *
     * Set likeList.
     */
    @Override
    public void setLikeList(Vector<CardStruct> likeList) {
        this.likeList = likeList;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param cardStruct
     *
     * Add item to likeList.
     */
    @Override
    public void addLikeList(CardStruct cardStruct) {
        if(likeList == null)  likeList = new Vector<>();
        if(isExist(cardStruct) == -1) {
            likeList.add(cardStruct);
        }
    }

    /**
     *
     * @param cardStruct
     * @return true if remove item successfully. false if item not found.
     */
    @Override
    public boolean delLikeList(CardStruct cardStruct) {
        int index = isExist(cardStruct);
        if(index >= 0) {
            likeList.remove(index);
            return true;
        }
        return false;
    }

    /**
     *
     * @param cardStruct
     * @return the index of the exist item. If the item is not exist, return -1.
     */
    @Override
    public int isExist(CardStruct cardStruct) {
        for(int i = 0; i < likeList.size(); i++) {
            if(likeList.get(i).getImageID(this) == cardStruct.getImageID(this)) {
                return i;
            }
        }
        return -1;
    }

}
