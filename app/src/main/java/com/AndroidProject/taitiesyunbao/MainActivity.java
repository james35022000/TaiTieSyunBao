package com.AndroidProject.taitiesyunbao;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
    TabHost.OnTabChangeListener, LikeFragment.OnLikeListener, MenuFragment.OnBuyItemListListener,
    GetImage.ImageCache {

    public Vector<ItemInfo> likeList, buyList;
    public Map<String, Bitmap> Cache;

    // Store Bottom tab image ID.
    private int tabImage[] = {R.drawable.main_tab_selector, R.drawable.menu_tab_selector,
                                R.drawable.like_tab_selector, R.drawable.sug_tab_selector};
    // Store Bottom tab text ID.
    private int tabText[] = {R.string.main_tab_name, R.string.menu_tab_name,
                                R.string.like_tab_name, R.string.sug_tab_name};
    // Store toolbar image ID.
    private int toolbarImage[] = {R.drawable.main_tittle, R.drawable.menu_tittle,
                                    R.drawable.like_tittle, R.drawable.sug_tittle};
    // Each page fragment.
    // Warning: If adding new fragment, you also need to add the fragment in "initPage()" function.
    private Class contentFragment[] = {MainFragment.class, MenuFragment.class,
                                        LikeFragment.class, SugFragment.class};

    // Store all pages' fragment in order to set MainPagerAdapter to initialize pager.
    private List<Fragment> fragmentList = new ArrayList<>();

    // Layout declare.
    private FragmentTabHost mTabHost;
    private MainViewPager viewPager;
    private ImageView toolbar_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLikeList(new Vector<ItemInfo>());
        setBuyList(new Vector<ItemInfo>());

        // Initialize action bar(Use android.support.v7.widget.Toolbar as toolbar)
        initActionBar();
        // Initialize tabs.
        initUI();
        // Initialize content.
        initPage();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {    }

    /**
     * @param arg0
     *
     * Tab change with viewPager.
     */
    @Override
    public void onPageSelected(int arg0) {
        mTabHost.setCurrentTab(arg0);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[arg0]));
    }

    /**
     * @param tabId
     *
     * ViewPager change with tab.
     */
    @Override
    public void onTabChanged(String tabId) {
        int index = mTabHost.getCurrentTab();
        viewPager.setCurrentItem(index);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[index]));
    }

    /**
     * Initialize toolbar.
     */
    private void initActionBar() {
        toolbar_icon = (ImageView) findViewById(R.id.toolbar_icon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar_icon.setImageDrawable(getDrawable(toolbarImage[0]));
        setTitle("");
        setSupportActionBar(toolbar);
    }

    /**
     * Initialize tab at bottom.
     */
    private void initUI() {
        viewPager = (MainViewPager) findViewById(R.id.pager);
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
    public Vector<ItemInfo> getLikeList() {
        if(likeList == null)  likeList = new Vector<>();
        return new Vector<>(likeList);
    }

    /**
     * Declare in LikeFragment.
     *
     * @param likeList
     */
    @Override
    public void setLikeList(Vector<ItemInfo> likeList) {
        this.likeList = likeList;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     *
     * Add item to likeList.
     */
    @Override
    public void addLikeList(ItemInfo ItemInfo) {
        if(likeList == null)  likeList = new Vector<>();
        if(isLikeItemExist(ItemInfo) == -1) {
            likeList.add(ItemInfo);
        }
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     * @return true if remove item successfully. false if item not found.
     */
    @Override
    public boolean delLikeList(ItemInfo ItemInfo) {
        int index = isLikeItemExist(ItemInfo);
        if(index >= 0) {
            likeList.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Declare in LikeFragment.
     *
     * @param ItemInfo
     * @return index of existing item, -1 if item not exist.
     */
    @Override
    public int isLikeItemExist(ItemInfo ItemInfo) {
        for(int i = 0; i < likeList.size(); i++) {
            if(likeList.get(i).getId() == ItemInfo.getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Declare in MenuFragment.
     *
     * @param buyList
     */
    public void setBuyList(Vector<ItemInfo> buyList) {
        this.buyList = buyList;
    }

    /**
     * Declare in MenuFragment.
     *
     * @return buyList.
     */
    @Override
    public Vector<ItemInfo> getBuyList() {
        return new Vector<>(buyList);
    }

    /**
     * Declare in MenuFragment.
     *
     * @param ItemInfo
     *
     * Add(or delete if amount in ItemInfo equal to 0) item to buyList.
     */
    @Override
    public void addBuyList(ItemInfo ItemInfo) {
        delBuyList(ItemInfo);
        if(ItemInfo.getAmount() == 0)  return;
        buyList.add(ItemInfo);
    }

    /**
     * Declare in MenuFragment
     *
     * @param ItemInfo
     *
     * Delete certain item in buyList.
     */
    @Override
    public void delBuyList(ItemInfo ItemInfo) {
        int index = isBuyItemExist(ItemInfo);
        if(index == -1)  return;
        buyList.remove(index);
    }

    /**
     * Declare in MenuFragment
     *
     * @param ItemInfo
     * @return index of existing item, -1 if item not exist.
     */
    @Override
    public int isBuyItemExist(ItemInfo ItemInfo) {
        for(int i = 0; i < buyList.size(); i++) {
            // Using Image ID to check whether they are equal or not.
            if(buyList.get(i).getId() == ItemInfo.getId()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Bitmap GetImage(String imgurID) {
        if (Cache == null) return null;
        if (Cache.containsKey(imgurID)) return Cache.get(imgurID);
        return null;
    }

    @Override
    public void SetImage(String imgurID, Bitmap bitmap) {
        if(Cache == null) Cache = new HashMap<>();
        Cache.put(imgurID, bitmap);
    }
}
