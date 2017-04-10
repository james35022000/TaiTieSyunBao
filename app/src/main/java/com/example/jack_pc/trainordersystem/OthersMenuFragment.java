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

public class OthersMenuFragment extends Fragment {
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
        return inflater.inflate(R.layout.menu_sort_others, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.others_recyclerView);
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
        if(list.size() == 4) {
            return;
        }
        list.add(new CardStruct(R.drawable.toy_1, R.string.others1, 320, R.string.other1_info, 1));
        list.add(new CardStruct(R.drawable.toy_2, R.string.others2, 320, R.string.other2_info, 2));
        list.add(new CardStruct(R.drawable.toy_3, R.string.others3, 320, R.string.other3_info, 4));
        list.add(new CardStruct(R.drawable.toy_4, R.string.others4, 120, R.string.other4_info, 5));

    }
}
