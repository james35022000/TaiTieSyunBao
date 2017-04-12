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
 * Display others' information using RecyclerView and CardView.
 * Created by JCChen on 2017/4/8.
 */

public class OtherMenuFragment extends Fragment {

    // MainActivity Context
    private Context context;

    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private LikeFragment.OnLikeListener likeListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ArrayList<ItemInfo> otherList = new ArrayList<>();



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_sort_other, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.other_recyclerView);
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

        adapter = new MenuRecyclerViewAdapter(getActivity(), otherList, likeListener, buyItemListListener);
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
        if(otherList.size() == 4) {
            return;
        }
        otherList.add(new ItemInfo(context, R.drawable.toy_1, R.string.other1, 320, R.string.other1_info, 1));
        otherList.add(new ItemInfo(context, R.drawable.toy_2, R.string.other2, 320, R.string.other2_info, 2));
        otherList.add(new ItemInfo(context, R.drawable.toy_3, R.string.other3, 320, R.string.other3_info, 4));
        otherList.add(new ItemInfo(context, R.drawable.toy_4, R.string.other4, 120, R.string.other4_info, 5));

    }
}
