package com.example.jack_pc.trainordersystem;

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
 * Created by Jack-PC on 2017/4/8.
 */

public class DrinkMenuFragment extends Fragment {

    private LikeFragment.onLikeListener likeListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    static public ArrayList<CardStruct> list = new ArrayList<>();



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        likeListener = (LikeFragment.onLikeListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_sort_drink, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.drink_recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getGoodsList();
        adapter = new MenuRecyclerViewAdapter(getActivity(), list, likeListener);
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
        // Now using static goods list temporarily (No connection)
        if(list.size() == 5) {
            return;
        }
        list.add(new CardStruct(R.drawable.drinks_1, R.string.drink1, 20, R.string.drink1_info, 10));
        list.add(new CardStruct(R.drawable.drinks_2, R.string.drink2, 25, R.string.drink2_info, 0));
        list.add(new CardStruct(R.drawable.drinks_3, R.string.drink3, 25, R.string.drink3_info, 3));
        list.add(new CardStruct(R.drawable.drinks_4, R.string.drink4, 20, R.string.drink4_info, 1));
        list.add(new CardStruct(R.drawable.drinks_5, R.string.drink5, 20, R.string.drink5_info, 1));

    }
}
