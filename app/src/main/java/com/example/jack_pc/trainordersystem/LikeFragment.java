package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

/**
 * Created by Jack-PC on 2017/4/7.
 */

public class LikeFragment extends Fragment {

    private Vector<CardStruct> likeList;
    private RecyclerView recyclerView;
    private LikeRecyclerViewAdapter adapter;

    public interface onLikeListener {

        Vector<CardStruct> getLikeList();
        void setLikeList(Vector<CardStruct> list);
        void addLikeList(CardStruct cardStruct);
        boolean delLikeList(CardStruct cardStruct);
        int isExist(CardStruct cardStruct);

    }

    onLikeListener likeListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        likeListener = (onLikeListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.like_frg_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.like_recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        likeList = likeListener.getLikeList();
        adapter = new LikeRecyclerViewAdapter(getActivity(), likeListener);
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

}
