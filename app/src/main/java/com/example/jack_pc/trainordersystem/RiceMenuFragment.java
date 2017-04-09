package com.example.jack_pc.trainordersystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jack-PC on 2017/4/8.
 */

public class RiceMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<CardStruct> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_sort_rice, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rice_recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getGoodsList();
        adapter = new MenuRecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        list.add(new CardStruct(getActivity(), R.drawable.food1, R.string.rice1, 80, R.string.rice1_info, 10));
        list.add(new CardStruct(getActivity(), R.drawable.food2, R.string.rice2, 80, R.string.rice2_info, 0));
        list.add(new CardStruct(getActivity(), R.drawable.food3, R.string.rice3, 100, R.string.rice3_info, 3));
        list.add(new CardStruct(getActivity(), R.drawable.food4, R.string.rice4, 150, R.string.rice4_info, 1));

    }

}
