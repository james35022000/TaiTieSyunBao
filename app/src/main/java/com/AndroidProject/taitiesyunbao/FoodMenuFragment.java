package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Display foods' information using RecyclerView and CardView.
 * Created by JCChen on 2017/4/8.
 */

public class FoodMenuFragment extends Fragment  {

    // MainActivity context.
    private Context context;

    // Each Interface.
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private LikeFragment.OnLikeListener likeListener;

    // Using RecyclerView as a container to replace ListView.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // Store foods information.
    public ArrayList<ItemInfo> foodList = new ArrayList<>();

    // Initialize Interface so that I can communicate with MainActivity.
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_sort_food, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.food_recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getGoodsList();

        // Set adapter to display content.
        adapter = new MenuRecyclerViewAdapter(getActivity(), foodList, likeListener, buyItemListListener);
        recyclerView.setAdapter(adapter);
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

    private void getGoodsList() {
        // Now using static goods list temporarily
        if(foodList.size() == 4) {
            return;
        }
        foodList.add(new ItemInfo(context, R.drawable.food_1, R.string.food1, 80, R.string.food1_info, 10));
        foodList.add(new ItemInfo(context, R.drawable.food_2, R.string.food2, 80, R.string.food2_info, 0));
        foodList.add(new ItemInfo(context, R.drawable.food_3, R.string.food3, 100, R.string.food3_info, 3));
        foodList.add(new ItemInfo(context, R.drawable.food_4, R.string.food4, 150, R.string.food4_info, 1));
    }
}
