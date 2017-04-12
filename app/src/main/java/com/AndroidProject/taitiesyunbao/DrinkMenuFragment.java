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
 * Display drinks' information using RecyclerView and CardView.
 * Created by JCChen on 2017/4/8.
 */

public class DrinkMenuFragment extends Fragment {

    // MainActivity Context.
    private Context context;

    private LikeFragment.OnLikeListener likeListener;
    private MenuFragment.OnBuyItemListListener buyItemListListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ArrayList<ItemInfo> drinkList = new ArrayList<>();



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_sort_drink, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drink_recyclerView);
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
        adapter = new MenuRecyclerViewAdapter(getActivity(), drinkList, likeListener, buyItemListListener);
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
        // Now using static goods list temporarily.
        if(drinkList.size() == 5) {
            return;
        }
        drinkList.add(new ItemInfo(context, R.drawable.drinks_1, R.string.drink1, 20, R.string.drink1_info, 10));
        drinkList.add(new ItemInfo(context, R.drawable.drinks_2, R.string.drink2, 25, R.string.drink2_info, 0));
        drinkList.add(new ItemInfo(context, R.drawable.drinks_3, R.string.drink3, 25, R.string.drink3_info, 3));
        drinkList.add(new ItemInfo(context, R.drawable.drinks_4, R.string.drink4, 20, R.string.drink4_info, 1));
        drinkList.add(new ItemInfo(context, R.drawable.drinks_5, R.string.drink5, 20, R.string.drink5_info, 1));

    }
}