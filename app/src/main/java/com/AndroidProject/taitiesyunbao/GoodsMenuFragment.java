package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Display goods' information using RecyclerView and CardView.
 * Created by JCChen on 2017/5/30.
 */

public class GoodsMenuFragment extends Fragment  {

    // Each Interface.
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private LikeFragment.OnLikeListener likeListener;
    private OnGoodListListener goodListListener;

    // Using RecyclerView as a container to replace ListView.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // Store foods information.
    public Vector<ItemInfo> goodList;

    private String Kind;

    private TabLayout tabLayout;
    private float initialY, tabHeight;

    private SwipeRefreshLayout swipeRefreshLayout;

    public interface OnGoodListListener {
        void setGoodList();
        Vector<ItemInfo> getGoodList();
        int isGoodExist(ItemInfo itemInfo);
    }

    // Initialize Interface so that I can communicate with MainActivity.
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
        goodListListener = (OnGoodListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_good_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.good_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Kind = getArguments().getString("Kind");
        swipeRefreshLayout.setProgressViewOffset(false, (int)tabHeight, (int)tabHeight + 100);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        initListener();
        displayGoods();

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

    private void displayGoods() {
        goodList = new Vector<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Goods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("Kind").getValue().toString().equals(Kind)) {
                        ItemInfo itemInfo = new ItemInfo(
                                ds.child("ID").getValue().toString(),
                                ds.child("Kind").getValue().toString(),
                                ds.child("ImgurID").getValue().toString(),
                                ds.child("Name").getValue().toString(),
                                ds.child("Price").getValue().toString(),
                                ds.child("Amount").getValue().toString(),
                                ds.child("Info").getValue().toString(),
                                likeListener.isLikeItemExist(
                                        new ItemInfo(ds.child("ID").getValue().toString())) != -1);
                        goodList.add(itemInfo);
                    }
                }
                ArrayList<ItemInfo> goods = new ArrayList<>();
                for(int i = 0; i < goodList.size(); i++) {
                    if(goodList.get(i).getKind().equals(Kind)) {
                        goods.add(goodList.get(i));
                    }
                }
                // Set adapter to display content.
                adapter = new MenuRecyclerViewAdapter(getActivity(), goods, likeListener,
                        buyItemListListener, tabHeight);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        this.initialY = tabLayout.getY();
        this.tabLayout.measure(0, 0);
        this.tabHeight = tabLayout.getMeasuredHeight();
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    if(recyclerView.getScrollState() == 2)
                        tabLayout.setY(initialY - tabHeight);
                    else if (tabLayout.getY() - dy >= initialY - tabHeight)
                        tabLayout.setY(tabLayout.getY() - dy);
                }
                else if(dy < 0) {
                    if(recyclerView.getScrollState() == 2)
                        tabLayout.setY(initialY);
                    else if (tabLayout.getY() - dy <= initialY)
                        tabLayout.setY(tabLayout.getY() - dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1))
                    tabLayout.setY(initialY);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setAdapter(null);
                displayGoods();
                goodListListener.setGoodList();
            }
        });
    }
}
