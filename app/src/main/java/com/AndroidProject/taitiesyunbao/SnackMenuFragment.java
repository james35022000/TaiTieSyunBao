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
 * Display snacks' information using RecyclerView and CardView.
 * Created by JCChen on 2017/4/8.
 */

public class SnackMenuFragment extends Fragment {

    // MainActivity Context.
    private Context context;

    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private LikeFragment.OnLikeListener likeListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ArrayList<ItemInfo> snackList = new ArrayList<>();



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_sort_snack, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.snack_recyclerView);
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

        adapter = new MenuRecyclerViewAdapter(getActivity(), snackList, likeListener, buyItemListListener);
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
        if(snackList.size() == 4) {
            return;
        }
        snackList.add(new ItemInfo(context, R.drawable.snack_1, R.string.snack1, 360, R.string.snack1_info, 3));
        snackList.add(new ItemInfo(context, R.drawable.snack_2, R.string.snack2, 70, R.string.snack2_info, 6));
        snackList.add(new ItemInfo(context, R.drawable.snack_3, R.string.snack3, 70, R.string.snack3_info, 19));
        snackList.add(new ItemInfo(context, R.drawable.snack_4, R.string.snack4, 120, R.string.snack4_info, 2));

    }
}
